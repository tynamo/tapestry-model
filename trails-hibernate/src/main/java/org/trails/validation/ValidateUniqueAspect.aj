/**
 *
 */
package org.trails.validation;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.persistence.HibernatePersistenceService;

public aspect ValidateUniqueAspect
{
    private HibernatePersistenceService persistenceService;

    private DescriptorService descriptorService;

    public DescriptorService getDescriptorService()
    {
        return descriptorService;
    }

    public void setDescriptorService(DescriptorService descriptorService)
    {
        this.descriptorService = descriptorService;
    }

    public HibernatePersistenceService getPersistenceService()
    {
        return persistenceService;
    }

    public void setPersistenceService(HibernatePersistenceService persistenceService)
    {
        this.persistenceService = persistenceService;
    }

    pointcut saveWithUnique(ValidateUniqueness validateUniqueness, Object savee) :
    	(execution(* HibernatePersistenceService.save(..)) && @args(validateUniqueness) && args(savee))
    	|| (execution(* HibernatePersistenceService.merge(..)) && @args(validateUniqueness) && args(savee));

    before(ValidateUniqueness validateUniqueness, Object savee) : saveWithUnique(validateUniqueness, savee)
    {
        DetachedCriteria criteria = DetachedCriteria.forClass(savee.getClass());
        IClassDescriptor descriptor = getDescriptorService().getClassDescriptor(savee.getClass());

		if (descriptor == null)
			throw new RuntimeException("Class " + savee.getClass() + " isn't mapped in Trails.");

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
            if (getPersistenceService().getInstances(savee.getClass(), criteria).size() > 0)
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
