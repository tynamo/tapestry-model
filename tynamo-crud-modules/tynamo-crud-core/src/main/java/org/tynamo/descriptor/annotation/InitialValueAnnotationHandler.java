package org.tynamo.descriptor.annotation;

import org.apache.commons.lang.Validate;
import org.tynamo.descriptor.TrailsPropertyDescriptor;

/**
 * Creates a {@link InitialValueDescriptorExtension} using the
 * information retrieved from a {@link InitialValue} annotation.
 *
 * @author pruggia
 */
public class InitialValueAnnotationHandler extends AbstractAnnotationHandler
		implements DescriptorAnnotationHandler<InitialValue, TrailsPropertyDescriptor>
{

	/**
	 * Creates a {@link InitialValueDescriptorExtension} and adds it to
	 * the property descriptor.
	 *
	 * @param annotation Annotation added to the property. It cannot be null.
	 * @param descriptor The property descriptor. It cannot be null.
	 * @return The provided descriptor with the extension added.
	 */
	public TrailsPropertyDescriptor decorateFromAnnotation(final InitialValue annotation,
													  final TrailsPropertyDescriptor descriptor)
	{
		Validate.notNull(annotation, "The annotation cannot be null");
		Validate.notNull(descriptor, "The descriptor cannot be null");
		InitialValueDescriptorExtension extension = new InitialValueDescriptorExtension(annotation.value());
		descriptor.addExtension(InitialValueDescriptorExtension.class
				.getName(), extension);
		return descriptor;
	}
}

