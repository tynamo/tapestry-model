package org.tynamo.util;

import org.apache.tapestry5.ioc.Messages;

public class TynamoMessages
{
	public static String edit(Messages messages, Class type)
	{
		return messages.format(Utils.EDIT_MESSAGE, DisplayNameUtils.getDisplayName(type, messages));
	}

	public static String listAll(Messages messages, Class type)
	{
		return messages.format(Utils.LISTALL_LINK_MESSAGE, DisplayNameUtils.getPluralDisplayName(type, messages));
	}

	public static String show(Messages messages, String name)
	{
		return messages.format(Utils.SHOW_MESSAGE, name);
	}

	public static String list(Messages messages, Class type)
	{
		return messages.format(Utils.LIST_MESSAGE, DisplayNameUtils.getPluralDisplayName(type, messages));
	}

	public static String add(Messages messages, Class type)
	{
		return messages.format(Utils.NEW_MESSAGE, DisplayNameUtils.getDisplayName(type, messages));
	}

	public static String added(Messages messages, Object bean)
	{
		return messages.getFormatter(Utils.ADDED_MESSAGE).format(bean);
	}

	public static String saved(Messages messages, Object bean)
	{
		return messages.getFormatter(Utils.SAVED_MESSAGE).format(bean);
	}

}
