package org.tynamo.pages;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.HttpError;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.tynamo.builder.BuilderDirector;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;
import org.tynamo.util.DisplayNameUtils;
import org.tynamo.util.Utils;

public class Add {

	@Inject
	private BuilderDirector builderDirector;

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

	@Property
	private TynamoClassDescriptor classDescriptor;

	@Property
	private BeanModel beanModel;

	@Property
	private Object bean;


	protected Object onActivate(Class clazz) {

		if (clazz == null) return new HttpError(Utils.SC_NOT_FOUND, messages.get(Utils.SC_NOT_FOUND_MESSAGE));

		this.bean = builderDirector.createNewInstance(clazz);
		this.classDescriptor = descriptorService.getClassDescriptor(clazz);
		this.beanModel = beanModelSource.createEditModel(clazz, messages);

		return null;
	}

	protected void cleanupRender() {
		bean = null;
		classDescriptor = null;
		beanModel = null;
	}

	protected Object[] onPassivate() {
		return new Object[]{classDescriptor.getBeanType()};
	}

	@Log
	protected Object onSuccess() {
		persitenceService.save(bean);
		return pageRenderLinkSource.createPageRenderLinkWithContext("Show", classDescriptor.getBeanType(), bean);
	}

	public Link onActionFromCancel() {
		return pageRenderLinkSource.createPageRenderLinkWithContext("List", classDescriptor.getBeanType());
	}

	public String getTitle() {
		return messages.format(Utils.ADD_NEW_MESSAGE, DisplayNameUtils.getDisplayName(classDescriptor, messages));
	}

	public String getListAllLinkMessage() {
		return messages
				.format(Utils.LISTALL_LINK_MESSAGE, DisplayNameUtils.getPluralDisplayName(classDescriptor, messages));
	}

}
