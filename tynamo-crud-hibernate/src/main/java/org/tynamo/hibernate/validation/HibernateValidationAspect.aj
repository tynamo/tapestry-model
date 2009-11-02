package org.trails.validation;

import org.trails.hibernate.*;

public aspect HibernateValidationAspect
{
	HibernateClassValidatorFactory hibernateClassValidatorFactory;

	before(Object savee) :
		(execution(* HibernatePersistenceServiceImpl.save(..)) && args(savee))
		|| (execution(* HibernatePersistenceServiceImpl.merge(..)) && args(savee))
	{
		hibernateClassValidatorFactory.validateEntity(savee);
	}

	public void setHibernateClassValidatorFactory(HibernateClassValidatorFactory hibernateClassValidatorFactory) {
		this.hibernateClassValidatorFactory = hibernateClassValidatorFactory;
	}
}
