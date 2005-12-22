/*
 * Created on 29/11/2005
 *
 */
package org.trails.validation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.trails.component.Utils;
import org.trails.servlet.TrailsApplicationServlet;

public class HibernateClassValidatorFactory {

	private static Map classValidator = new HashMap();
	
	private static final HibernateClassValidatorFactory singleton = new HibernateClassValidatorFactory(); 
	
	private HibernateClassValidatorFactory() {
	}
	
	public static HibernateClassValidatorFactory getSingleton() {
		return singleton;
	}
	
	public void validateEntity(Object entity) {
		Locale locale = TrailsApplicationServlet.getCurrentLocale();

		String key = entity.getClass().toString() + "locale:" + locale;		
		ClassValidator validator = (ClassValidator) classValidator.get(key);
		if (validator == null) {
			validator = initializeCache(key, entity, locale);
		}
		
		InvalidValue[] invalidValues = validator.getInvalidValues(entity);
		if (invalidValues.length > 0) {
			throw new InvalidStateException(invalidValues);
		}
		
	}

	private ClassValidator initializeCache(String key, Object entity, Locale locale) {
		Class entityClass = Utils.checkForCGLIB(entity.getClass());
		ClassValidator validator;
		if (locale == null) {
			validator = new ClassValidator(entityClass);			
		} else {
			ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
			validator = new ClassValidator(entityClass, bundle);
		}
		
		classValidator.put(key, validator);
		return validator;
	}
	
	
}
