package org.tynamo.model.elasticsearch.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Include this property's fields in the index.
 * 
 * <p>
 * You can use the <code>fields</code> attribute to specify which fields should be embedded. Leave empty to embed all fields.
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticSearchEmbedded {
	/** prefix to use in index */
	String prefix() default "";

	/** fields to embed in index */
	String[] fields() default {};

	/** embedding mode to use */
	Mode mode() default Mode.embedded;

	public enum Mode {
		embedded, nested, object
	}
}
