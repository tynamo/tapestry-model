/*
 * Created on 29/11/2005
 *
 */
package org.trails.validation;

import java.util.Enumeration;
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

	/**
	 * This inner class doesn't return exceptions when some key is searched in
	 * the bundle. This is nice so we don't have exceptions thrown in the screen
	 * by hibernate ClassValidator. Instead, we will have a
	 * [TRAILS][KEY-IN-UPPER] string returned.
	 * 
	 * @author Eduardo Fernandes Piva (eduardo@gwe.com.br)
	 *
	 */
	private class MyBundle extends ResourceBundle {

		private ResourceBundle parentBundle;
		
		public MyBundle(ResourceBundle bundle) {
			this.parentBundle = bundle;
		}
		
		@Override
		protected Object handleGetObject(String key) {
			try {
				return parentBundle.getObject(key);				
			} catch (Exception e) {
				return "[TRAILS][" + key.toUpperCase() + "]";
			}
		}

		@Override
		public Enumeration<String> getKeys() {
			return parentBundle.getKeys();
		}
		
	}
	
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
			ResourceBundle myBundle = new MyBundle(bundle);
			validator = new ClassValidator(entityClass, myBundle);
		}
		
		classValidator.put(key, validator);
		return validator;
	}
	
	
}
