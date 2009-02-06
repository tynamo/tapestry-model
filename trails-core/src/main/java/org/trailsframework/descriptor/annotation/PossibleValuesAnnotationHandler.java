/* vim: set ts=2 et sw=2 cindent fo=qroca: */

package org.trailsframework.descriptor.annotation;

import org.apache.commons.lang.Validate;
import org.trailsframework.descriptor.TrailsPropertyDescriptor;

/**
 * Creates a {@link PossibleValuesDescriptorExtension} using the
 * information retrieved from a {@link PossibleValues} annotation.
 *
 * @author pruggia
 */
public class PossibleValuesAnnotationHandler extends AbstractAnnotationHandler
		implements DescriptorAnnotationHandler<PossibleValues, TrailsPropertyDescriptor>
{

	/**
	 * Creates a {@link PossibleValuesDescriptorExtension} and adds it to the
	 * property descriptor.
	 *
	 * @param annotation Annotation added to the property. It cannot be null.
	 * @param descriptor The property descriptor. It cannot be null.
	 * @return Returns descriptor, with the possible values extension.
	 */
	public TrailsPropertyDescriptor decorateFromAnnotation(final PossibleValues annotation,
													  final TrailsPropertyDescriptor descriptor)
	{
		Validate.notNull(annotation, "The annotation cannot be null");
		Validate.notNull(descriptor, "The descriptor cannot be null");

		PossibleValuesDescriptorExtension extension = new PossibleValuesDescriptorExtension(annotation.value());
		descriptor.addExtension(PossibleValuesDescriptorExtension.class.getName(), extension);
		return descriptor;
	}
}

