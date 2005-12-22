package org.trails.validation;

import org.hibernate.HibernateException;
import org.hibernate.event.MergeEvent;
import org.hibernate.event.SaveOrUpdateEvent;
import org.hibernate.event.def.DefaultMergeEventListener;
import org.hibernate.event.def.DefaultSaveOrUpdateEventListener;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;

public class ValidateMergeEventListener extends
        DefaultMergeEventListener
{

    @Override
    public void onMerge(MergeEvent event)
            throws HibernateException
    {
    	HibernateClassValidatorFactory.getSingleton().validateEntity(event.getOriginal());
        super.onMerge(event);
    }

}
