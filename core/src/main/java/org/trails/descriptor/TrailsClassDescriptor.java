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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ognl.Ognl;
import ognl.OgnlException;

import org.trails.component.Utils;


/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TrailsClassDescriptor extends TrailsDescriptor implements IClassDescriptor
{
    private List propertyDescriptors = new ArrayList();
    private List methodDescriptors = new ArrayList();
    //private BeanDescriptor beanDescriptor;
    private boolean child;
    
    boolean allowRemove = true;
    
    boolean allowSave = true;
    
    public TrailsClassDescriptor(IClassDescriptor descriptor)
    {
        super(descriptor);
        copyPropertyDescriptorsFrom(descriptor);
        copyMethodDescriptorsFrom(descriptor);       
    }

	private void copyMethodDescriptorsFrom(IClassDescriptor descriptor)
	{
		for (Iterator iter = descriptor.getMethodDescriptors().iterator(); iter.hasNext();)
        {
            IMethodDescriptor  methodDescriptor = (IMethodDescriptor) iter.next();
            getMethodDescriptors().add(methodDescriptor.clone());
        }
	}

	protected void copyPropertyDescriptorsFrom(IClassDescriptor descriptor)
	{
		for (Iterator iter = descriptor.getPropertyDescriptors().iterator(); iter.hasNext();)
        {
            IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) iter.next();
            getPropertyDescriptors().add(propertyDescriptor.clone());
        }
	}
    
    public TrailsClassDescriptor(Class type)
    {
        super(type);
    }

    public TrailsClassDescriptor(Class type, String displayName)
    {
        super(type);
        this.setDisplayName(displayName);
    }
    
    /**
     * @return Returns the methodDescriptors.
     */
    public List getMethodDescriptors()
    {
        return methodDescriptors;
    }

    /**
     * @param methodDescriptors
     *            The methodDescriptors to set.
     */
    public void setMethodDescriptors(List methodDescriptors)
    {
        this.methodDescriptors = methodDescriptors;
    }

    /**
     * @return Returns the propertyDescriptors.
     */
    public List getPropertyDescriptors()
    {
        return propertyDescriptors;
    }

    /**
     * @param propertyDescriptors
     *            The propertyDescriptors to set.
     */
    public void setPropertyDescriptors(List propertyDescriptors)
    {
        this.propertyDescriptors = propertyDescriptors;
    }

    public IPropertyDescriptor getIdentifierDescriptor()
    {
        String ognl = "propertyDescriptors.{? identifier}[0]";

        return findDescriptor(ognl);
    }

    /**
     * @param ognl
     * @return
     */
    private IPropertyDescriptor findDescriptor(String ognl)
    {
        try
        {
            return (IPropertyDescriptor) Ognl.getValue(ognl, this);
        }catch (OgnlException oe)
        {
            //oe.printStackTrace();

            return null;
        }catch (IndexOutOfBoundsException ie)
        {
            return null;
        }
    }

    /**
     * @param string
     * @return
     */
    public IPropertyDescriptor getPropertyDescriptor(String name)
    {
        return findDescriptor("propertyDescriptors.{? name == '" + name +
            "'}[0]");
    }

    /**
     * @return
     */
    public String getPluralDisplayName()
    {
        return Utils.pluralize(Utils.unCamelCase(getDisplayName()));
    }
    
    /**
     * @return Returns the child.
     */
    public boolean isChild()
    {
        return child;
    }
    
    /**
     * @param child The child to set.
     */
    public void setChild(boolean child)
    {
        this.child = child;
    }

    @Override
    public Object clone()
    {
        return new TrailsClassDescriptor(this);
    }

    public List getPropertyDescriptors(String[] properties)
    {
        ArrayList descriptors = new ArrayList();
        for (int i = 0; i < properties.length; i++)
        {
            descriptors.add(getPropertyDescriptor(properties[i]));
        }
        return descriptors;
    }

    public boolean isAllowRemove()
    {
        return allowRemove;
    }

    public void setAllowRemove(boolean allowRemove)
    {
        this.allowRemove = allowRemove;
    }

    public boolean isAllowSave()
    {
        return allowSave;
    }

    public void setAllowSave(boolean allowSave)
    {
        this.allowSave = allowSave;
    }

    /**
     * Added toString method to help with unit testing debugging.
     */
    public String toString() {
    	return "{TrailsClassDescriptor - Type: " + getType() + "}";
    }

}
