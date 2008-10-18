package org.trailsframework.pages;

import org.apache.tapestry.commons.components.DateTimeField;
import org.apache.tapestry.commons.components.Editor;
import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanEditContext;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.trailsframework.descriptor.CollectionDescriptor;
import org.trailsframework.descriptor.IPropertyDescriptor;
import org.trailsframework.services.DescriptorService;
import org.trailsframework.services.PersistenceService;
import org.trailsframework.util.GenericSelectionModel;


public class Editors
{

	@Environmental
	@Property(write = false)
	private BeanEditContext beanEditContext;

	@Environmental
	@Property(write = false)
	private PropertyEditContext propertyEditContext;

	@Inject
	private PersistenceService persitenceService;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private ValueEncoderSource valueEncoderSource;


	@Component(parameters = {"value=propertyEditContext.propertyValue", "label=prop:propertyEditContext.label",
			"clientId=propertyEditContext.propertyid", "validate=prop:dateFieldValidator"})
	private DateTimeField dateField;

	@Component(
			parameters = {"value=propertyEditContext.propertyValue", "label=prop:propertyEditContext.label",
					"translate=prop:fckTranslator", "validate=prop:fckValidator", 
					"clientId=prop:propertyEditContext.propertyId", "annotationProvider=propertyEditContext"})
	private Editor fckEditor;


	public SelectModel getSelectModel()
	{
		IPropertyDescriptor propertyDescriptor = descriptorService.getClassDescriptor(beanEditContext.getBeanClass()).getPropertyDescriptor(propertyEditContext.getPropertyId());

		if (propertyDescriptor.isCollection())
		{
			CollectionDescriptor collectionDescriptor = (CollectionDescriptor) propertyDescriptor;
			return new GenericSelectionModel(persitenceService.getInstances(collectionDescriptor.getElementType()));
		} else
		{
			return new GenericSelectionModel(persitenceService.getInstances(propertyEditContext.getPropertyType()));
		}
	}

	/**
	 * Provide a value encoder for a type.
	 *
	 * @return
	 */
	public ValueEncoder getValueEncoderForProperty()
	{
		IPropertyDescriptor propertyDescriptor = descriptorService.getClassDescriptor(beanEditContext.getBeanClass()).getPropertyDescriptor(propertyEditContext.getPropertyId());
		if (propertyDescriptor.isCollection())
		{
			CollectionDescriptor collectionDescriptor = (CollectionDescriptor) propertyDescriptor;
			return valueEncoderSource.getValueEncoder(collectionDescriptor.getElementType());
		} else
		{
			return valueEncoderSource.getValueEncoder(propertyEditContext.getPropertyType());
		}
	}

	public FieldValidator getDateFieldValidator()
	{
		return propertyEditContext.getValidator(dateField);
	}

	public FieldValidator getFckValidator()
	{
		return propertyEditContext.getValidator(fckEditor);
	}

	public FieldTranslator getFckTranslator()
	{
		return propertyEditContext.getTranslator(fckEditor);
	}

}
