package org.tynamo.descriptor.annotation.beaneditor;

import org.apache.tapestry5.ioc.annotations.AnnotationUseContext;
import org.apache.tapestry5.ioc.annotations.UseWith;
import org.tynamo.descriptor.annotation.handlers.HandledBy;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@UseWith(AnnotationUseContext.BEAN)
@HandledBy("AddPageBeanModelAnnotationHandler")
public @interface AddPageBeanModel
{

	/**
	 * A comma-seperated list of property names to be added to the BeanModel. Cells for added columns will be blank unless
	 * a cell override is provided.
	 */
//	String add() default "";

	/**
	 * A comma-separated list of property names to be removed from the BeanModel  . The names are case-insensitive.
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
