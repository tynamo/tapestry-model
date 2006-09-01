package org.trails.descriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;


public class TrailsDescriptorService implements DescriptorService
{
    protected List types;
    
    protected Map descriptors = new HashMap();
    
    private List decorators = new ArrayList();
    
    private DescriptorFactory descriptorFactory;
    
    public class DescriptorComparator implements Comparator
    {

        public int compare(Object o1, Object o2)
        {
            IClassDescriptor descriptor1 = (IClassDescriptor)o1;
            IClassDescriptor descriptor2 = (IClassDescriptor)o2;
            return descriptor1.getDisplayName().compareTo(
                    descriptor2.getDisplayName());
        }
        
    }
    
    public void init() throws OgnlException
    {
        descriptors.clear();
        for (Iterator iter = types.iterator(); iter.hasNext();)
        {
            Class type = (Class) iter.next();
            IClassDescriptor descriptor = getDescriptorFactory().buildClassDescriptor(type);
            descriptor = applyDecorators(descriptor);
            descriptors.put(type, descriptor);
        }
        // second pass to find children
        markChildClasses();
    }

    /**
     * Have the decorators decorate this descriptor
     * @param descriptor
     * @return The resulting descriptor after all decorators are applied
     */
    protected IClassDescriptor applyDecorators(IClassDescriptor descriptor)
    {
        IClassDescriptor currDescriptor = descriptor;
        for (Iterator iter = getDecorators().iterator(); iter.hasNext();)
        {
            DescriptorDecorator decorator = (DescriptorDecorator) iter.next();
            currDescriptor = decorator.decorate(currDescriptor);
        }
        return currDescriptor;
    }

    protected void markChildClasses() throws OgnlException
    {
        List childRelationships = (List)Ognl.getValue("#root.{ propertyDescriptors }", descriptors);
        for (Iterator iter = childRelationships.iterator(); iter.hasNext();)
        {
            List list = (List)iter.next();
            for (Iterator iterator = list.iterator(); iterator.hasNext();)
            {
                IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) iterator.next();
                if (propertyDescriptor.isCollection() && ((CollectionDescriptor)propertyDescriptor).isChildRelationship())
                {  
                    getClassDescriptor(((CollectionDescriptor)propertyDescriptor).getElementType()).setChild(true);
                }
            }

        }
    }
    
    /* (non-Javadoc)
     * @see org.trails.descriptor.IDescriptorFactory#buildClassDescriptor(java.lang.Class)
     */
    public IClassDescriptor getClassDescriptor(Class type)
    {
        if (type.getName().contains("CGLIB"))
        {
            return (IClassDescriptor)descriptors.get(type.getSuperclass());
        }
        else
        {
            return (IClassDescriptor)descriptors.get(type);
        }
    }


    public List getTypes()
    {
        return types;
    }
    
    /**
     * 
     * @param types all the classes this service should describe
     */
    public void setTypes(List types)
    {
        this.types = types;
    }

    public List getAllDescriptors()
    {
        List allDescriptors = new ArrayList(descriptors.values());
        Collections.sort(
                allDescriptors,
                new DescriptorComparator());
        return allDescriptors;
    }

    public List getDecorators()
    {
        return decorators;
    }
    

    public void setDecorators(List decorators)
    {
        this.decorators = decorators;
    }

	public DescriptorFactory getDescriptorFactory()
	{
		return descriptorFactory;
	}

	public void setDescriptorFactory(DescriptorFactory descriptorFactory)
	{
		this.descriptorFactory = descriptorFactory;
	}


   
}
