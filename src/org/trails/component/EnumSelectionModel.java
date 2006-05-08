package org.trails.component;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.trails.TrailsRuntimeException;

import java.util.ArrayList;
import java.util.List;

public class EnumSelectionModel implements IPropertySelectionModel
{
    private List instances;
    private boolean allowNone;
    private Class type;
    public static String DEFAULT_NONE_LABEL = "None";
    public static String DEFAULT_NONE_VALUE = "none";

    private String noneLabel = EnumSelectionModel.DEFAULT_NONE_LABEL;

    public EnumSelectionModel(Class type)
    {
        this(type, false);

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
        this.allowNone = allowNone;
        this.instances = auxInstances;
        if (this.allowNone)
        {
            this.instances = new ArrayList();
            this.instances.addAll(auxInstances);
            this.instances.add(0, null);
        }
    }

    /* (non-Javadoc)
    * @see org.apache.tapestry.form.IPropertySelectionModel#getOptionCount()
    */
    public int getOptionCount()
    {
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
            return getNoneLabel();
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
                return EnumSelectionModel.DEFAULT_NONE_VALUE;
            } else
            {
                return instances.get(index).toString();
            }
        } catch (Exception e)
        {
            throw new TrailsRuntimeException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.apache.tapestry.form.IPropertySelectionModel#translateValue(java.lang.String)
     */
    public Object translateValue(String value)
    {
        try
        {
            if (allowNone)
            {
                if (value.equals(EnumSelectionModel.DEFAULT_NONE_VALUE)) return null;
            }
            return Enum.valueOf(type, value);
        } catch (Exception e)
        {
            throw new TrailsRuntimeException(e);
        }
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



