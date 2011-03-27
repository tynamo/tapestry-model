package org.tynamo.pages;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.services.HttpError;
import org.tynamo.TynamoGridDataSource;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;
import org.tynamo.util.DisplayNameUtils;
import org.tynamo.util.Utils;

public class List {

	@Inject
	private PersistenceService persitenceService;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private Messages messages;

	@Inject
	private PropertyAccess propertyAccess;

	@Property
	private Object bean;

	@Property
	private TynamoClassDescriptor classDescriptor;

	protected Object onActivate(Class clazz) {

		if (clazz == null) return new HttpError(Utils.SC_NOT_FOUND, messages.get(Utils.SC_NOT_FOUND_MESSAGE));
		classDescriptor = descriptorService.getClassDescriptor(clazz);

		return null;
	}

	protected Object[] onPassivate() {
		return new Object[]{classDescriptor.getBeanType()};
	}

	public Object getSource() {
		return new TynamoGridDataSource(persitenceService, classDescriptor.getBeanType());
	}

	public Object[] getEditPageContext() {
		return new Object[]{classDescriptor.getBeanType(), bean};
	}

	public String getTitle() {
		return messages.format(Utils.LIST_MESSAGE, DisplayNameUtils.getPluralDisplayName(classDescriptor, messages));
	}

	public String getNewLinkMessage() {
		return messages.format(Utils.NEW_MESSAGE, DisplayNameUtils.getDisplayName(classDescriptor, messages));
	}

	public final String getModelId() {
		return propertyAccess.get(bean, classDescriptor.getIdentifierDescriptor().getName()).toString();
	}

}
