package org.trails.component;

import java.util.List;
import java.util.Locale;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.components.Block;
import org.trails.TrailsRuntimeException;
import org.trails.descriptor.IClassDescriptor;
import org.trails.i18n.ResourceBundleMessageSource;

public abstract class ClassDescriptorComponent extends BaseComponent
{

    public ClassDescriptorComponent()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public abstract IClassDescriptor getClassDescriptor();

    public abstract void setClassDescriptor(IClassDescriptor ClassDescriptor);

    public abstract String[] getPropertyNames();

    public abstract void setPropertyNames(String[] PropertyNames);

    /**
     * @return
     * @throws OgnlException
     */
    public List getPropertyDescriptors()
    {
        if (getPropertyNames() == null || getPropertyNames().length == 0)
        {
            try
            {
                return (List)Ognl.getValue("#this.{? not(hidden)}", getClassDescriptor().getPropertyDescriptors());
            }
            catch (OgnlException oe)
            {
                throw new TrailsRuntimeException(oe);
            }
        }
        else
        {
            return getClassDescriptor().getPropertyDescriptors(getPropertyNames());
        }
    }

    /**
     * Return the Spring ResourceBundleMessageSource. This is used to implement
     * i18n in all Trails components, accessing a i18n properties file in the
     * application instead of accessing the property file located in org.trais.component package.
     * By doing this, someone who would need i18n wouldn't need to change the property
     * located in the org.trails.component package and rebuild the trails.jar
     * @return
     */
    @InjectObject("spring:trailsMessageSource")
    public abstract ResourceBundleMessageSource getResourceBundleMessageSource();

    public String getMessage(String key) {
    	Locale locale = getContainer().getPage().getEngine().getLocale();
       	return getResourceBundleMessageSource().getMessageWithDefaultValue(key, locale, "[TRAILS][" + key.toUpperCase() + "]");
    }

	public boolean hasBlock(String propertyName)
	{
	    if (getPage().getComponents().containsKey(propertyName))
	    {
	        return true;
	    }
	    return false;
	}

	public Block getBlock(String propertyName)
	{
	    if (getPage().getComponents().containsKey(propertyName))
	    {
	        return (Block)getPage().getComponent(propertyName);
	    }
	    return null;
	}
}
