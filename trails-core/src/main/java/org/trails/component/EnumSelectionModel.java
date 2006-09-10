package org.trails.component;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.trails.TrailsRuntimeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnumSelectionModel extends IdentifierSelectionModel
{
    private Class type;

    private String noneLabel = DEFAULT_NONE_LABEL;

    public EnumSelectionModel(Class type)
    {
        this(type, false);

    }
    
    public EnumSelectionModel(List instances, boolean allowNone)
    {
        setAllowNone(instances, allowNone);
    }
    
    public EnumSelectionModel(Class type, boolean allowNone)
    {
        Object[] enums = type.getEnumConstants();
        List auxInstances = new ArrayList();
        for (int i = 0; i < enums.length; i++)
        {
            auxInstances.add(enums[i]);
        }

        this.type = type;
        setAllowNone(auxInstances, allowNone);
    }

    public EnumSelectionModel(Class type, String labelProperty, boolean allowNone)
    {
        this(type, allowNone);
        setLabelProperty(labelProperty);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.tapestry.form.IPropertySelectionModel#getValue(int)
     */
    public String getValue(int index)
    {
        try
        {
            if (allowNone && index == 0)
            {
                return DEFAULT_NONE_VALUE;
            } else
            {
                return instances.get(index).toString();
            }
        } catch (Exception e)
        {
            throw new TrailsRuntimeException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.tapestry.form.IPropertySelectionModel#translateValue(java.lang.String)
     */
    public Object translateValue(String value)
    {
        if (allowNone)
        {
            if (value.equals(DEFAULT_NONE_VALUE))
                return null;
        }
        for (Iterator iter = instances.iterator(); iter.hasNext();)
        {
            Object enumElement = iter.next();
            if (enumElement != null && enumElement.toString().equals(value))
            {
                return enumElement;
            }
        }
        return null;
    }

    public String getNoneLabel()
    {
        return noneLabel;
    }

    public void setNoneLabel(String noneLabel)
    {
        this.noneLabel = noneLabel;
    }
}
