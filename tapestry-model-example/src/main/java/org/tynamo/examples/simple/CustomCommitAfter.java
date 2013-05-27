package org.tynamo.examples.simple;

import org.apache.tapestry5.ioc.annotations.UseWith;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.apache.tapestry5.ioc.annotations.AnnotationUseContext.*;

/**
 * @see org.apache.tapestry5.hibernate.annotations.CommitAfter
 * @see org.apache.tapestry5.jpa.annotations.CommitAfter
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
@UseWith({COMPONENT, MIXIN, PAGE})
public @interface CustomCommitAfter {

}
