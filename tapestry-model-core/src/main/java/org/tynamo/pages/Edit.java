package org.tynamo.pages;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.ContextValueEncoder;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;
import org.tynamo.util.DisplayNameUtils;
import org.tynamo.util.Utils;

public class Edit {

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

	private TynamoClassDescriptor classDescriptor;

	@Property
	private BeanModel beanModel;

	@Property(read = false)
	private Object bean;

	protected void onActivate(Class clazz, String id) {

		assert clazz != null; //@todo throw a proper exception

		this.bean = contextValueEncoder.toValue(clazz, id);
		this.classDescriptor = descriptorService.getClassDescriptor(clazz);
		this.beanModel = beanModelSource.createEditModel(clazz, messages);

		assert bean != null; //@todo throw a proper exception
	}

	protected void cleanupRender() {
		bean = null;
		classDescriptor = null;
		beanModel = null;
	}

	/**
	 * This tells Tapestry to put type & id into the URL, making it bookmarkable.
	 *
	 * @return
	 */
	protected Object[] onPassivate() {
		return new Object[]{classDescriptor.getType(), bean};
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
		return pageRenderLinkSource.createPageRenderLinkWithContext("Show", classDescriptor.getType(), getBean());
	}

	public TynamoClassDescriptor getClassDescriptor() {
		return classDescriptor;
	}

	public Object getBean() {
		return bean;
	}
}
