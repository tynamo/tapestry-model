package org.tynamo.descriptor.annotation.extension;

import org.tynamo.descriptor.annotation.handlers.HandledBy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to declare filter options from a select based on
 * other property.
 * <p/>
 * The most common example of this is selecting a State based on the value of
 * the country property. If the property you want to filter by is named
 * "countryFilter" and the Country class has a property named "states" you
 * should annotate the state property in this way:<br>
 *
 * @author pruggia
 * @PossibleValues("countryFilter.states")<br> <br>
 * Don't forget to add a {@link InitialValue} to the filtering
 * property.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@HandledBy("PossibleValuesAnnotationHandler")
public @interface PossibleValues
{

	/**
	 * The expression that when executed provides the options to select values
	 * from.
	 */
	String value();
}

