/*
 * Copyright 2004 Chris Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.trails.page;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.annotations.Lifecycle;
import org.apache.tapestry.callback.ICallback;
import org.apache.tapestry.engine.ILink;
import org.trails.callback.UrlCallback;
import org.trails.component.Utils;
import org.trails.engine.TrailsPagesServiceParameter;
import org.trails.persistence.PersistenceException;
import org.trails.validation.TrailsValidationDelegate;

/**
 * This page will edit an instance contained in the model property
 *
 * @author Chris Nelson
 */
public abstract class EditPage extends ModelPage implements IAssociationPage
{

	private static final Log LOG = LogFactory.getLog(EditPage.class);

	@Bean(lifecycle = Lifecycle.REQUEST)
	public abstract TrailsValidationDelegate getDelegate();

	public abstract ICallback getNextPage();

	public abstract void setNextPage(ICallback NextPage);

	public ILink save(IRequestCycle cycle)
	{
		if (save())
		{
			if (getCallbackStack() != null && !getCallbackStack().isEmpty())
			{
				// When saving objects with assigned IDs, we need to removed the last element of the stack.
				getCallbackStack().pop();
			}
			return linkToThisPage();
		}
		return null;
	}

	private ILink defaultCallback()
	{
		return getTrailsPagesService().getLink(false, new TrailsPagesServiceParameter(PageType.LIST, getClassDescriptor()));
	}

	private ILink linkToThisPage()
	{
		return getTrailsPagesService()
				.getLink(false, new TrailsPagesServiceParameter(PageType.EDIT, getClassDescriptor(), getModel(), getAssociationDescriptor(), getParent()));
	}

	protected ICallback callbackToThisPage()
	{
		return new UrlCallback(linkToThisPage().getURL());
	}

	protected boolean save()
	{
		if (!getDelegate().getHasErrors())
		{
			// We get hibernate errors when the cascade tries to
			// put the same object on the session twice
			// if (!cameFromChildCollection())
			// {
			try
			{
				setModel(getPersistenceService().save(getModel()));
			} catch (PersistenceException pe)
			{
				getDelegate().record(pe);
				return false;
			}
			// }
			return true;
		}
		return false;
	}

	public ILink cancel(IRequestCycle cycle)
	{
		return goBack(cycle);
	}

	public ILink goBack(IRequestCycle cycle)
	{

		if (getCallbackStack() != null)
		{
			ICallback callback = getCallbackStack().popPreviousCallback();
			if (callback != null)
			{
				callback.performCallback(cycle);
			}
		}
		return defaultCallback();
	}

	/**
	 * @param cycle
	 */
	public ILink saveAndReturn(IRequestCycle cycle)
	{
		if (save())
		{
			if (cameFromCollection())
			{
				Utils.executeOgnlExpression(getAssociationDescriptor().findAddExpression(), getModel(), getParent());
				getPersistenceService().save(getParent());
			}
			return goBack(cycle);
		}
		return null;
	}

	public ILink remove(IRequestCycle cycle)
	{

		try
		{
			if (cameFromCollection())
			{
				Utils.executeOgnlExpression(getAssociationDescriptor().findRemoveExpression(), getModel(), getParent());
				getPersistenceService().save(getParent());
			}

			getPersistenceService().remove(getModel());

		} catch (PersistenceException pe)
		{
			getDelegate().record(pe);
			return null;

		} catch (Exception e)
		{
			getDelegate().record(e);
			return null;
		}

		return goBack(cycle);
	}

	/**
	 * @return
	 */
	public String getTitle()
	{
		Object[] params = new Object[]{getClassDescriptor().getDisplayName()};
		if (cameFromCollection() && isModelNew())
		{
			return getResourceBundleMessageSource()
					.getMessageWithDefaultValue("org.trails.i18n.add", params, "[TRAILS][ORG.TRAILS.I18N.ADD]");
		} else
		{
			return getResourceBundleMessageSource()
					.getMessageWithDefaultValue("org.trails.i18n.edit", params, "[TRAILS][ORG.TRAILS.I18N.EDIT]");
		}
	}

	public boolean cameFromCollection()
	{
		return getParent() != null;
	}

}
