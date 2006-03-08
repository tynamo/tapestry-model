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
package org.trails.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.validation.ValidateUniqueness;

@Entity
@ValidateUniqueness(property = "name")
public class Thing
{
    private Integer id;

    private String name;

    private String text;
    
    private Integer number;
    
    private Integer number2;

    private boolean flag;

    @Id
    @PropertyDescriptor(summary = false)
    public Integer getId()
    {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @return Returns the name.
     * @hibernate.property not-null="true"
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @hibernate.property type="yes_no"
     * @return Returns the on.
     */
    public boolean isFlag()
    {
        return flag;
    }

    /**
     * @param on
     *            The on to set.
     */
    public void setFlag(boolean on)
    {
        this.flag = on;
    }

    @Column(length = 300)
    @PropertyDescriptor(summary = false)
    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    @Override
    public boolean equals(Object obj)
    {

        if (this == obj)
            return true;
        try
        {
            final Thing many = (Thing) obj;
            if (!getId().equals(many.getId()))
                return false;
            return true;
        } catch (Exception e)
        {
            return false;
        }

    }
    
    public String toString() { return getName(); }

    public Integer getNumber()
    {
        return number;
    }

    public void setNumber(Integer number)
    {
        this.number = number;
    }

    public Integer getNumber2()
    {
        return number2;
    }

    public void setNumber2(Integer number2)
    {
        this.number2 = number2;
    }

}
