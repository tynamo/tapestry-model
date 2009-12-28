/* vim: set ts=2 et sw=2 cindent fo=qroca: */

package org.tynamo.descriptor.annotation;

import java.util.Map;

/**
 * Extension to the property descriptor that holds information about possible
 * values.
 *
 * @author pruggia
 */
public class InitialValueDescriptorExtension extends ExpressionExtension
{
	public InitialValueDescriptorExtension(String theExpression)
	{
		super(theExpression);
	}

	public InitialValueDescriptorExtension(String theExpression, Map context)
	{
		super(theExpression, context);
	}
}

