package org.trailsframework.hibernate.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.LibraryMapping;
import org.trailsframework.hibernate.validation.HibernateClassValidatorFactory;
import org.trailsframework.hibernate.validation.HibernateValidationDelegate;

public class TrailsHibernateModule {

	public static void bind(ServiceBinder binder) {
		// Make bind() calls on the binder object to define most IoC services.
		// Use service builder methods (example below) when the implementation
		// is provided inline, or requires more initialization than simply
		// invoking the constructor.

		binder.bind(HibernatePersistenceService.class, HibernatePersistenceServiceImpl.class);
		binder.bind(HibernateClassValidatorFactory.class, HibernateClassValidatorFactory.class);
		binder.bind(HibernateValidationDelegate.class, HibernateValidationDelegate.class);
		
	}

	/**
	 * Add our components and pages to the "trails" library.
	 */
	public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
		configuration.add(new LibraryMapping("trails-hibernate", "org.trailsframework.hibernate"));
	}
}
