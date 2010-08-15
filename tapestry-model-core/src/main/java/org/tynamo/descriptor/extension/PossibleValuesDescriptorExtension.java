package org.tynamo.descriptor.extension;

import java.util.Map;

/**
 * Extension to the property descriptor that holds information about possible
 * values.
 *
 * @author pruggia
 */
public class PossibleValuesDescriptorExtension extends ExpressionExtension
{

	public PossibleValuesDescriptorExtension(String theExpression)
	{
		super(theExpression);
	}

	public PossibleValuesDescriptorExtension(String theExpression, Map context)
	{
		super(theExpression, context);
	}
}
