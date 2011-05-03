package org.tynamo.descriptor.annotation.beaneditor;

import org.tynamo.PageType;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BeanModel {


	String context() default "";

	PageType pageType() default PageType.DEFAULT;

	/**
	 * A comma-seperated list of property names to be added to the BeanModel. Cells for added columns will be blank unless
	 * a cell override is provided.
	 */
//	String add() default "";

	/**
	 * A comma-separated list of property names to be removed from the BeanModel. The names are case-insensitive.
	 */
	String exclude() default "";

	/**
	 * A comma-separated list of property names to be retained from the BeanModel. Only these properties will be retained,
	 * and the properties will also be reordered. The names are case-insensitive.
	 */
	String include() default "";

	/**
	 * A comma-separated list of property names indicating the order in which the properties should be presented. The names
	 * are case insensitive. Any properties not indicated in the list will be appended to the end of the display order.
	 * <p/>
	 * * @see org.apache.tapestry5.beaneditor.ReorderProperties
	 */
	String reorder() default "";

}
