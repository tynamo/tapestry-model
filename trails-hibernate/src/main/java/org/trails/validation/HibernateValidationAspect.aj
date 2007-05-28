package org.trails.validation;

import org.trails.hibernate.*;

public aspect HibernateValidationAspect
{
	before(Object savee) :
		(execution(* HibernatePersistenceServiceImpl.save(..)) && args(savee))
		|| (execution(* HibernatePersistenceServiceImpl.merge(..)) && args(savee))
	{
		HibernateClassValidatorFactory.getSingleton().validateEntity(savee);
	}
}
