package org.tynamo.internal.services;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.internal.beaneditor.BeanModelUtils;
import org.apache.tapestry5.internal.services.BeanModelSourceImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.ioc.services.ClassFactory;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.*;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.extension.BeanModelExtension;
import org.tynamo.internal.InternalConstants;
import org.tynamo.services.DescriptorService;

public class TynamoBeanModelSourceImpl extends BeanModelSourceImpl implements BeanModelSource
{

	DescriptorService descriptorService;
	RequestGlobals requestGlobals;

	public TynamoBeanModelSourceImpl(TypeCoercer typeCoercer,
	                                 PropertyAccess propertyAccess,
	                                 PropertyConduitSource propertyConduitSource,
	                                 @ComponentLayer ClassFactory classFactory,
	                                 @Primary DataTypeAnalyzer dataTypeAnalyzer,
	                                 ObjectLocator locator,
	                                 DescriptorService descriptorService, RequestGlobals requestGlobals)
	{
		super(typeCoercer, propertyAccess, propertyConduitSource, classFactory, dataTypeAnalyzer, locator);
		this.descriptorService = descriptorService;
		this.requestGlobals = requestGlobals;
	}

	public <T> BeanModel<T> create(Class<T> beanClass, boolean filterReadOnlyProperties, Messages messages)
	{
		BeanModel<T> dataModel = super.create(beanClass, filterReadOnlyProperties, messages);

		TynamoClassDescriptor classDescriptor = descriptorService.getClassDescriptor(beanClass);

		if (classDescriptor != null)
		{
			String activePageName = requestGlobals.getActivePageName().toLowerCase();
			int index = activePageName.indexOf(beanClass.getSimpleName().toLowerCase());
			String contextKey = index > 0 ? activePageName.substring(0, index) : activePageName;

			if (classDescriptor.supportsExtension(BeanModelExtension.class))
			{
				BeanModelExtension extension = classDescriptor.getExtension(BeanModelExtension.class);
				BeanModelUtils.modify(dataModel, null,
						extension.getIncludePropertyNames(contextKey),
						extension.getExcludePropertyNames(contextKey),
						extension.getReorderPropertyNames(contextKey));
			}
		}

		return dataModel;
	}
}
