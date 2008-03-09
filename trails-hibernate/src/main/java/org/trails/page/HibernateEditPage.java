package org.trails.page;

import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.annotations.Lifecycle;
import org.apache.tapestry.annotations.InjectObject;
import org.hibernate.validator.InvalidStateException;
import org.trails.persistence.HibernatePersistenceService;
import org.trails.persistence.PersistenceException;
import org.trails.validation.HibernateValidationDelegate;

public abstract class HibernateEditPage extends EditPage
{

	@Bean(lifecycle = Lifecycle.REQUEST)
	public abstract HibernateValidationDelegate getHibernateValidationDelegate();

	/**
	 * @todo: remove when the components reuse issue goes away.
	 */
	public HibernateValidationDelegate getDelegate()
	{
		return getHibernateValidationDelegate();
	}

	@InjectObject("service:trails.hibernate.PersistenceService")
	public abstract HibernatePersistenceService getHibernatePersistenceService();

	/**
	 * @todo: remove when the components reuse issue goes away.
	 */
	public HibernatePersistenceService getPersistenceService()
	{
		return getHibernatePersistenceService();
	}

	@Override
	protected boolean save()
	{
		if (!getDelegate().getHasErrors())
		{
			try
			{
				setModel(getPersistenceService().save(getModel()));
			} catch (PersistenceException pe)
			{
				getDelegate().record(pe);
				return false;
			} catch (InvalidStateException ivex)
			{
				getDelegate().record(getClassDescriptor(), ivex);
				return false;
			}
			return true;
		}
		return false;
	}
}
