package org.tynamo.pages;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.ContextValueEncoder;
import org.apache.tapestry5.services.HttpError;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;
import org.tynamo.services.TynamoPageRenderLinkSource;
import org.tynamo.util.DisplayNameUtils;
import org.tynamo.util.Utils;

/**
 * Edit Composition Page
 */
public class EditC {

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
	private TynamoPageRenderLinkSource tynamoPageRenderLinkSource;

	@Property(write = false)
	private CollectionDescriptor collectionDescriptor;

	@Property
	private Object bean;

	@Property(write = false)
	private Object parentBean;

	@Property(write = false)
	private TynamoClassDescriptor classDescriptor;

	@Property
	private BeanModel beanModel;


	protected Object onActivate(Class clazz, String parentId, String property, String id) {

		if (clazz != null)
		{
			TynamoPropertyDescriptor propertyDescriptor = descriptorService.getClassDescriptor(clazz).getPropertyDescriptor(property);

			if (propertyDescriptor != null)
			{
				this.collectionDescriptor = ((CollectionDescriptor) propertyDescriptor);

				this.classDescriptor = descriptorService.getClassDescriptor(collectionDescriptor.getElementType());
				this.beanModel = beanModelSource.createEditModel(classDescriptor.getBeanType(), messages);

				this.parentBean = contextValueEncoder.toValue(clazz, parentId);

				if (parentBean != null)
				{
					this.bean = contextValueEncoder.toValue(classDescriptor.getBeanType(), id);
					if (bean != null) return null; // I know this is counterintuitive
				}
			}
		}

		return new HttpError(Utils.SC_NOT_FOUND, messages.get(Utils.SC_NOT_FOUND_MESSAGE));

	}

	/**
	 * This tells Tapestry to put type & id into the URL, making it bookmarkable.
	 *
	 * @return
	 */
	protected Object[] onPassivate() {
		return new Object[]{collectionDescriptor.getBeanType(), parentBean, collectionDescriptor.getName(), bean};
	}

	protected void cleanupRender() {
		bean = null;
		classDescriptor = null;
		beanModel = null;
		parentBean = null;
		collectionDescriptor = null;
	}

	@Log
	protected Object onSuccess() {
		persitenceService.save(bean);
		return back();
	}

	public Link onActionFromCancel() {
		return back();
	}

	public String getTitle() {
		return messages.format(Utils.EDIT_MESSAGE, DisplayNameUtils.getDisplayName(classDescriptor, messages));
	}

	public Link back() {
		return tynamoPageRenderLinkSource.createPageRenderLinkWithContext(PageType.EDIT,
		                                                                  collectionDescriptor.getBeanType(),
		                                                                  parentBean);
	}

	public String getListAllLinkMessage() {
		return messages.format(Utils.LISTALL_LINK_MESSAGE, DisplayNameUtils.getPluralDisplayName(classDescriptor, messages));
	}

}
