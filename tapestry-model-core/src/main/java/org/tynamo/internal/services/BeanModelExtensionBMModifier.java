package org.tynamo.internal.services;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.internal.beaneditor.BeanModelUtils;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.extension.BeanModelExtension;
import org.tynamo.services.BeanModelModifier;
import org.tynamo.services.DescriptorService;

/**
 * Performs standard set of modifications to a {@link org.apache.tapestry5.beaneditor.BeanModel}
 * properties may be included, removed or reordered based on the contents of the {@link org.tynamo.descriptor.extension.BeanModelExtension}
 * and the value of context key
 */
public class BeanModelExtensionBMModifier implements BeanModelModifier
{

	private DescriptorService descriptorService;

	public BeanModelExtensionBMModifier(DescriptorService descriptorService)
	{
		this.descriptorService = descriptorService;
	}

	public boolean modify(BeanModel beanModel, String key)
	{
		TynamoClassDescriptor classDescriptor = descriptorService.getClassDescriptor(beanModel.getBeanType());

		if (!classDescriptor.supportsExtension(BeanModelExtension.class)) return false;

		BeanModelExtension extension = classDescriptor.getExtension(BeanModelExtension.class);

		if (!extension.hasModifiersForKey(key)) return false;

		BeanModelUtils.modify(beanModel, null, extension.getIncludePropertyNames(key),
				extension.getExcludePropertyNames(key),
				extension.getReorderPropertyNames(key));

		return true;
	}
}
