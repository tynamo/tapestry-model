package org.tynamo.util;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.tynamo.pages.PageType;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.extension.BeanModelExtension;

import java.util.List;

/**
 * Utilities used to modify a {@link org.apache.tapestry5.beaneditor.BeanModel}.
 */
public final class BeanModelUtils
{

	/**
	 * Removes properties from the bean model.
	 *
	 * @param model           to modifiy
	 * @param classDescriptor
	 */
	public static void exclude(BeanModel model, TynamoClassDescriptor classDescriptor)
	{
		try
		{
			List<String> nameList = (List<String>) Ognl
					.getValue("propertyDescriptors.{? identifier or !summary or nonVisual or collection}.{name}",
					          classDescriptor);

			model.exclude((String[]) nameList.toArray(new String[nameList.size()]));

		} catch (OgnlException e)
		{
		}
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
		if (PageType.LIST.getContextKey().equals(key))
		{
			exclude(dataModel, classDescriptor);
		}

		if (classDescriptor.supportsExtension(BeanModelExtension.class))
		{
			BeanModelExtension extension = classDescriptor.getExtension(BeanModelExtension.class);

			org.apache.tapestry5.internal.beaneditor.BeanModelUtils
					.modify(dataModel, null, extension.getIncludePropertyNames(key),
					        extension.getExcludePropertyNames(key),
					        extension.getReorderPropertyNames(key));
		}
	}

	public static String join(String firstList, String optionalSecondList)
	{
		if (InternalUtils.isBlank(optionalSecondList))
			return firstList;

		return firstList + "," + optionalSecondList;
	}

}
