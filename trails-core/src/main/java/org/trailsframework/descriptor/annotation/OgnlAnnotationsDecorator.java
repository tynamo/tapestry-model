package org.trailsframework.descriptor.annotation;

import ognl.Ognl;
import org.trailsframework.descriptor.*;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Creates {@link InitialValueDescriptorExtension} and {@link PossibleValuesDescriptorExtension} extensions
 * using the information retrieved from {@link InitialValue} and {@link PossibleValues} annotations.
 */
public class OgnlAnnotationsDecorator implements DescriptorDecorator
{

	/**
	 * It holds the Map of variables to put into the available namespace (scope) for OGNL expressions.
	 */
	private Map context;

	/**
	 * {@inheritDoc}
	 */
	public TrailsClassDescriptor decorate(TrailsClassDescriptor descriptor)
	{
		decoratePropertyDescriptors(descriptor);
		return descriptor;
	}

	private void decoratePropertyDescriptors(TrailsClassDescriptor descriptor)
	{
		for (TrailsPropertyDescriptor propertyDescriptor : descriptor.getPropertyDescriptors())
		{
			decoratePropertyDescriptor(propertyDescriptor);
			// recursively decorate components
			if (propertyDescriptor.isEmbedded())
			{
				decorate((EmbeddedDescriptor) propertyDescriptor);
			}
		}
	}

	private void decoratePropertyDescriptor(TrailsPropertyDescriptor propertyDescriptor)
	{
		try
		{
			Field propertyField = propertyDescriptor.getBeanType().getDeclaredField(propertyDescriptor.getName());
			decorateFromAnnotations(propertyDescriptor, propertyField.getAnnotations());

		} catch (Exception ex)
		{
			// don't care
		}
		try
		{
			PropertyDescriptor beanPropDescriptor = (PropertyDescriptor) Ognl.getValue("propertyDescriptors.{? name == '" + propertyDescriptor.getName() + "'}[0]",
					Introspector.getBeanInfo(propertyDescriptor.getBeanType()));

			Method readMethod = beanPropDescriptor.getReadMethod();
			decorateFromAnnotations(propertyDescriptor, readMethod.getAnnotations());
		}
		catch (Exception ex)
		{
			// don't care
		}
	}

	private void decorateFromAnnotations(Descriptor descriptor, Annotation[] annotations)
	{
		for (Annotation annotation : annotations)
		{
			if (annotation instanceof InitialValue)
			{
				InitialValueDescriptorExtension extension = new InitialValueDescriptorExtension(((InitialValue) annotation).value(), context);
				descriptor.addExtension(InitialValueDescriptorExtension.class.getName(), extension);
			} else if (annotation instanceof PossibleValues)
			{
				PossibleValuesDescriptorExtension extension = new PossibleValuesDescriptorExtension(((PossibleValues) annotation).value(), context);
				descriptor.addExtension(PossibleValuesDescriptorExtension.class.getName(), extension);
			}
		}
	}

	/**
	 * sets the context value
	 *
	 * @param context
	 */
	public void setContext(Map context)
	{
		this.context = context;
	}
}