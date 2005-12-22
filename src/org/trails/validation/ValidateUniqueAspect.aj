/**
 * 
 */
package org.trails.validation;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.trails.persistence.*;
import org.trails.descriptor.*;

public aspect ValidateUniqueAspect
{
    private PersistenceService persistenceService;
    
    private DescriptorService descriptorService;
    
    public DescriptorService getDescriptorService()
    {
        return descriptorService;
    }

    public void setDescriptorService(DescriptorService descriptorService)
    {
        this.descriptorService = descriptorService;
    }

    public PersistenceService getPersistenceService()
    {
        return persistenceService;
    }

    public void setPersistenceService(PersistenceService persistenceService)
    {
        this.persistenceService = persistenceService;
    }

    pointcut saveWithUnique(ValidateUniqueness validateUniqueness, Object savee) : execution(* PersistenceService.save(..))
        && @args(validateUniqueness) && args(savee);
    
    before(ValidateUniqueness validateUniqueness, Object savee) : saveWithUnique(validateUniqueness, savee)
    {
        DetachedCriteria criteria = DetachedCriteria.forClass(savee.getClass());
        IClassDescriptor descriptor = getDescriptorService().getClassDescriptor(savee.getClass());
 
        try
        {
            String propertyName = validateUniqueness.property();
            Object value = PropertyUtils.getProperty(savee, propertyName);
            criteria.add(Restrictions.eq(propertyName, value));
            String idPropertyName = descriptor.getIdentifierDescriptor().getName();
            Object idValue = PropertyUtils.getProperty(savee, idPropertyName);
            if (idValue != null)
            {
                criteria.add(Restrictions.not(Restrictions.idEq(idValue)));
            }
            if (getPersistenceService().getInstances(criteria).size() > 0)
            {
                throw new UniquenessException(descriptor.getPropertyDescriptor(propertyName));
            }
        } catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
    }
    
    
}
