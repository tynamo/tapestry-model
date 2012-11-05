package org.tynamo.model.elasticsearch.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface ElasticSearchField.
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticSearchField {
	String type() default "";

	Index index() default Index.NOT_SET;

	Store store() default Store.NOT_SET;

	public enum Index {
		yes, analyzed, not_analyzed, no, NOT_SET
	}

	public enum Store {
		yes, no, NOT_SET
	}
}
