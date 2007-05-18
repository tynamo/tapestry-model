package org.trails.page;

import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.annotations.Lifecycle;
import org.hibernate.validator.InvalidStateException;
import org.trails.hibernate.HasAssignedIdentifier;
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

	protected boolean isModelNew(Object model)
	{
		if (model instanceof HasAssignedIdentifier)
		{
			return !((HasAssignedIdentifier) getModel()).isSaved();
		} else
		{
			return super.isModelNew(model);
		}
	}
}
