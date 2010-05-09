package org.tynamo.mixins;

import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Log;
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
	 * The BeanDisplay component to which this mixin is attached.
	 */
	@InjectContainer
	private BeanDisplay beanDisplay;

	@Inject
	private Environment environment;

	@Inject
	private PropertyAccess propertyAccess;

	@Log
	void setupRender()
	{
		final TynamoBeanContext tynamoBeanContext = environment.peek(TynamoBeanContext.class);
		final Object object = propertyAccess.get(beanDisplay, "object");

		if (tynamoBeanContext == null)
		{
			TynamoBeanContext context = new TynamoBeanContext()
			{
				public Class getBeanType()
				{
					return object.getClass();
				}

				public Object getBeanInstance()
				{
					return object;
				}

				public String getCurrentProperty()
				{
					return null;
				}

				public void setCurrentProperty(String s)
				{
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
