package org.trails.validation;

import org.trails.hibernate.*;

public aspect HibernateValidationAspect
{
    before(Object savee) : execution(* HibernatePersistenceService.save(..)) && args(savee)
    {
        HibernateClassValidatorFactory.getSingleton().validateEntity(savee);
    }
}
