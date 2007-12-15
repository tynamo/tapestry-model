package org.trails.validation;

import java.util.Iterator;
import java.util.List;

import ognl.Ognl;
import ognl.OgnlException;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.trails.exception.TrailsRuntimeException;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.persistence.HibernatePersistenceService;

public aspect CheckForOrphansAspect
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

    pointcut removeWithAssertNoOrphans(AssertNoOrphans assertNoOrphans, Object removee) : execution(* HibernatePersistenceService.remove(..))
    && @args(assertNoOrphans) && args(removee);

    before(AssertNoOrphans assertNoOrphans, Object removee) : removeWithAssertNoOrphans(assertNoOrphans, removee)
    {
    	Class orphanClass = assertNoOrphans.value();
    	if (!orphanClass.equals(Object.class))
    	{
    		checkForOrphansUsingClass(assertNoOrphans, removee);
    	}
    	else
    	{
    		checkForOrphansUsingProperty(assertNoOrphans, removee);
    	}
    }

    private void checkForOrphansUsingProperty(AssertNoOrphans assertNoOrphans, Object removee)
    {
    	try
    	{
    		Integer size = (Integer)Ognl.getValue(assertNoOrphans.childrenProperty() + ".size()", removee);
    		if (size > 0)
    		{
    			throw new OrphanException(buildMessage(assertNoOrphans, "Cannot remove."));
    		}
    	}
    	catch (OgnlException oe)
    	{
    		throw new TrailsRuntimeException(oe);
    	}
    }

    private String buildMessage(AssertNoOrphans assertNoOrphans, String defaultMessage)
    {
    	if (assertNoOrphans.message().length() == 0)
    	{
    		return defaultMessage;
    	}
    	else
    	{
    		return assertNoOrphans.message();
    	}
    }

	private void checkForOrphansUsingClass(AssertNoOrphans assertNoOrphans, Object removee)
	{
		Class orphanClass = assertNoOrphans.value();
		IClassDescriptor orphanDescriptor = getDescriptorService().getClassDescriptor(orphanClass);
    	IClassDescriptor removeeDescriptor = getDescriptorService().getClassDescriptor(removee.getClass());
    	for (Iterator iter = orphanDescriptor.getPropertyDescriptors().iterator(); iter.hasNext();)
		{
			IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) iter.next();
			if (propertyDescriptor.getPropertyType().equals(removee.getClass()))
			{
				DetachedCriteria criteria = DetachedCriteria.forClass(orphanClass);
				criteria.add(Restrictions.eq(propertyDescriptor.getName(), removee));
				List instances = getPersistenceService().getInstances(orphanClass, criteria);
				if (instances.size() > 0)
				{
					String defaultMessage = "This " + removeeDescriptor.getDisplayName() +
						" cannot be removed because there is a " + orphanDescriptor.getDisplayName() +
						" that refers to it.";
					throw new OrphanException(buildMessage(assertNoOrphans, defaultMessage));
				}
			}
		}
	}
}
