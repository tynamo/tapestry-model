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
package org.trails.descriptor;

import org.apache.commons.lang.builder.EqualsBuilder;



/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TrailsPropertyDescriptor extends TrailsDescriptor implements IPropertyDescriptor
{
    private boolean searchable = true;

    private boolean required;

    private boolean readOnly;

    private String name;

    private int index = UNDEFINED_INDEX;

    private int length = DEFAULT_LENGTH;

    private boolean large;

    private String format;

    private boolean summary = true;

    private boolean richText;

    private Class beanType;

    private IClassDescriptor parentClassDescriptor;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // constructors
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * It's kinda like an old-skool C++ copy constructor
     *
     */
    public TrailsPropertyDescriptor(Class beanType, IPropertyDescriptor descriptor)
    {
        this(beanType, descriptor.getPropertyType());
        copyFrom(descriptor);
    }

    public TrailsPropertyDescriptor(Class beanType, Class type)
    {
        super(type);
        this.beanType = beanType;
    }

    public TrailsPropertyDescriptor(Class beanType, String name, Class type)
    {
        this(beanType, type);
        this.name = name;
        setDisplayName(name);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // methods
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return
     */
    public Class getPropertyType()
    {
        return getType();
    }

    /**
     * @return
     */
    public boolean isNumeric()
    {
        return getPropertyType().getName().endsWith("Double") ||
        getPropertyType().getName().endsWith("Integer") ||
        getPropertyType().getName().endsWith("Float") ||
        getPropertyType().getName().endsWith("double") ||
        getPropertyType().getName().endsWith("int") ||
        getPropertyType().getName().endsWith("float") ||
        getPropertyType().getName().endsWith("BigDecimal");
    }

    public boolean isBoolean()
    {
        return getPropertyType().getName().endsWith("boolean") ||
            getPropertyType().getName().endsWith("Boolean");
    }
    /**
     * @return
     */
    public boolean isDate()
    {
        // TODO Auto-generated method stub
        return getPropertyType().getName().endsWith("Date");
    }

    /**
     * @return
     */
    public boolean isString()
    {
        // TODO Auto-generated method stub
        return getPropertyType().getName().endsWith("String");
    }


    /**
     * @see org.trails.descriptor.IPropertyDescriptor#getParentClassDescriptor
     */
    public IClassDescriptor getParentClassDescriptor()
    {
        return parentClassDescriptor;
    }

    /**
     * @see org.trails.descriptor.IPropertyDescriptor#setParentClassDescriptor
     */
    public void setParentClassDescriptor(IClassDescriptor parentClassDescriptor)
    {
        this.parentClassDescriptor = parentClassDescriptor;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // bean setters / getters
    /////////////////////////////////////////////////////////////////////////////////////////////////////////


    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    /**
     * @return
     */
    public boolean isObjectReference()
    {
        return false;
    }

    /**
     * @return
     */
    public boolean isOwningObjectReference()
    {
        return false;
    }

    /**
     * @return Returns the required.
     */
    public boolean isRequired()
    {
        return required;
    }

    /**
     * @param required The required to set.
     */
    public void setRequired(boolean required)
    {
        this.required = required;
    }

    /**
     * @return
     */
    public boolean isReadOnly()
    {
        return readOnly;
    }

    /**
     * @param readOnly The readOnly to set.
     */
    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }

    /**
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return Returns the identifier.
     */
    public boolean isIdentifier()
    {
        return false;
    }

    /**
     * @return Returns the collection.
     */
    public boolean isCollection()
    {
        return false;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public Object clone()
    {
        return new TrailsPropertyDescriptor(beanType, this);
    }

    public boolean equals(Object obj)
    {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    public int getLength()
    {
        return length;
    }

    public void setLength(int length)
    {
        this.length = length;
    }

    public boolean isLarge()
    {
        return large;
    }

    public void setLarge(boolean large)
    {
        this.large = large;
    }

    public String getFormat()
    {
        return format;
    }

    public void setFormat(String format)
    {
        this.format = format;
    }

    public boolean isSearchable()
    {
        return searchable;
    }

    public void setSearchable(boolean searchable)
    {
        this.searchable = searchable;
    }

    public boolean isSummary()
    {
        return summary;
    }

    public void setSummary(boolean summary)
    {
        this.summary = summary;
    }

    public boolean isRichText()
    {
        return richText;
    }

    public void setRichText(boolean richText)
    {
        this.richText = richText;
    }

    public Class getBeanType() {
        return beanType;
    }

    public void setBeanType(Class beanType) {
        this.beanType = beanType;
    }

    public boolean isEmbedded()
    {
        return false;
    }

}
