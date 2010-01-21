package org.tynamo.model.jpa.pages;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.services.BeanModelSource;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;
import org.tynamo.util.BeanModelUtils;
import org.tynamo.util.DisplayNameUtils;
import org.tynamo.util.Utils;

public class JPAListPage
{

	@Inject
	private PersistenceService persitenceService;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private Messages messages;

	@Inject
	private PropertyAccess propertyAccess;

	@Inject
	private BeanModelSource beanModelSource;

	@Property(read = false)
	private Object bean;

	private TynamoClassDescriptor classDescriptor;

	private BeanModel beanModel;

	protected void onActivate(Class clazz) throws Exception
	{
		classDescriptor = descriptorService.getClassDescriptor(clazz);
		beanModel = beanModelSource.createDisplayModel(clazz, messages);
		BeanModelUtils.exclude(beanModel, classDescriptor);
	}

	protected Object[] onPassivate()
	{
		return new Object[]{classDescriptor.getType()};
	}

	public java.util.List getInstances()
	{
		return persitenceService.getInstances(classDescriptor.getType());
	}

	public Object[] getEditPageContext()
	{
		return new Object[]{classDescriptor.getType(), bean};
	}

	public String getTitle()
	{
		return messages.format(Utils.LIST_MESSAGE, DisplayNameUtils.getPluralDisplayName(classDescriptor, messages));
	}

	public String getNewLinkMessage()
	{
		return messages.format(Utils.NEW_MESSAGE, DisplayNameUtils.getDisplayName(classDescriptor, messages));
	}

	public final String getModelId()
	{
		return propertyAccess.get(bean, classDescriptor.getIdentifierDescriptor().getName()).toString();
	}


	public final TynamoClassDescriptor getClassDescriptor()
	{
		return classDescriptor;
	}

	public final BeanModel getBeanModel()
	{
		return beanModel;
	}

	public final Object getBean()
	{
		return bean;
	}
}
