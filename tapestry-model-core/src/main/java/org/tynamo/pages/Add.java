package org.tynamo.pages;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.HttpError;
import org.tynamo.PageType;
import org.tynamo.builder.BuilderDirector;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;
import org.tynamo.services.TynamoPageRenderLinkSource;
import org.tynamo.util.DisplayNameUtils;
import org.tynamo.util.Utils;

public class Add {

	@Inject
	private BuilderDirector builderDirector;

	@Inject
	private Messages messages;

	@Inject
	private PersistenceService persitenceService;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private TynamoPageRenderLinkSource tynamoPageRenderLinkSource;

	@Property
	private TynamoClassDescriptor classDescriptor;

	@Property
	private Object bean;


	protected Object onActivate(Class clazz) {

		if (clazz == null) return new HttpError(Utils.SC_NOT_FOUND, messages.get(Utils.SC_NOT_FOUND_MESSAGE));

		this.bean = builderDirector.createNewInstance(clazz);
		this.classDescriptor = descriptorService.getClassDescriptor(clazz);

		return null;
	}

	protected void cleanupRender() {
		bean = null;
		classDescriptor = null;
	}

	protected Object[] onPassivate() {
		return new Object[]{classDescriptor.getBeanType()};
	}

//	@CommitAfter
	@Log
	protected Object onSuccess() {
		persitenceService.save(bean);
		return tynamoPageRenderLinkSource.createPageRenderLinkWithContext(PageType.SHOW, classDescriptor.getBeanType(),bean);
	}

	public Link onActionFromCancel() {
		return tynamoPageRenderLinkSource.createPageRenderLinkWithContext(PageType.LIST, classDescriptor.getBeanType());
	}

	public String getTitle() {
		return messages.format(Utils.ADD_NEW_MESSAGE, DisplayNameUtils.getDisplayName(classDescriptor, messages));
	}

	public String getListAllLinkMessage() {
		return messages
				.format(Utils.LISTALL_LINK_MESSAGE, DisplayNameUtils.getPluralDisplayName(classDescriptor, messages));
	}

}
