package org.tynamo.examples.simple.pages.collections;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.tynamo.examples.simple.CustomCommitAfter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.ContextValueEncoder;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.tynamo.builder.BuilderDirector;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.examples.simple.entities.Catalog;
import org.tynamo.examples.simple.entities.Category;
import org.tynamo.routing.annotations.At;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;
import org.tynamo.util.TynamoMessages;
import org.tynamo.util.Utils;

@At(value = "/catalog/{0}/categories/new", order = "before:collections/AddC")
public class CatalogCategoriesAddC
{

	private static final Class<Catalog> parentType = Catalog.class;
	private static final String property = "categories";

	@Inject
	private BuilderDirector builderDirector;

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
	private PageRenderLinkSource pageRenderLinkSource;


	@Property(write = false)
	private CollectionDescriptor collectionDescriptor;

	@Property
	private Category bean;

	@Property(write = false)
	private Catalog parentBean;

	@Property(write = false)
	private TynamoClassDescriptor classDescriptor;

	@Property
	private BeanModel beanModel;

	@OnEvent(EventConstants.ACTIVATE)
	Object activate(String parentId)
	{
		TynamoPropertyDescriptor propertyDescriptor = descriptorService.getClassDescriptor(parentType).getPropertyDescriptor(property);

		if (propertyDescriptor != null)
		{
			this.collectionDescriptor = ((CollectionDescriptor) propertyDescriptor);

			this.classDescriptor = descriptorService.getClassDescriptor(collectionDescriptor.getElementType());
			this.bean = builderDirector.createNewInstance(Category.class);
			this.beanModel = beanModelSource.createEditModel(Category.class, messages);

			this.parentBean = contextValueEncoder.toValue(parentType, parentId);

			if (parentBean != null) return null; // I know this is counterintuitive
		}

		return Utils.new404(messages);
	}

	/**
	 * This tells Tapestry to put type & id into the URL, making it bookmarkable.
	 */
	@OnEvent(EventConstants.PASSIVATE)
	Object[] passivate()
	{
		return new Object[]{parentBean};
	}


	@Log
	@CustomCommitAfter
	@OnEvent(EventConstants.SUCCESS)
	Link success()
	{
		persitenceService.addToCollection(collectionDescriptor, bean, parentBean);
		return back();
	}

	@CleanupRender
	void cleanup()
	{
		bean = null;
		classDescriptor = null;
		beanModel = null;
		parentBean = null;
		collectionDescriptor = null;
	}

	@OnEvent("cancel")
	Link back()
	{
		return pageRenderLinkSource.createPageRenderLinkWithContext(ListC.class, parentType, parentBean, collectionDescriptor.getName());
	}

	public String getTitle()
	{
		return TynamoMessages.add(messages, collectionDescriptor.getElementType());
	}

	public String getListAllLinkMessage()
	{
		return TynamoMessages.listAll(messages, classDescriptor.getBeanType());
	}

}