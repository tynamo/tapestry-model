package org.tynamo.pages;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ContextValueEncoder;
import org.apache.tapestry5.services.HttpError;
import org.tynamo.PageType;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;
import org.tynamo.services.TynamoPageRenderLinkSource;
import org.tynamo.util.DisplayNameUtils;
import org.tynamo.util.Utils;

public class Show {

	@Inject
	private ContextValueEncoder contextValueEncoder;

	@Inject
	private Messages messages;

	@Inject
	private PersistenceService persitenceService;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private TynamoPageRenderLinkSource tynamoPageRenderLinkSource;

	private TynamoClassDescriptor classDescriptor;

	@Property(read = false)
	private Object bean;

	protected Object onActivate(Class clazz, String id) {

		if (clazz == null) return new HttpError(Utils.SC_NOT_FOUND, messages.get(Utils.SC_NOT_FOUND_MESSAGE));

		this.bean = contextValueEncoder.toValue(clazz, id);
		this.classDescriptor = descriptorService.getClassDescriptor(clazz);

		if (bean == null) return new HttpError(Utils.SC_NOT_FOUND, messages.get(Utils.SC_NOT_FOUND_MESSAGE));

		return null;
	}

	protected void cleanupRender() {
		bean = null;
		classDescriptor = null;
	}

	/**
	 * This tells Tapestry to put type & id into the URL, making it bookmarkable.
	 *
	 * @return
	 */
	protected Object[] onPassivate() {
		return new Object[]{classDescriptor.getBeanType(), bean};
	}

	public Object[] getEditPageContext() {
		return new Object[]{classDescriptor.getBeanType(), bean};
	}

	public String getEditLinkMessage() {
		return messages.format(Utils.EDIT_MESSAGE, DisplayNameUtils.getDisplayName(classDescriptor, messages));
	}

	public String getTitle() {
		return messages.format(Utils.SHOW_MESSAGE, bean.toString(), messages);
	}

//	@CommitAfter
	public Link onActionFromDelete() {
		persitenceService.remove(bean);
		return tynamoPageRenderLinkSource.createPageRenderLinkWithContext(PageType.LIST, classDescriptor.getBeanType());
	}

	public boolean isAllowRemove() {
		return classDescriptor.isAllowRemove() && !classDescriptor.isChild();
	}

	public String getListAllLinkMessage() {
		return messages
				.format(Utils.LISTALL_LINK_MESSAGE, DisplayNameUtils.getPluralDisplayName(classDescriptor, messages));
	}


	public TynamoClassDescriptor getClassDescriptor() {
		return classDescriptor;
	}

	public Object getBean() {
		return bean;
	}
}
