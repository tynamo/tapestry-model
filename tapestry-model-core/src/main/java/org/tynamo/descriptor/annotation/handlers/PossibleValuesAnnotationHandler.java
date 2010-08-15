/* vim: set ts=2 et sw=2 cindent fo=qroca: */

package org.tynamo.descriptor.annotation.handlers;

import org.apache.commons.lang.Validate;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.annotation.PossibleValues;
import org.tynamo.descriptor.extension.PossibleValuesDescriptorExtension;

/**
 * Creates a {@link org.tynamo.descriptor.extension.PossibleValuesDescriptorExtension} using the
 * information retrieved from a {@link org.tynamo.descriptor.annotation.PossibleValues} annotation.
 *
 * @author pruggia
 */
public class PossibleValuesAnnotationHandler extends AbstractAnnotationHandler
		implements DescriptorAnnotationHandler<PossibleValues, TynamoPropertyDescriptor>
{

	/**
	 * Creates a {@link org.tynamo.descriptor.extension.PossibleValuesDescriptorExtension} and adds it to the
	 * property descriptor.
	 *
	 * @param annotation Annotation added to the property. It cannot be null.
	 * @param descriptor The property descriptor. It cannot be null.
	 * @return Returns descriptor, with the possible values extension.
	 */
	public TynamoPropertyDescriptor decorateFromAnnotation(final PossibleValues annotation,
													  final TynamoPropertyDescriptor descriptor)
	{
		Validate.notNull(annotation, "The annotation cannot be null");
		Validate.notNull(descriptor, "The descriptor cannot be null");

		PossibleValuesDescriptorExtension extension = new PossibleValuesDescriptorExtension(annotation.value());
		descriptor.addExtension(PossibleValuesDescriptorExtension.class.getName(), extension);
		return descriptor;
	}
}

