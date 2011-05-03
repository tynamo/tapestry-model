package org.tynamo.descriptor.annotation.handlers;

import org.apache.commons.lang.Validate;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.annotation.extension.InitialValue;
import org.tynamo.descriptor.extension.InitialValueDescriptorExtension;

/**
 * Creates a {@link org.tynamo.descriptor.extension.InitialValueDescriptorExtension} using the
 * information retrieved from a {@link org.tynamo.descriptor.annotation.extension.InitialValue} annotation.
 *
 * @author pruggia
 */
public class InitialValueAnnotationHandler extends AbstractAnnotationHandler
		implements DescriptorAnnotationHandler<InitialValue, TynamoPropertyDescriptor>
{

	/**
	 * Creates a {@link org.tynamo.descriptor.extension.InitialValueDescriptorExtension} and adds it to
	 * the property descriptor.
	 *
	 * @param annotation Annotation added to the property. It cannot be null.
	 * @param descriptor The property descriptor. It cannot be null.
	 * @return The provided descriptor with the extension added.
	 */
	public TynamoPropertyDescriptor decorateFromAnnotation(final InitialValue annotation,
													  final TynamoPropertyDescriptor descriptor)
	{
		Validate.notNull(annotation, "The annotation cannot be null");
		Validate.notNull(descriptor, "The descriptor cannot be null");
		InitialValueDescriptorExtension extension = new InitialValueDescriptorExtension(annotation.value());
		descriptor.addExtension(InitialValueDescriptorExtension.class
				.getName(), extension);
		return descriptor;
	}
}

