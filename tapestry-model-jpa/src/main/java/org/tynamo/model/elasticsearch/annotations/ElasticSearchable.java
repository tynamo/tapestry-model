package org.tynamo.model.elasticsearch.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.tapestry5.ioc.annotations.AnnotationUseContext;
import org.apache.tapestry5.ioc.annotations.UseWith;
import org.tynamo.descriptor.annotation.handlers.HandledBy;

/**
 * The Interface ElasticSearchable.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@HandledBy("ElasticSearchAnnotationHandler")
@Documented
@UseWith(AnnotationUseContext.BEAN)
public @interface ElasticSearchable {
	/** The index name to use */
	String indexName() default "";
}
