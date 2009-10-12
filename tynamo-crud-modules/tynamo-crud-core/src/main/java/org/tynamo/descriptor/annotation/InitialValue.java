package org.tynamo.descriptor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to initialize a field based on an OGNL expression, usually
 * another field.
 * <p/>
 * For example, assume you have a car of certain model made by certain make.
 * The car has a relation to the model, but not the make. So you create a
 * transient property make initialized by model.make.<br>
 *
 * @author pruggia
 * @InitialValue("model.make")<br> This expression is only evaluated when editing the entity.
 * <p/>
 * See also {@link PossibleValues}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@DescriptorAnnotation(InitialValueAnnotationHandler.class)
public @interface InitialValue
{

	/**
	 * The expression that when executed provides the initial value for the
	 * property.
	 */
	String value();
}

