package org.trails.validation;

import org.hibernate.HibernateException;
import org.hibernate.event.MergeEvent;
import org.hibernate.event.def.DefaultMergeEventListener;

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
