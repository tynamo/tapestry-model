/*
 * Created on Mar 18, 2005
 *
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
package org.trailsframework.descriptor;


/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public interface IPropertyDescriptor extends IDescriptor
{
	public static final int UNDEFINED_INDEX = -1;

	public static final int DEFAULT_LENGTH = 255;

	public void setIndex(int index);

	public int getIndex();

	/**
	 * @return
	 */
	public Class getPropertyType();

	/**
	 * @return
	 */
	public boolean isNumeric();

	public boolean isBoolean();

	/**
	 * @return
	 */
	public boolean isDate();

	/**
	 * @return
	 */
	public boolean isString();

	/**
	 * @return
	 */
	public boolean isObjectReference();

	/**
	 * @return Returns the required.
	 */
	public boolean isRequired();

	/**
	 * @param required The required to set.
	 */
	public void setRequired(boolean required);

	/**
	 * @return
	 */
	public boolean isReadOnly();

	/**
	 * @param readOnly The readOnly to set.
	 */
	public void setReadOnly(boolean readOnly);

	/**
	 * @return
	 */
	public String getName();

	public void setName(String name);

	/**
	 * @return
	 */
	public String getShortDescription();

	public void setShortDescription(String shortDescription);

	public int getLength();

	public void setLength(int length);

	public abstract boolean isLarge();

	public abstract void setLarge(boolean Large);

	public String getFormat();

	public void setFormat(String format);

	public boolean isSearchable();

	public void setSearchable(boolean searchable);

	public boolean isSummary();

	public boolean isCollection();

	public void setSummary(boolean summary);

	public boolean isEmbedded();

	public boolean isRichText();

	public boolean isIdentifier();

	public void setRichText(boolean richText);

	public Class getBeanType();

	public void setBeanType(Class beanType);

}