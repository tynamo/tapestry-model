package org.tynamo.pages;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.PageLink;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.ContextValueEncoder;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;
import org.tynamo.util.DisplayNameUtils;


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
	private PageLink link;

	public Link onActionFromCancel()
	{
		return back();
	}

	public String getListAllLinkMessage()
	{
		return messages.format("org.tynamo.component.listalllink", DisplayNameUtils.getPluralDisplayName(getClassDescriptor(), messages));
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

	public abstract String getTitle();

	public abstract TynamoClassDescriptor getClassDescriptor();

	public abstract Object getBean();

	public abstract Link back();

	protected abstract BeanModel createBeanModel(Class clazz);

}
