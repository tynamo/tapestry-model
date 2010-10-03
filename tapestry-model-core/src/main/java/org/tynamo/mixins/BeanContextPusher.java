package org.tynamo.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.BeanDisplay;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.services.Environment;
import org.tynamo.services.TynamoBeanContext;

/**
 * This mixin pushes the {@link TynamoBeanContext} into the {@link Environment} it's meant to be used in a {@link
 * BeanDisplay}
 */
public class BeanContextPusher
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
	 * The container's property
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String property;

	@Log
	void setupRender()
	{
		final TynamoBeanContext tynamoBeanContext = environment.peek(TynamoBeanContext.class);

		if (tynamoBeanContext == null)
		{
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
					return propertyAccess.get(container, property);
				}
			};
			environment.push(TynamoBeanContext.class, context);
		}
	}

	@Log
	void cleanupRender()
	{
		environment.pop(TynamoBeanContext.class);
	}

}
