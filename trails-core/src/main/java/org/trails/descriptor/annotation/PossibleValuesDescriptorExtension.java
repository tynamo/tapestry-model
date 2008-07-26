package org.trails.descriptor.annotation;

import org.apache.commons.lang.Validate;
import org.trails.descriptor.IDescriptorExtension;

/**
 * Extension to the property descriptor that holds information about possible
 * values.
 *
 * @author pruggia
 */
public class PossibleValuesDescriptorExtension implements IDescriptorExtension
{

	/**
	 * The serial id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Ognl expression that evaluated gets a list of possible values to use with
	 * the current property, cannot be null.
	 */
	private String expression;

	/**
	 * Creates a {@link PossibleValuesDescriptorExtension}.
	 *
	 * @param theExpression Ognl expression that evaluated gets a list of possible
	 *                      values to use with the current property, cannot be null.
	 */
	public PossibleValuesDescriptorExtension(final String theExpression)
	{
		super();
		Validate.notNull(theExpression, "The expression cannot be null");
		expression = theExpression;
	}

	/**
	 * Gets the Ognl expression that evaluated gets a list of possible values to
	 * use with the current property.
	 *
	 * @return a String, never null.
	 */
	public String getExpression()
	{
		return expression;
	}
}
