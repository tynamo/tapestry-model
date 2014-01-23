package org.tynamo.examples.simple.pages;


import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.tynamo.descriptor.annotation.beaneditor.BeanModel;
import org.tynamo.descriptor.annotation.beaneditor.BeanModels;
import org.tynamo.examples.simple.CustomCommitAfter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ContextValueEncoder;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.tynamo.examples.simple.entities.Product;
import org.tynamo.util.TynamoMessages;
import org.tynamo.routing.annotations.At;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;
import org.tynamo.util.Utils;

/**
 * Page for displayig the properties of an object.
 *
 * @note:
 * When extending this page for customization purposes, it's better to copy & paste code than trying to use inheritance.
 *
 */
@BeanModels({
		@BeanModel(beanType = Product.class, exclude = "id")
})
@At(value = "/{0}/{1}", order = "after:Add")
public class Show
{

	@Inject
	private ContextValueEncoder contextValueEncoder;

	@Inject
	private Messages messages;

	@Inject
	private PersistenceService persistenceService;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private PageRenderLinkSource pageRenderLinkSource;

	@Property(write = false)
	private Class beanType;

	@Property
	private Object bean;

	@OnEvent(EventConstants.ACTIVATE)
	Object activate(Class clazz, String id)
	{

		if (clazz == null) return Utils.new404(messages);

		this.bean = contextValueEncoder.toValue(clazz, id);
		this.beanType = clazz;

		if (bean == null) return Utils.new404(messages);

		return null;
	}

	@CleanupRender
	void cleanup()
	{
		bean = null;
		beanType = null;
	}

	/**
	 * This tells Tapestry to put type & id into the URL, making it bookmarkable.
	 */
	@OnEvent(EventConstants.PASSIVATE)
	Object[] passivate()
	{
		return new Object[]{beanType, bean};
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
		persistenceService.remove(bean);
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
