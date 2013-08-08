package org.tynamo.internal.services;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Mapper;
import org.apache.tapestry5.func.Predicate;
import org.tynamo.PageType;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.extension.BlobDescriptorExtension;
import org.tynamo.services.BeanModelModifier;
import org.tynamo.services.DescriptorService;

import java.util.List;

/**
 * Removes the Tynamo "recommended" list of properties to exclude from the bean model.
 * <p/>
 * It excludes all nonVisual properties from the BeanModel for ALL the contexts, and for the "list" context key it also
 * removes identifier properties, collection properties and blob properties.
 */
public class DefaultExclusionsBMModifier implements BeanModelModifier
{

	private DescriptorService descriptorService;

	public DefaultExclusionsBMModifier(DescriptorService descriptorService)
	{
		this.descriptorService = descriptorService;
	}

	/**
	 * @param beanModel to modifiy
	 * @param key       to choose which configuration set to apply
	 */
	@Override
	public boolean modify(BeanModel<?> beanModel, final String key)
	{

		TynamoClassDescriptor classDescriptor = descriptorService.getClassDescriptor(beanModel.getBeanType());

		List<String> nameList = F.flow(classDescriptor.getPropertyDescriptors()).filter(new Predicate<TynamoPropertyDescriptor>()
		{
			public boolean accept(TynamoPropertyDescriptor descriptor)
			{
				if (PageType.LIST.getContextKey().equals(key))
				{
					return descriptor.isIdentifier() || descriptor.isCollection() || descriptor.isNonVisual() ||
							descriptor.supportsExtension(BlobDescriptorExtension.class);
				} else
				{
					return descriptor.isNonVisual();
				}
			}
		}).map(new Mapper<TynamoPropertyDescriptor, String>()
		{
			public String map(TynamoPropertyDescriptor descriptor)
			{
				return descriptor.getName();
			}
		}).toList();

		beanModel.exclude((String[]) nameList.toArray(new String[nameList.size()]));
		return true;
	}
}
