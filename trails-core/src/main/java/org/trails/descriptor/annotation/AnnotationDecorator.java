package org.trails.descriptor.annotation;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import ognl.Ognl;

import org.trails.descriptor.DescriptorDecorator;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IDescriptor;
import org.trails.descriptor.IDescriptorExtension;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.TrailsDescriptor;
import org.trails.hibernate.EmbeddedDescriptor;

/**
 * This class uses the annotations on a given class or property to modify its
 * descriptor
 * 
 * @author Chris Nelson
 */
public class AnnotationDecorator implements DescriptorDecorator {

	public IClassDescriptor decorate(IClassDescriptor descriptor) {

		// Phase I, decorate class descriptors
		Annotation[] classAnnotations = descriptor.getType().getAnnotations();
		IClassDescriptor decoratedDescriptor = (IClassDescriptor) decorateFromAnnotations(
				descriptor, classAnnotations);

		// Phase II, decorate property descriptors for the class
		ArrayList decoratedPropertyDescriptors = new ArrayList();
		for (Iterator iter = descriptor.getPropertyDescriptors().iterator(); iter
				.hasNext();) {
			IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) iter
					.next();
			/**
			 * Why do we clone? The root property descriptor may go several
			 * levels deep. We chomp thru constructing a class descriptor that
			 * references all lower level property descriptors. We do this for
			 * every class. Everything is keyed for fleet access on classname.
			 */
			IPropertyDescriptor clonedDescriptor = decoratePropertyDescriptor(propertyDescriptor);

			if (clonedDescriptor.isEmbedded()) { // recursively decorate
													// embedded components
													// starting at the top with
													// classes, then properties
				clonedDescriptor = (EmbeddedDescriptor) decorate((EmbeddedDescriptor) clonedDescriptor);
			}
			decoratedPropertyDescriptors.add(clonedDescriptor);
		}
		decoratedDescriptor
				.setPropertyDescriptors(decoratedPropertyDescriptors);
		sortDescriptors(decoratedDescriptor);
		return decoratedDescriptor;
	}

	protected IPropertyDescriptor decoratePropertyDescriptor(
			IPropertyDescriptor propertyDescriptor) {
		IPropertyDescriptor clonedDescriptor = (IPropertyDescriptor) propertyDescriptor
				.clone();
		try {// Phase I PropertyDescriptors
			Field propertyField = clonedDescriptor.getBeanType()
					.getDeclaredField(propertyDescriptor.getName());
			clonedDescriptor = (IPropertyDescriptor) decorateFromAnnotations(
					clonedDescriptor, propertyField.getAnnotations());

		} catch (Exception ex) {
			// don't care
		}
		try {// Phase II MethodDescriptors
			PropertyDescriptor beanPropDescriptor = (PropertyDescriptor) Ognl
					.getValue("propertyDescriptors.{? name == '"
							+ propertyDescriptor.getName() + "'}[0]",
							Introspector.getBeanInfo(clonedDescriptor
									.getBeanType()));

			Method readMethod = beanPropDescriptor.getReadMethod();
			clonedDescriptor = (IPropertyDescriptor) decorateFromAnnotations(
					clonedDescriptor, readMethod.getAnnotations());
		} catch (Exception ex) {
			// System.out.println(propertyDescriptor.getName());
			// ex.printStackTrace();
			// don't care
		}

		try {
			/**
			 * PHASE III Extensions, do we really need this?
			 * 
			 * Yes, much of our descriptors are no implemented as
			 * IDescriptorExtensions instead of true TrialsPropertyDescriptors.
			 * So in order to populate the IDescriptorExtension instance
			 * properly, we need to dredge out all extensions for any given
			 * class and send them in to be decorated for both properties and
			 * methods.
			 * 
			 * Is the framework smart enough to ask into the extensions at
			 * runtime?
			 */
			Map<String, IDescriptorExtension> extensions = ((TrailsDescriptor) clonedDescriptor)
					.getExtensions();
			Set set = extensions.entrySet();
			Iterator iter = set.iterator();
			while (iter.hasNext()) {
				Field propertyField = clonedDescriptor.getBeanType()
						.getDeclaredField(propertyDescriptor.getName());
				PropertyDescriptor beanPropDescriptor = (PropertyDescriptor) Ognl
						.getValue("propertyDescriptors.{? name == '"
								+ propertyDescriptor.getName() + "'}[0]",
								Introspector.getBeanInfo(clonedDescriptor
										.getBeanType()));
				Method readMethod = beanPropDescriptor.getReadMethod();

				Entry entry = (Entry) iter.next();
				IDescriptorExtension ext = (IDescriptorExtension) entry
						.getValue(); // classes annotations
				clonedDescriptor = decorateFromAnnotations(ext,
						propertyField.getAnnotations()).getPropertyDescriptor();

				// phase IV method annotations; Integer does not work in OGNL,
				if (ext.getPropertyDescriptor().getName() != null) {
					beanPropDescriptor = (PropertyDescriptor) Ognl.getValue(
							"propertyDescriptors.{? name == '"
									+ ext.getPropertyDescriptor().getName()
									+ "'}[0]", Introspector.getBeanInfo(ext
									.getPropertyDescriptor().getBeanType()));

					readMethod = beanPropDescriptor.getReadMethod();
					clonedDescriptor = (IPropertyDescriptor) decorateFromAnnotations(
							ext, readMethod.getAnnotations());
				}
			}
		} catch (Exception ex) {
			// System.out.println(propertyDescriptor.getName());
			// ex.printStackTrace();
			// don't care
		}
		return clonedDescriptor;
	}

	/**
	 * Rearrange the property descriptors by their index
	 * 
	 * @param descriptor
	 */
	private void sortDescriptors(IClassDescriptor descriptor) {
		ArrayList sortedDescriptors = new ArrayList();
		sortedDescriptors.addAll(descriptor.getPropertyDescriptors());
		for (Iterator iter = descriptor.getPropertyDescriptors().iterator(); iter
				.hasNext();) {
			IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) iter
					.next();
			if (propertyDescriptor.getIndex() != IPropertyDescriptor.UNDEFINED_INDEX) {
				Collections.swap(sortedDescriptors, propertyDescriptor
						.getIndex(), sortedDescriptors
						.indexOf(propertyDescriptor));
			}
		}
		descriptor.setPropertyDescriptors(sortedDescriptors);

	}

	private IDescriptor decorateFromAnnotations(IDescriptor descriptor,
			Annotation[] annotations) {
		IDescriptor clonedDescriptor = descriptor;
		if (annotations.length > 0) { // lets get preventive here
			clonedDescriptor = (IDescriptor) descriptor.clone();

			for (int i = 0; i < annotations.length; i++) {
				Annotation annotation = annotations[i];
				// If the annotation type itself has a DescriptorAnnotation,
				// it's
				// one of ours
				DescriptorAnnotation handlerAnnotation = annotation
						.annotationType().getAnnotation(
								DescriptorAnnotation.class);
				if (handlerAnnotation != null) {
					try {
						DescriptorAnnotationHandler handler = handlerAnnotation
								.value().newInstance();

						/**
						 * !! This is how we get our properties migrated from
						 * our annotation to our property descriptor !!
						 */
						clonedDescriptor = handler.decorateFromAnnotation(
								annotation, clonedDescriptor);
					} catch (Exception ex) {
						// ex.printStackTrace();
					}
				}
			}
		}
		return clonedDescriptor;
	}

	private IDescriptorExtension decorateFromAnnotations(
			IDescriptorExtension descriptor, Annotation[] annotations) {
		IDescriptorExtension clonedDescriptor = descriptor;
		if (annotations.length > 0) { // lets get preventive here
			clonedDescriptor = (IDescriptorExtension) descriptor.clone();

			for (int i = 0; i < annotations.length; i++) {
				Annotation annotation = annotations[i];
				// If the annotation type itself has a DescriptorAnnotation,
				// it's
				// one of ours
				DescriptorExtensionAnnotation handlerAnnotation = annotation
						.annotationType().getAnnotation(
								DescriptorExtensionAnnotation.class);
				if (handlerAnnotation != null) {
					try {
						DescriptorExtensionAnnotationHandler handler = handlerAnnotation
								.value().newInstance();

						/**
						 * !! This is how we get our properties migrated from
						 * our annotation to our property descriptor !!
						 */
						clonedDescriptor = handler.decorateFromAnnotation(
								annotation, clonedDescriptor);
						// decoratedExtensions.put(clonedDescriptor.getBeanType().getName(),
						// clonedDescriptor);
					} catch (Exception ex) {
						// ex.printStackTrace();
					}
				}
			}
		}
		return clonedDescriptor;
	}

	private Field getPropertyField(IPropertyDescriptor clonedDescriptor,
			Class beanType) {
		Field propertyField = null;
		try {
			propertyField = beanType.getDeclaredField(clonedDescriptor
					.getName());
		} catch (NoSuchFieldException ex) {
			if (beanType.getSuperclass() instanceof java.lang.Object)
				return null;
			else {
				// recursion !!!
				propertyField = getPropertyField(clonedDescriptor, beanType
						.getSuperclass());
			}
		}
		return propertyField;
	}
}
