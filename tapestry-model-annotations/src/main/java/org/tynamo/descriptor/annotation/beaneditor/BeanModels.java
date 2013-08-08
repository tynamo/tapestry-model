package org.tynamo.descriptor.annotation.beaneditor;

import org.apache.tapestry5.ioc.annotations.AnnotationUseContext;
import org.apache.tapestry5.ioc.annotations.UseWith;
import org.tynamo.descriptor.annotation.handlers.HandledBy;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@UseWith({AnnotationUseContext.BEAN, AnnotationUseContext.PAGE})
@HandledBy("BeanModelAnnotationHandler")
public @interface BeanModels {

	BeanModel[] value();

}
