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
import java.util.List;
import org.trails.component.Utils;

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
            String idPropertyName = descriptor.getIdentifierDescriptor().getName();
            Object idValue = PropertyUtils.getProperty(savee, idPropertyName);
            
            // Note that neither this or the previous Criteria-based implementation works
            // if the unique property is an entity. You could implement this as well similarly
            // as is done evaluating <Operation>RequiresAssociation with an entity
            // See SecurePersistenceServiceImpl for example
            String queryString = "select count(*) from " + Utils.checkForCGLIB(savee.getClass()).getName() 
            + " where " + propertyName + " = ?"; 
            
            List result;
            if (idValue != null) {
            	queryString += " and " + idPropertyName + " != ?";
            	result = getPersistenceService().find(queryString, new Object[]{value, idValue});
            }
            else result = getPersistenceService().find(queryString, value);
            
            if ((Long)result.get(0) > 0)
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
