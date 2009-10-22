package org.tynamo.examples.recipe.pages;


import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ContextValueEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.hibernate.pages.HibernateEditPage;
import org.tynamo.util.DisplayNameUtils;

public class EditCompositionPage extends HibernateEditPage
{

	private static final Logger LOGGER = LoggerFactory.getLogger(EditCompositionPage.class);

	@Inject
	private ContextValueEncoder contextValueEncoder;

	@Property(write = false)
	private CollectionDescriptor collectionDescriptor;

	@Property(write = false)
	private Object parentBean;

	final void onActivate(Class clazz, String parentId, String property, String id) throws Exception
	{
		TynamoPropertyDescriptor propertyDescriptor =
				getDescriptorService().getClassDescriptor(clazz).getPropertyDescriptor(property);
		collectionDescriptor = ((CollectionDescriptor) propertyDescriptor);

		TynamoClassDescriptor classDescriptor =
				getDescriptorService().getClassDescriptor(collectionDescriptor.getElementType());
		Object bean = contextValueEncoder.toValue(classDescriptor.getType(), id);
		BeanModel beanModel = createBeanModel(classDescriptor.getType());

		activate(bean, classDescriptor, beanModel);
		parentBean = contextValueEncoder.toValue(clazz, parentId);
	}

	/**
	 * This tells Tapestry to put type & id into the URL, making it bookmarkable.
	 *
	 * @return
	 */
	protected Object[] onPassivate()
	{
		return new Object[]{collectionDescriptor.getBeanType(), parentBean, collectionDescriptor.getName(), getBean()};
	}

	protected void cleanupRender()
	{
		super.cleanupRender();
		parentBean = null;
		collectionDescriptor = null;
	}

	public String getTitle()
	{
		return getMessages()
				.format("org.tynamo.i18n.edit", DisplayNameUtils.getDisplayName(getClassDescriptor(), getMessages()));
	}


	public Link back()
	{
		return getPageRenderLinkSource().createPageRenderLinkWithContext(Edit.class, false, collectionDescriptor.getBeanType(), parentBean);
	}

}