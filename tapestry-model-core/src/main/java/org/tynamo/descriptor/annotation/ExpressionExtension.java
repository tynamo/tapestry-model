package org.tynamo.descriptor.annotation;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tynamo.descriptor.DescriptorExtension;

import java.util.Map;


public abstract class ExpressionExtension implements DescriptorExtension
{
	private static Log LOG = LogFactory.getLog(PossibleValuesDescriptorExtension.class);

	/**
	 * Ognl expression that evaluated gets a list of possible values to use with
	 * the current property, cannot be null.
	 */
	private String expression;

	/**
	 * Map of variables to put into the available namespace (scope) for OGNL expressions.
	 */
	private Map context;

	/**
	 * Creates a {@link ExpressionExtension}.
	 *
	 * @param theExpression Ognl expression that evaluated gets a list of possible
	 *                      values to use with the current property, cannot be null.
	 */
	public ExpressionExtension(final String theExpression)
	{
		super();
		Validate.notNull(theExpression, "The expression cannot be null");
		expression = theExpression;
	}


	/**
	 * Creates a {@link ExpressionExtension}.
	 *
	 * @param theExpression Ognl expression that evaluated gets a list of possible
	 *                      values to use with the current property, cannot be null.
	 * @param context
	 */
	public ExpressionExtension(final String theExpression, Map context)
	{
		this(theExpression);
		this.context = context;
	}

	/**
	 * Gets the Ognl expression that evaluated gets a list of possible values to
	 * use with the current property.
	 *
	 * @return a String, never null.
	 */


	/**
	 * Method used to initialize the value of the filtering property using the
	 * value of the filtered one (for example, initialize the country based on the
	 * value of the state property.
	 *
	 * @param model the model used by the edit page, cannot be null.
	 * @return
	 * @throws ognl.OgnlException
	 */
	public Object evaluateExpresion(final Object model) throws OgnlException
	{
		Validate.notNull(model, "The model cannot be null");
		try
		{
			if (context != null)
			{
				return Ognl.getValue(expression, context, model);
			} else
			{
				return Ognl.getValue(expression, model);
			}
		} catch (OgnlException e)
		{
			LOG.warn("Exception thrown evaluationg " + expression, e);
			throw e;
		}
	}
}
