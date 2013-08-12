package org.tynamo.model.jpa.services;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceException;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.slf4j.Logger;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.EmbeddedDescriptor;
import org.tynamo.descriptor.IdentifierDescriptor;
import org.tynamo.descriptor.IdentifierDescriptorImpl;
import org.tynamo.descriptor.ObjectReferenceDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.decorators.DescriptorDecorator;
import org.tynamo.descriptor.factories.DescriptorFactory;
import org.tynamo.exception.TynamoRuntimeException;
import org.tynamo.model.exception.MetadataNotFoundException;
import org.tynamo.model.jpa.TynamoJpaSymbols;
import org.tynamo.model.jpa.internal.ConfigurableEntityManagerProvider;

/**
 * This decorator will add metadata information. It will replace simple
 * reflection based TynamoPropertyTynamoPropertyDescriptors with appropriate
 * Hibernate descriptors <p/> Background... TynamoDescriptorService operates one
 * ReflectorDescriptorFactory - TynamoDescriptorService iterates/scans all class
 * types encountered - ReflectorDescriptorFactory allocates property descriptor
 * instance for the class type - TynamoDescriptorService decorates property
 * descriptor by calling this module JPADescriptorDecorator -
 * JPADescriptorDecorator caches the decorated property descriptor into a
 * decorated descriptor list - decorated descriptor list gets populated into
 * class descriptor for class type - TynamoDescriptorService finally populates
 * decorated class descriptor and it's aggregated list of decorated property
 * descriptors into it's own list/cache of referenced class descriptors
 *
 * @see TynamoPropertyDescriptor
 * @see ObjectReferenceDescriptor
 * @see CollectionDescriptor
 * @see EmbeddedDescriptor
 */
public class JpaDescriptorDecorator implements DescriptorDecorator
{

	private Logger logger;

	private DescriptorFactory descriptorFactory;

	/** Columns longer than this will have their large property set to true. */
	private final int largeColumnLength;

	private final boolean ignoreNonEntityTypes;

	private EntityManager entityManager;

	public JpaDescriptorDecorator(
	                              DescriptorFactory descriptorFactory, @Autobuild ConfigurableEntityManagerProvider entityManagerProvider,
	                              @Symbol(TynamoJpaSymbols.LARGE_COLUMN_LENGTH)
	                              int largeColumnLength,
	                              @Symbol(TynamoJpaSymbols.IGNORE_NON_HIBERNATE_TYPES)
	                              boolean ignoreNonEntityTypes,
	                              Logger logger) {
		entityManager = entityManagerProvider.getEntityManager();
		this.descriptorFactory = descriptorFactory;
		this.largeColumnLength = largeColumnLength;
		this.ignoreNonEntityTypes = ignoreNonEntityTypes;
		this.logger = logger;
	}

	public TynamoClassDescriptor decorate(TynamoClassDescriptor descriptor) {
		ArrayList<TynamoPropertyDescriptor> decoratedPropertyDescriptors = new ArrayList<TynamoPropertyDescriptor>();

		Class type = descriptor.getBeanType();
		Metamodel metamodel = null;

		try {
			metamodel = findMetadata(type);
		} catch (MetadataNotFoundException e) {
			if (ignoreNonEntityTypes) {
				logger.warn("MetadataNotFound! Ignoring:" + descriptor.getBeanType().toString());
				return descriptor;
			} else {
				throw new TynamoRuntimeException(e);
			}
		}

		Set<String> idProperties = getIdentifierProperties(type);
		for (TynamoPropertyDescriptor propertyDescriptor : descriptor.getPropertyDescriptors()) {
			try {
				TynamoPropertyDescriptor descriptorReference;
				if (idProperties != null && idProperties.contains(propertyDescriptor.getName())) {
					// FIXME should we mark an identifier descriptor as part of a composite key?
					descriptorReference = createIdentifierDescriptor(type, propertyDescriptor, descriptor);
				} else if (notJpaProperty(metamodel, type, propertyDescriptor)) {
					// If this is not a jpa property (i.e. marked Transient), it's certainly not searchable
					// Are there any other properties like this?
					propertyDescriptor.setSearchable(false);
					descriptorReference = propertyDescriptor;
				} else {
					//Attribute mappingProperty = getMapping(type).getAttribute(propertyDescriptor.getName());
					descriptorReference = decoratePropertyDescriptor(type, metamodel, propertyDescriptor,
																	 descriptor);
				}

				decoratedPropertyDescriptors.add(descriptorReference);

			} catch (PersistenceException e) {
				throw new TynamoRuntimeException(e);
			}
		}

		descriptor.setPropertyDescriptors(decoratedPropertyDescriptors);
		descriptor.setSearchable(true);

		return descriptor;
	}

	protected TynamoPropertyDescriptor decoratePropertyDescriptor(Class type, Metamodel metamodel,
																  TynamoPropertyDescriptor propertyDescriptor, TynamoClassDescriptor parentClassDescriptor) {
		ManagedType managedType = metamodel.managedType(parentClassDescriptor.getBeanType());
		Attribute attribute;
		try {
			attribute = managedType.getAttribute(propertyDescriptor.getName());
		} catch (IllegalArgumentException ex) { // Attribute not available - read only!
			propertyDescriptor.setReadOnly(true);
			return propertyDescriptor;
		}

		TynamoPropertyDescriptor descriptorReference = propertyDescriptor;

		if (attribute.isCollection()) {
			assert (attribute instanceof PluralAttribute);
			PluralAttribute pluralAttribute = (PluralAttribute) attribute;
			descriptorReference = decorateCollectionDescriptor(pluralAttribute, propertyDescriptor, parentClassDescriptor);
		} else if (!attribute.isCollection()) {
			switch (attribute.getPersistentAttributeType()) {
				case EMBEDDED:
					descriptorReference = buildEmbeddedDescriptor(type, metamodel, propertyDescriptor, parentClassDescriptor);
					break;
				case BASIC:
					SingularAttribute singularAttribute = managedType.getSingularAttribute(attribute.getName());
					descriptorReference.setLength(findColumnLength(singularAttribute));
					descriptorReference.setLarge(isLarge(singularAttribute));
					if (!singularAttribute.isOptional()) {
						descriptorReference.setRequired(true);
					}
					if (isNotInsertable(singularAttribute) && isNotUpdateable(singularAttribute)) {
						descriptorReference.setReadOnly(true);
					}
					break;
				case ELEMENT_COLLECTION:
					break;
				case ONE_TO_ONE:
					descriptorReference = decorateAssociationDescriptor(attribute, metamodel, propertyDescriptor,
																		parentClassDescriptor);
					break;
				case MANY_TO_ONE:
					descriptorReference = decorateAssociationDescriptor(attribute, metamodel, propertyDescriptor,
																		parentClassDescriptor);
					break;
			}
		}
		return descriptorReference;

		/* TODO: enum handling?
		} else if (entityType.getJavaType().isEnum()) {
			propertyDescriptor.addExtension(EnumReferenceDescriptor.class.getName(), new EnumReferenceDescriptor(entityType.getJavaType()));
		}*/
	}

	private CollectionDescriptor decorateCollectionDescriptor(PluralAttribute pluralAttribute, TynamoPropertyDescriptor propertyDescriptor, TynamoClassDescriptor parentClassDescriptor) {

		CollectionDescriptor collectionDescriptor = new CollectionDescriptor(pluralAttribute.getJavaType(), propertyDescriptor);
		collectionDescriptor.setElementType(pluralAttribute.getElementType().getJavaType());

		switch (pluralAttribute.getPersistentAttributeType()) {
			case ONE_TO_MANY: {
				collectionDescriptor.setOneToMany(true);

				Annotation a = getAnnotation(pluralAttribute.getJavaMember(), OneToMany.class);
				if (a != null) {
					OneToMany aOneToMany = (OneToMany) a;
					collectionDescriptor.setChildRelationship(aOneToMany.orphanRemoval());
					String mappedBy = aOneToMany.mappedBy();
					if (!"".equals(mappedBy)) {
						collectionDescriptor.setInverseProperty(mappedBy);
						parentClassDescriptor.setHasCyclicRelationships(true);
					}
				}
				break;
			}
			case MANY_TO_MANY:
				break;
		}
		return collectionDescriptor;
	}

	private boolean isNotInsertable(SingularAttribute singularAttribute) {
		Annotation a = singularAttribute.getType().getJavaType().getAnnotation(Column.class);
		if (a != null) {
			Column column = (Column) a;
			return !column.insertable();
		} else {
			return false;
		}
	}

	private boolean isNotUpdateable(SingularAttribute singularAttribute) {
		Annotation a = singularAttribute.getType().getJavaType().getAnnotation(Column.class);
		if (a != null) {
			Column column = (Column) a;
			return !column.updatable();
		} else {
			return false;
		}
	}

	private EmbeddedDescriptor buildEmbeddedDescriptor(Class type, Metamodel metamodel,
													   TynamoPropertyDescriptor descriptor, TynamoClassDescriptor parentClassDescriptor) {

		//EmbeddableType componentMapping = (EmbeddableType) metamodel.getValue();
		EmbeddableType componentMapping = metamodel.embeddable(descriptor.getPropertyType());

		TynamoClassDescriptor embeddedClassDescriptor = descriptorFactory.buildClassDescriptor(descriptor.getPropertyType());

		ArrayList<TynamoPropertyDescriptor> decoratedProperties = new ArrayList<TynamoPropertyDescriptor>();

		// go thru each property and decorate it with JPA info
		for (TynamoPropertyDescriptor propertyDescriptor : embeddedClassDescriptor.getPropertyDescriptors()) {
			if (notAHibernateProperty(componentMapping, propertyDescriptor)) {
				decoratedProperties.add(propertyDescriptor);
			} else {
				//Attribute property = componentMapping.getAttribute(propertyDescriptor.getName());
				TynamoPropertyDescriptor TynamoPropertyDescriptor = decoratePropertyDescriptor(embeddedClassDescriptor.getBeanType(),
																							   metamodel, propertyDescriptor, parentClassDescriptor);
				decoratedProperties.add(TynamoPropertyDescriptor);
			}
		}

		embeddedClassDescriptor.setPropertyDescriptors(decoratedProperties);

		return new EmbeddedDescriptor(type, descriptor, embeddedClassDescriptor);
	}

	/**
	 * The default way to order our property descriptors is by the order they
	 * appear in the jpa config, with id first. Any non-mapped properties
	 * are tacked on at the end, til I think of a better way.
	 *
	 * @param propertyDescriptors
	 * @return
	 */
//	protected List sortPropertyDescriptors(Class type, List propertyDescriptors) {
//		ArrayList sortedPropertyDescriptors = new ArrayList();
//
//		try {
//			sortedPropertyDescriptors.add(Ognl.getValue("#this.{? identifier == true}[0]", propertyDescriptors));
//			for (Object obj : findMetadata(type).managedType(type).getAttributes()) {
//				Attribute mapping = (Attribute) obj;
//				sortedPropertyDescriptors.addAll((List) Ognl.getValue("#this.{ ? name == \"" + mapping.getName()
//																	  + "\"}", propertyDescriptors));
//			}
//		} catch (Exception ex) {
//			throw new TynamoRuntimeException(ex);
//		}
//		return sortedPropertyDescriptors;
//	}

	/**
	 * Find the Hibernate metadata for this type, traversing up the hierarchy to
	 * supertypes if necessary
	 *
	 * @param type
	 * @return
	 */
	protected <T> Metamodel findMetadata(Class<T> type) throws MetadataNotFoundException {
		Metamodel managedType = entityManager.getMetamodel();
		if (managedType != null) {
			return managedType;
		}
		if (!type.equals(Object.class)) {
			return findMetadata(type.getSuperclass());
		} else {
			throw new MetadataNotFoundException("Failed to find metadata.");
		}
	}

	/**
	 * Checks to see if a property descriptor is in a component mapping
	 *
	 * @param componentMapping
	 * @param propertyDescriptor
	 * @return true if the propertyDescriptor property is in componentMapping
	 */
	protected boolean notAHibernateProperty(EmbeddableType componentMapping, TynamoPropertyDescriptor propertyDescriptor) {
		for (Object obj : componentMapping.getAttributes()) {
			Attribute property = (Attribute) obj;
			if (property.getName().equals(propertyDescriptor.getName())) {
				return false;
			}
		}
		return true;
	}

	private boolean isLarge(SingularAttribute mappingProperty) {
		// Hack to avoid setting large property if length
		// is exactly equal to Hibernate default column length
		return findColumnLength(mappingProperty) != 255
			   && findColumnLength(mappingProperty) > largeColumnLength;
	}

	private Annotation getAnnotation(Member member, Class annotationType) {
		return member instanceof Field ? ((Field) member).getAnnotation(annotationType)
			: member instanceof Method ? ((Method) member).getAnnotation(annotationType) : null;
	}

	private int findColumnLength(SingularAttribute mappingProperty) {
		Annotation a = getAnnotation(mappingProperty.getJavaMember(), Column.class);
		if (a != null) {
			Column column = (Column) a;
			return column.length();
		} else {
			return 255; // default length
		}
	}


	protected boolean notJpaProperty(Metamodel metamodel, Class classType, TynamoPropertyDescriptor descriptor) {
		ManagedType type = metamodel.managedType(classType);
		Set<Attribute> attrs = type.getAttributes();
		boolean ret = true;
		for (Attribute a : attrs) {
			if (a.getName().equals(descriptor.getName())) {
				ret = false;
			}
		}
		return ret;
	}

	/**
	 * @param type
	 * @param descriptor
	 * @param parentClassDescriptor
	 * @return
	 */
	private IdentifierDescriptor createIdentifierDescriptor(Class type, TynamoPropertyDescriptor descriptor, TynamoClassDescriptor parentClassDescriptor) {
		IdentifierDescriptor identifierDescriptor;
		ManagedType mapping = getMapping(type);

		if (mapping.getAttribute(descriptor.getName()).getPersistentAttributeType().equals(Attribute.PersistentAttributeType.EMBEDDED)) {
			EmbeddedDescriptor embeddedDescriptor = buildEmbeddedDescriptor(type,
																			findMetadata(type), descriptor, parentClassDescriptor);
			embeddedDescriptor.setIdentifier(true);
			identifierDescriptor = embeddedDescriptor;
		} else {
			identifierDescriptor = new IdentifierDescriptorImpl(type, descriptor);
		}

		if ( getAnnotation(mapping.getSingularAttribute(descriptor.getName()).getJavaMember(), GeneratedValue.class) == null)
		{
			identifierDescriptor.setGenerated(false);
		}

		return identifierDescriptor;
	}

	public TynamoPropertyDescriptor decorateAssociationDescriptor(Attribute attribute, Metamodel metamodel,
																  TynamoPropertyDescriptor descriptor, TynamoClassDescriptor parentClassDescriptor) {

		ObjectReferenceDescriptor descriptorReference = new ObjectReferenceDescriptor(attribute.getJavaType(), descriptor, attribute.getJavaType());

		System.out.println("Association Type: " + attribute.getPersistentAttributeType());
		switch (attribute.getPersistentAttributeType()) {
			case ONE_TO_ONE: {
				Annotation a = attribute.getJavaType().getAnnotation(OneToOne.class);
				if (a != null) {
					OneToOne aOneToOne = (OneToOne) a;
					String inverseProperty = aOneToOne.mappedBy();
					//descriptorReference.setChildRelationship(aOneToOne.orphanRemoval());

					//TODO: understand what is going on here and optimize it
					if ("".equals(inverseProperty)) {
						// http://forums.jpa.org/viewtopic.php?t=974287&sid=12d018b08dffe07e263652190cfc4e60
						// Caution... this does not support multiple
						// class references across the OneToOne relationship
						Class returnType = attribute.getJavaType();
						for (int i = 0; i < returnType.getDeclaredMethods().length; i++) {
							if (returnType.getDeclaredMethods()[i].getReturnType().equals(attribute.getDeclaringType().getJavaType())) {
								Method theProperty = returnType.getDeclaredMethods()[i];
								/* strips preceding 'get' */
								inverseProperty = theProperty.getName().substring(3).toLowerCase();
								break;
							}
						}
					}
				} else {
					return descriptorReference;
				}
			}
			break;
			case MANY_TO_ONE: {
				Annotation a = attribute.getJavaType().getAnnotation(OneToOne.class);
				if (a != null) {
					ManyToOne aManyToOne = (ManyToOne) a;
					//TODO: do some stuff?
				}
			}
			break;
		}
		return descriptorReference;
	}

	/**
	 * @param type
	 * @return
	 */
	protected ManagedType getMapping(Class type) {
		//Configuration cfg = entityManagerSource.getConfiguration();
		return entityManager.getMetamodel().managedType(type);
		//return cfg.getClassMapping(type.getName());
	}


	private Set<String> getIdentifierProperties(Class type) {
		try {
			ManagedType mapping = getMapping(type);
			if (!(mapping instanceof EntityType)) return null;
			EntityType entityType = (EntityType)mapping;
			// NOTE either eclipselink 2.4.0 has a bug or I don't understand how hasSingleIdAttribute is supposed to work, but
			// it returns true even if a class is annotated with @IdClass and has multiple @Id attributes.
			// Directly check for annotation as well as a fix
			if (!entityType.hasSingleIdAttribute() || type.isAnnotationPresent(IdClass.class)) {
				Set<SingularAttribute> idAttrs = entityType.getIdClassAttributes();
				Set<String> result = new HashSet<String>(idAttrs.size());
				for (SingularAttribute attr : idAttrs)
					result.add(attr.getName());
				return result;
			}
			Set<String> result =  new HashSet<String>(1);
			result.add(entityType.getId(entityType.getIdType().getJavaType()).getName());
			return result;
		} catch (PersistenceException e) {
			throw new TynamoRuntimeException(e);
		}
	}
}
