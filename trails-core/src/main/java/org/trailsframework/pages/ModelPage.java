package org.trailsframework.pages;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PageLink;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.ContextValueEncoder;
import org.trailsframework.descriptor.IClassDescriptor;
import org.trailsframework.services.DescriptorService;
import org.trailsframework.services.PersistenceService;
import org.trailsframework.util.DisplayNameUtils;


public abstract class ModelPage
{

	@Inject
	private ContextValueEncoder contextValueEncoder;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private Messages messages;

	@Inject
	private PersistenceService persitenceService;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private ComponentResources resources;

	@Component
	private Form form;

	@Component
	private PageLink link;

	abstract void onValidateFormFromForm() throws ValidationException;

	abstract Object onSuccess();

	public Link onActionFromCancel()
	{
		return back();
	}

	public String getTitle()
	{
		return messages.format("org.trails.i18n.edit", DisplayNameUtils.getDisplayName(getClassDescriptor(), messages));
	}

	public String getListAllLinkMessage()
	{
		return messages.format("org.trails.component.listalllink", DisplayNameUtils.getPluralDisplayName(getClassDescriptor(), messages));
	}

	public ContextValueEncoder getContextValueEncoder()
	{
		return contextValueEncoder;
	}

	public BeanModelSource getBeanModelSource()
	{
		return beanModelSource;
	}

	public Messages getMessages()
	{
		return messages;
	}

	public DescriptorService getDescriptorService()
	{
		return descriptorService;
	}

	public PersistenceService getPersitenceService()
	{
		return persitenceService;
	}

	public ComponentResources getResources()
	{
		return resources;
	}

	public Form getForm()
	{
		return form;
	}

	public abstract IClassDescriptor getClassDescriptor();

	public abstract Object getBean();

	public abstract Link back();

}