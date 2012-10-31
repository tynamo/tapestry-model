package org.tynamo.descriptor.annotation.handlers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;

public class AnnotationHandlerUtils
{

	/**
	 * This method will check if an annotation property has a default value by
	 * see if there is a field named DEFAULT_ + property name.  If it has a default
	 * value, we only set the property on the target object if the annotation property is NOT set to
	 * the default value.
	 *
	 * @param propertyDescriptorAnno
	 * @param annotationMethod
	 * @return
	 */
	public static boolean isDefault(Annotation propertyDescriptorAnno, Method annotationMethod)
	{
		try
		{
			Field defaultField = propertyDescriptorAnno.getClass().getField("DEFAULT_" + annotationMethod.getName());
			if (defaultField != null)
			{
				if (annotationMethod.invoke(propertyDescriptorAnno).equals(
					defaultField.get(propertyDescriptorAnno)))
				{
					return true;
				}
			}
			return false;
		} catch (Exception ex)
		{
			return false;
		}
	}

	/**
	 * For each attribute of annotation, will search for a matching property on
	 * the target and set it with the value of the attribute unless the attribute
	 * is set to the "default" value
	 *
	 * @param annotation
	 * @param descriptor
	 */
	public static void setPropertiesFromAnnotation(Annotation annotation, Object target)
	{
		/**
		 * !! This is how we get our properties migrated from our
		 * annotation to our property descriptor !!
		 */
		for (Method annotationMethod : annotation.getClass().getMethods())
		{
			try
			{
				if (!isDefault(annotation, annotationMethod))
				{
					PropertyUtils.setProperty(target,
						annotationMethod.getName(),
						annotationMethod.invoke(annotation)
					);
				}

			} catch (Exception e)
			{
				// ignored
			}
		}
	}

}
