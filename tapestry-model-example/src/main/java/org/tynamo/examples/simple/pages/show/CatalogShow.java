package org.tynamo.examples.simple.pages.show;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.tynamo.examples.simple.CustomCommitAfter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ContextValueEncoder;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.examples.simple.entities.Catalog;
import org.tynamo.examples.simple.pages.List;
import org.tynamo.routing.annotations.At;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;
import org.tynamo.util.TynamoMessages;
import org.tynamo.util.Utils;

@At(value = "/catalog/{0}", order = "before:Show")
public class CatalogShow
{

	@Inject
	private ContextValueEncoder contextValueEncoder;

	@Inject
	private Messages messages;

	@Inject
	private PersistenceService persitenceService;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private PageRenderLinkSource pageRenderLinkSource;

	@Property(write = false)
	private Class<Catalog> beanType = Catalog.class;

	@Property
	private Catalog bean;

	@OnEvent(EventConstants.ACTIVATE)
	Object activate(Catalog catalog)
	{
		this.bean = catalog;
//		this.bean = contextValueEncoder.toValue(clazz, id);
		if (bean == null) return Utils.new404(messages);
		return null;
	}

	public CollectionDescriptor getCategoriesDescriptor()
	{
		return (CollectionDescriptor) descriptorService.getClassDescriptor(beanType).getPropertyDescriptor("categories");
	}

	@CleanupRender
	void cleanup()
	{
		bean = null;
	}

	/**
	 * This tells Tapestry to put type & id into the URL, making it bookmarkable.
	 */
	@OnEvent(EventConstants.PASSIVATE)
	Object[] passivate()
	{
		return new Object[]{bean};
	}

	public Object[] getEditPageContext()
	{
		return new Object[]{beanType, bean};
	}

	public String getEditLinkMessage()
	{
		return TynamoMessages.edit(messages, beanType);
	}

	public String getTitle()
	{
		return TynamoMessages.show(messages, bean.toString());
	}

	@CustomCommitAfter
	@OnEvent("delete")
	Link delete()
	{
		persitenceService.remove(bean);
		return pageRenderLinkSource.createPageRenderLinkWithContext(List.class, beanType);
	}

	public boolean isDeleteAllowed()
	{
		return descriptorService.getClassDescriptor(beanType).isAllowRemove();
	}

	public String getListAllLinkMessage()
	{
		return TynamoMessages.listAll(messages, beanType);
	}

}
