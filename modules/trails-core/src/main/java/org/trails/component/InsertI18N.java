package org.trails.component;

import java.text.Format;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.components.Insert;
import org.apache.tapestry.components.InsertMode;
import org.trails.i18n.ResourceBundleMessageSource;

/**
 * Used to insert some text (from a parameter) into the HTML.
 * Emits a value into the response page.
 */
@ComponentClass(allowBody = false)
public abstract class InsertI18N extends Insert
{
	/**
	 * Return the Spring ResourceBundleMessageSource. This is used to implement
	 * i18n in all Trails components, accessing a i18n properties file in the
	 * application instead of accessing the property file located in org.trais.component package.
	 * By doing this, someone who would need i18n wouldn't need to change the property
	 * located in the org.trails.component package and rebuild the trails.jar
	 *
	 * @return
	 */
	@InjectObject("spring:trailsMessageSource")
	public abstract ResourceBundleMessageSource getResourceBundleMessageSource();

	@Parameter(required = true)
	public abstract String getBundleKey();

	@Parameter
	public abstract String getDefaultMessage();

	@Parameter
	public abstract Object getParams();

	@Parameter(defaultValue = "page.locale")
	public abstract Locale getLocale();

	/**
	 * A Format object used to convert the value to a string.
	 *
	 * @return
	 */
	@Parameter
	public abstract Format getFormat();

	/**
	 * If false (the default), then HTML characters in the value are escaped.  If
	 * true, then value is emitted exactly as is.
	 *
	 * @return
	 */
	@Parameter
	public abstract boolean getRaw();

	/**
	 * If true, causes the tag used to define the insert to be used, as well as any informal
	 * parameters bound.
	 *
	 * @return
	 */
	@Parameter(defaultValue = "false")
	public abstract boolean getRenderTag();

	/**
	 * Determines which mode to use: breaks after each line, or wrap each line
	 * as a paragraph.
	 *
	 * @return
	 */
	@Parameter
	public abstract InsertMode getMode();

	public String getValue()
	{
		if (getDefaultMessage() != null)
		{
			return getResourceBundleMessageSource().getMessageWithDefaultValue(getBundleKey(), getParams() != null ? constructServiceParameters(getParams()) : ResourceBundleMessageSource.EMPTY_ARGUMENTS, getLocale(), getDefaultMessage());
		} else
		{
			return getResourceBundleMessageSource().getMessage(getBundleKey(), getParams() != null ? constructServiceParameters(getParams()) : ResourceBundleMessageSource.EMPTY_ARGUMENTS, getLocale());
		}
	}

	/**
	 * Converts a parameter value to an array of objects.
	 *
	 * @param parameterValue the input value which may be
	 *                       <ul>
	 *                       <li>null (returns null)
	 *                       <li>An array of Object (returns the array)
	 *                       <li>A {@link java.util.List}(returns an array of the values in the List})
	 *                       <li>A single object (returns the object as a single-element array)
	 *                       </ul>
	 * @return An array representation of the input object.
	 */

	public static Object[] constructServiceParameters(Object parameterValue)
	{
		if (parameterValue == null)
			return null;

		if (parameterValue instanceof Object[])
			return (Object[]) parameterValue;

		if (parameterValue instanceof List)
		{
			List list = (List) parameterValue;

			return list.toArray();
		}

		return new Object[]{parameterValue};
	}
}
