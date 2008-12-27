package org.trailsframework.util;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ioc.Messages;
import org.trailsframework.descriptor.IClassDescriptor;
import org.trailsframework.descriptor.IPropertyDescriptor;


public class DisplayNameUtils
{

	private static final String PLURAL_SUFIX = "__plural";
	private static final String SHORTDESC_SUFIX = "__shortDescription";

	public static String getDisplayName(IClassDescriptor classDescriptor, Messages messages)
	{
		String fullName = classDescriptor.getType().getName();
		String shortName = classDescriptor.getType().getSimpleName();
		return selectDisplayName(fullName, shortName, classDescriptor.getType().getSimpleName(), messages);
	}

	public static String getDisplayName(IPropertyDescriptor propertyDescriptor, Messages messages)
	{
		String fullName = propertyDescriptor.getBeanType().getName() + "." + propertyDescriptor.getName();
		String shortName = propertyDescriptor.getName();
		return selectDisplayName(fullName, shortName, Utils.unCamelCase(propertyDescriptor.getName()), messages);
	}


	public static String getPluralDisplayName(IClassDescriptor classDescriptor, Messages messages)
	{
		String fullName = classDescriptor.getType().getName() + PLURAL_SUFIX;
		String shortName = classDescriptor.getType().getSimpleName() + PLURAL_SUFIX;
		return selectDisplayName(fullName, shortName, Utils.pluralize(classDescriptor.getType().getSimpleName()), messages);
	}

	public static String getShortDescription(IClassDescriptor classDescriptor, Messages messages)
	{
		String fullName = classDescriptor.getType().getName() + SHORTDESC_SUFIX;
		String shortName = classDescriptor.getType().getSimpleName() + SHORTDESC_SUFIX;
		return selectDisplayName(fullName, shortName, StringUtils.EMPTY, messages);
	}

	public static String getShortDescription(IPropertyDescriptor propertyDescriptor, Messages messages)
	{
		String fullName = propertyDescriptor.getBeanType().getName() + "." + propertyDescriptor.getName() + SHORTDESC_SUFIX;
		String shortName = propertyDescriptor.getName() + SHORTDESC_SUFIX;
		return selectDisplayName(fullName, shortName, StringUtils.EMPTY, messages);
	}


	/**
	 * Selects a localized message, given two keys and a default value in case no key is found.
	 */
	private static String selectDisplayName(String firstKey, String secondKey, String defValue, Messages messages)
	{

		if (messages.contains(firstKey)) return messages.get(firstKey);
		if (messages.contains(secondKey)) return messages.get(secondKey);

		return defValue;
	}

}
