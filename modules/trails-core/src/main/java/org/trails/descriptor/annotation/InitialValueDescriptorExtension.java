/* vim: set ts=2 et sw=2 cindent fo=qroca: */

package org.trails.descriptor.annotation;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.trails.descriptor.IDescriptorExtension;

/**
 * Extension to the property descriptor that holds information about possible
 * values.
 *
 * @author pruggia
 */
public class InitialValueDescriptorExtension implements IDescriptorExtension
{

	/**
	 * The serial id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The class logger.
	 */
	private static Log log = LogFactory.getLog(InitialValueDescriptorExtension.class);

	/**
	 * OGNL expression to evaluate the initial value for the property.
	 * <p/>
	 * Cannot be null.
	 */
	private String expression;

	/**
	 * OGNL expression to evaluate the initial value for the property.
	 *
	 * @return a String, never null.
	 */
	public String getExpression()
	{
		return expression;
	}

	/**
	 * Creates a {@link PossibleValuesDescriptorExtension}.
	 *
	 * @param theExpression OGNL expression to evaluate the initial value for the
	 *                      property, cannot be null.
	 */
	public InitialValueDescriptorExtension(final String theExpression)
	{
		super();
		Validate.notNull(theExpression, "The expression cannot be null");
		expression = theExpression;
	}

	/**
	 * Method used to initialize the value of the filtering property using the
	 * value of the filtered one (for example, initialize the country based on the
	 * value of the state property.
	 *
	 * @param model				  the model used by the edit page, cannot be null.
	 * @param propertyName		   name of the property we are initializing,
	 *                               cannot be null.
	 * @param propertySelectionModel The selection model. It cannot be null.
	 */
	public void initValue(final Object model, final String propertyName,
						  final IPropertySelectionModel propertySelectionModel)
	{
		Validate.notNull(model, "The model cannot be null");
		Validate.notNull(propertyName, "The property name cannot be null");
		Validate.notNull(propertySelectionModel, "The property selection model" + " cannot be null");
		Object providerValue = null;
		try
		{
			providerValue = Ognl.getValue(getExpression(), model);
		} catch (Exception ex)
		{
			log.warn("Exception thrown evaluationg " + getExpression(), ex);
		}
		if (providerValue != null)
		{
			try
			{
				Ognl.setValue(propertyName, model, providerValue);
			} catch (OgnlException ex)
			{
				log.warn("Exception thrown evaluating " + getExpression(), ex);
			}
		} else
		{
			try
			{
				Ognl.setValue(propertyName, model, propertySelectionModel.getOption(0));
			} catch (OgnlException ex)
			{
				log.warn("Exception thrown evaluating " + getExpression(), ex);
			}
		}
	}
}

