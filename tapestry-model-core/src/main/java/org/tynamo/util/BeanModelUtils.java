package org.tynamo.util;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Mapper;
import org.apache.tapestry5.func.Predicate;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.tynamo.PageType;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.extension.BeanModelExtension;

import java.util.List;

/**
 * Utilities used to modify a {@link org.apache.tapestry5.beaneditor.BeanModel}.
 */
public final class BeanModelUtils
{

	/**
	 * Removes from the bean model the Tynamo "recommended" list of properties to exclude.
	 *
	 * It excludes all nonVisual properties from the BeanModel for ALL context, and for the "list" context it also removes
	 * identifier and collection properties.
	 *
	 * @param dataModel to modifiy
	 * @param classDescriptor
	 * @param key to choose which configuration set to apply
	 */
	public static void applyDefaultExclusions(BeanModel dataModel, TynamoClassDescriptor classDescriptor,
	                                          final String key)
	{
		List<String> nameList = F.flow(classDescriptor.getPropertyDescriptors()).filter(new Predicate<TynamoPropertyDescriptor>()
		{
			public boolean accept(TynamoPropertyDescriptor descriptor)
			{
				if (PageType.LIST.getContextKey().equals(key))
				{
					return descriptor.isIdentifier() || descriptor.isCollection() || descriptor.isNonVisual();
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

		dataModel.exclude((String[]) nameList.toArray(new String[nameList.size()]));
	}

	/**
	 * Performs standard set of modifications to a {@link org.apache.tapestry5.beaneditor.BeanModel}
	 * properties may be included, removed or reordered based on the contents of the {@link BeanModelExtension}
	 * and the value of context key
	 *
	 * @param dataModel to modifiy
	 * @param classDescriptor
	 * @param key to choose which configuration set to apply
	 */
	public static void modify(BeanModel dataModel, TynamoClassDescriptor classDescriptor, String key)
	{
		if (classDescriptor.supportsExtension(BeanModelExtension.class))
		{
			BeanModelExtension extension = classDescriptor.getExtension(BeanModelExtension.class);

			org.apache.tapestry5.internal.beaneditor.BeanModelUtils
					.modify(dataModel, null, extension.getIncludePropertyNames(key),
					        extension.getExcludePropertyNames(key),
					        extension.getReorderPropertyNames(key));

			if (extension.isApplyDefaultExclusions())
			{
				applyDefaultExclusions(dataModel, classDescriptor, key);
			}
		} else
		{
			applyDefaultExclusions(dataModel, classDescriptor, key);
		}
	}

	public static String join(String firstList, String optionalSecondList)
	{
		if (InternalUtils.isBlank(optionalSecondList))
			return firstList;

		return firstList + "," + optionalSecondList;
	}

}
