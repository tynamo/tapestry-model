/*
 * Copyright 2004 Chris Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.tynamo.util;

import ognl.Ognl;
import ognl.OgnlException;
import org.tynamo.exception.TynamoRuntimeException;

import java.util.Collection;
import java.util.HashMap;


public class Utils
{

	/* keys used to internationalize Tynamo in Tapestry components and pages */
	public static final String APPLY_MESSAGE = "org.tynamo.i18n.apply";
	public static final String APPLY_AND_RETURN_MESSAGE = "org.tynamo.i18n.applyAndReturn";
	public static final String REMOVE_MESSAGE = "org.tynamo.i18n.remove";
	public static final String CANCEL_MESSAGE = "org.tynamo.i18n.cancel";
	public static final String ADD_NEW_MESSAGE = "org.tynamo.i18n.addNew";

	public static final String EDIT_MESSAGE = "org.tynamo.i18n.edit";
	public static final String SHOW_MESSAGE = "org.tynamo.i18n.show";
	public static final String LIST_MESSAGE = "org.tynamo.i18n.list";
	public static final String NEW_MESSAGE = "org.tynamo.i18n.new";

	public static final String ADDED_MESSAGE = "org.tynamo.i18n.added";
	public static final String SAVED_MESSAGE = "org.tynamo.i18n.saved";

	public static final String LISTALL_LINK_MESSAGE = "org.tynamo.component.listalllink";

	/**
	 * Thank you, AndroMDA project...
	 * Linguistically pluralizes a singular noun. <p/>
	 * <ul>
	 * <li><code>noun</code> becomes <code>nouns</code></li>
	 * <li><code>key</code> becomes <code>keys</code></li>
	 * <li><code>word</code> becomes <code>words</code></li>
	 * <li><code>property</code> becomes <code>properties</code></li>
	 * <li><code>bus</code> becomes <code>busses</code></li>
	 * <li><code>boss</code> becomes <code>bosses</code></li>
	 * </ul>
	 * <p/>
	 * Whitespace as well as <code>null></code> arguments will return an empty
	 * String.
	 * </p>
	 *
	 * @param singularNoun A singularNoun to pluralize
	 * @return The plural of the argument singularNoun
	 */
	public static String pluralize(String singularNoun)
	{
		String pluralNoun = singularNoun;

		int nounLength = pluralNoun.length();

		if (nounLength == 1)
		{
			pluralNoun = pluralNoun + 's';
		} else if (nounLength > 1)
		{
			char secondToLastChar = pluralNoun.charAt(nounLength - 2);

			if (pluralNoun.endsWith("y"))
			{
				switch (secondToLastChar)
				{
					case 'a': // fall-through
					case 'e': // fall-through
					case 'i': // fall-through
					case 'o': // fall-through
					case 'u':
						pluralNoun = pluralNoun + 's';
						break;
					default:
						pluralNoun = pluralNoun.substring(0, nounLength - 1)
							+ "ies";
				}
			} else if (pluralNoun.endsWith("s"))
			{
				switch (secondToLastChar)
				{
					case 's':
						pluralNoun = pluralNoun + "es";
						break;
					default:
						pluralNoun = pluralNoun + "ses";
				}
			} else
			{
				pluralNoun = pluralNoun + 's';
			}
		}
		return pluralNoun;
	}

	/**
	 * @param type the (usable) super type if passed a CGLIB enhanced class
	 * @return
	 */
	public static Class checkForCGLIB(Class type)
	{
		if (type.getName().contains("CGLIB"))
		{
			return type.getSuperclass();
		} else return type;
	}

	public static void executeOgnlExpression(String ognlExpression, Object member, Object model)
	{
		HashMap context = new HashMap();
		context.put("member", member);

		try
		{
			Ognl.getValue(ognlExpression + "(#member)", context, model);
		} catch (OgnlException e)
		{
			throw new TynamoRuntimeException(e, model.getClass());
		}
	}

	/**
	 * Tests whether or not a name matches against at least one exclude pattern.
	 *
	 * @param name The name to match. Must not be null.
	 * @param exclusionPatterns the list of exclude patterns to test against
	 * @return true when the name matches against at least one exclude pattern, or false otherwise.
	 */
	public static boolean isExcluded(String name, Collection<String> exclusionPatterns)
	{
		for (String exclusionPattern : exclusionPatterns)
		{
			if (name.matches(exclusionPattern))
			{
				return true;
			}
		}

		return false;
	}
}
