package org.tynamo.util;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.Messages;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.descriptor.TrailsPropertyDescriptor;


public class DisplayNameUtils
{

	private static final String PLURAL_SUFIX = "-plural";
	private static final String SHORTDESC_SUFIX = "-shortDescription";


	/**
	 * Looks for a label within the messages based on the class name.
	 * If found, it is used, otherwise the name is "linguistically pluralized" (only works in English)
	 * The message key is the full or the simple name of the entity suffixed with "-plural".
	 */
	public static String getPluralDisplayName(TrailsClassDescriptor classDescriptor, Messages messages)
	{
		String fullName = classDescriptor.getType().getName() + PLURAL_SUFIX;
		String shortName = classDescriptor.getType().getSimpleName() + PLURAL_SUFIX;
		return selectDisplayName(fullName, shortName, Utils.pluralize(TapestryInternalUtils.toUserPresentable(classDescriptor.getType().getSimpleName())), messages);
	}

	/**
	 * Looks for a label within the messages based on the class name.
	 * If found, it is used, otherwise the name is converted to a user presentable form.
	 * The message key is either the full name of the entity or the simpleName
	 */
	public static String getDisplayName(TrailsClassDescriptor classDescriptor, Messages messages)
	{
		String fullName = classDescriptor.getType().getName();
		String shortName = classDescriptor.getType().getSimpleName();
		return selectDisplayName(fullName, shortName, TapestryInternalUtils.toUserPresentable(classDescriptor.getType().getSimpleName()), messages);
	}

	/**
	 * Looks for a label within the messages based on the property name.
	 * If found, it is used, otherwise the name is converted to a user presentable form.
	 * The message key is the property name suffixed with "-label".
	 */
	public static String getDisplayName(TrailsPropertyDescriptor propertyDescriptor, Messages messages)
	{
		return TapestryInternalUtils.defaultLabel(propertyDescriptor.getName(), messages, propertyDescriptor.getName());
	}

	/**
	 * Looks for a label within the messages based on the class name.
	 * If found, it is used, otherwise an empty string is returned
	 * The message key is the property name suffixed with "-shortDescription".
	 */
	public static String getShortDescription(TrailsClassDescriptor classDescriptor, Messages messages)
	{
		String fullName = classDescriptor.getType().getName() + SHORTDESC_SUFIX;
		String shortName = classDescriptor.getType().getSimpleName() + SHORTDESC_SUFIX;
		return selectDisplayName(fullName, shortName, StringUtils.EMPTY, messages);
	}

	/**
	 * Looks for a label within the messages based on the property name.
	 * If found, it is used, otherwise an empty string is returned
	 * The message key is the full or the simple name of the entity suffixed with "-shortDescription".
	 */
	public static String getShortDescription(TrailsPropertyDescriptor propertyDescriptor, Messages messages)
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
