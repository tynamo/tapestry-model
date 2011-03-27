package org.tynamo.mixins;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.*;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.services.Environment;
import org.tynamo.internal.InternalConstants;
import org.tynamo.services.BeanModelSourceContext;
import org.tynamo.services.TynamoBeanContext;

/**
 * This mixin has two purposes.
 * <p/>
 * it pushes the {@link TynamoBeanContext} into the {@link Environment} it's meant to be used in a {@link BeanDisplay} or with a {@link Grid}
 * it pushes the {@link BeanModelSourceContext} into the {@link Environment}
 */
public class Advisor
{

	/**
	 * The component to which this mixin is attached.
	 */
	@InjectContainer
	private Object container;

	@Inject
	private Environment environment;

	@Inject
	private PropertyAccess propertyAccess;

	/**
	 * The context key
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL, allowNull = false)
	private String key;

	/**
	 * The container's property to retrieve the object (or bean)
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL, allowNull = false)
	private String containerProperty;

	@SetupRender
	void init()
	{
		pushBeanModelSourceContext();
		if (isTynamoBeanContextNeeded()) pushTynamoBeanContext();
	}

	private void pushBeanModelSourceContext()
	{
		if (StringUtils.isEmpty(key)) key = guessKey();
		BeanModelSourceContext context = new BeanModelSourceContext(key);
		environment.push(BeanModelSourceContext.class, context);
	}


	private void pushTynamoBeanContext()
	{
		if (StringUtils.isEmpty(containerProperty)) containerProperty = guessPropertyName();

		TynamoBeanContext context = new TynamoBeanContext()
		{
			public Class getBeanType()
			{
				return getObject().getClass();
			}

			public Object getBeanInstance()
			{
				return getObject();
			}

			public String getCurrentProperty()
			{
				return null;
			}

			public void setCurrentProperty(String s)
			{
			}

			private Object getObject()
			{
				return propertyAccess.get(container, containerProperty);
			}
		};
		environment.push(TynamoBeanContext.class, context);
	}

	private String guessKey()
	{

		if (container instanceof Grid) return InternalConstants.LIST_PAGE_CONTEXT_KEY;

		if (container instanceof BeanDisplay || container instanceof PropertyDisplay)
		{
			return InternalConstants.SHOW_PAGE_CONTEXT_KEY;
		}

		if (container instanceof BeanEditor || container instanceof BeanEditForm || container instanceof PropertyEditor)
		{
			return InternalConstants.EDIT_PAGE_CONTEXT_KEY;
		}

		throw new RuntimeException(
				"The context key couldn't be guessed from the container, please provide one. eg: avisor.key=\"add\"");
	}

	private String guessPropertyName()
	{
		if (container instanceof Grid) return "row";
		if (container instanceof BeanDisplay) return "object";

		throw new RuntimeException(
				"The container's object property couldn't be guessed, please provide one. eg: avisor.containerProperty=\"bean\"");
	}

	private boolean isTynamoBeanContextNeeded()
	{
		return containerProperty != null || (container instanceof Grid) || (container instanceof BeanDisplay);
	}

	@Log
	void cleanupRender()
	{
		environment.pop(BeanModelSourceContext.class);
		if (isTynamoBeanContextNeeded()) environment.pop(TynamoBeanContext.class);
	}

}
