package org.tynamo.pages;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Log;
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

public class Edit {

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

	@Log
	protected Object onSuccess() {
		persitenceService.save(bean);
		return back();
	}

	public Link onActionFromCancel() {
		return back();
	}

	public String getListAllLinkMessage() {
		return messages
				.format(Utils.LISTALL_LINK_MESSAGE, DisplayNameUtils.getPluralDisplayName(classDescriptor, messages));
	}

	public boolean isAllowRemove() {
		return classDescriptor.isAllowRemove() && !classDescriptor.isChild();
	}

	public String getTitle() {
		return messages.format(Utils.EDIT_MESSAGE, DisplayNameUtils.getDisplayName(classDescriptor, messages));
	}

	public Link back() {
		return tynamoPageRenderLinkSource.createPageRenderLinkWithContext(PageType.SHOW, classDescriptor.getBeanType(),getBean());
	}

	public TynamoClassDescriptor getClassDescriptor() {
		return classDescriptor;
	}

	public Object getBean() {
		return bean;
	}
}
