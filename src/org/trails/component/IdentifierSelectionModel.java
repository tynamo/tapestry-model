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
package org.trails.component;

import java.util.ArrayList;
import java.util.List;

import ognl.Ognl;

import org.apache.commons.beanutils.BeanUtils;
import org.trails.TrailsRuntimeException;
import org.apache.tapestry.form.IPropertySelectionModel;


/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IdentifierSelectionModel implements IPropertySelectionModel
{
    private List instances;
    private String idPropery = "id";
    private boolean allowNone;
    public static String NONE_LABEL = "None";
    public static String NONE_VALUE = "none";
    
    public IdentifierSelectionModel(List instances, String idProperty)
    {
        this.idPropery = idProperty;

        this.instances = instances;
    }

    public IdentifierSelectionModel(List instances, String idProperty, boolean allowNone)
    {
        this(instances, idProperty);
        this.allowNone = allowNone;
        if (this.allowNone)
        {
            this.instances = new ArrayList();
            this.instances.addAll(instances);
            this.instances.add(0, null);
        }
    }
    
    /* (non-Javadoc)
     * @see org.apache.tapestry.form.IPropertySelectionModel#getOptionCount()
     */
    public int getOptionCount()
    {
        // TODO Auto-generated method stub
        return instances.size();
    }

    /* (non-Javadoc)
     * @see org.apache.tapestry.form.IPropertySelectionModel#getOption(int)
     */
    public Object getOption(int index)
    {
        return instances.get(index);
    }

    /* (non-Javadoc)
     * @see org.apache.tapestry.form.IPropertySelectionModel#getLabel(int)
     */
    public String getLabel(int index)
    {
        if (allowNone && index == 0)
        {
            return NONE_LABEL;
        }
        return instances.get(index).toString();
    }

    /* (non-Javadoc)
     * @see org.apache.tapestry.form.IPropertySelectionModel#getValue(int)
     */
    public String getValue(int index)
    {
        try
        {
            if (allowNone && index == 0)
            {
                return NONE_VALUE;
            }
            else
            {
                return BeanUtils.getProperty(instances.get(index), idPropery);
            }
        }catch (Exception e)
        {
            throw new TrailsRuntimeException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.apache.tapestry.form.IPropertySelectionModel#translateValue(java.lang.String)
     */
    public Object translateValue(String value)
    {
        List realInstances = allowNone ? instances.subList(1, instances.size()) : instances;
        try
        {
            if (allowNone)
            {
                if (value.equals(NONE_VALUE)) return null;
            }
            return Ognl.getValue("#root.{? #this." + idPropery +
                ".toString() == \"" + value + "\" }[0]", realInstances);
        }catch (Exception e)
        {
            throw new TrailsRuntimeException(e);
        }
    }

    /**
     * @return Returns the instances.
     */
    public List getInstances()
    {
        return instances;
    }

    /**
     * @param instances The instances to set.
     */
    public void setInstances(List instances)
    {
        this.instances = instances;
    }
}
