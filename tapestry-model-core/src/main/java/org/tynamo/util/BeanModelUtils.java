package org.tynamo.util;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.tynamo.descriptor.TynamoClassDescriptor;

import java.util.List;

/**
 * Utilities used to modify a {@link org.apache.tapestry5.beaneditor.BeanModel}.
 */
public final class BeanModelUtils
{

	/**
	 * Removes properties from the bean model.
	 *
	 * @param model		   to modifiy
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

	public static final String join(String firstList, String optionalSecondList)
	{
		if (InternalUtils.isBlank(optionalSecondList))
			return firstList;

		return firstList + "," + optionalSecondList;
	}

}
