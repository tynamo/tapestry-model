package org.trails.descriptor;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.trails.persistence.PersistenceService;


public class TrailsDescriptorService implements DescriptorService
{
    protected List types;
    
    protected Map descriptors = new HashMap();
    
    private List decorators = new ArrayList();
    
    private List propertyExcludes = new ArrayList();
    
    private List methodExcludes = new ArrayList();
    
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
            descriptors.put(type, applyDecorators(buildClassDescriptor(type)));
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
                TrailsPropertyDescriptor propertyDescriptor = (TrailsPropertyDescriptor) iterator.next();
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

    protected IClassDescriptor buildClassDescriptor(Class type)
    {
        try
        {
            TrailsClassDescriptor descriptor = new TrailsClassDescriptor(type);
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            BeanUtils.copyProperties(descriptor, beanInfo.getBeanDescriptor());
            descriptor.setPropertyDescriptors(buildPropertyDescriptors(type, beanInfo));
            //descriptor.setMethodDescriptors(buildMethodDescriptors(beanInfo));
            return descriptor;
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

//    protected List buildMethodDescriptors(BeanInfo beanInfo)
//    {
//        ArrayList methodDescriptors = new ArrayList();
//        for (int i = 0; i < beanInfo.getMethodDescriptors().length; i++)
//        {
//            MethodDescriptor methodDescriptor = beanInfo.getMethodDescriptors()[i];
//            methodDescriptors.add(new TrailsMethodDescriptor(
//                methodDescriptor.getName(), methodDescriptor.getMethod().getParameterTypes()));
//        }
//        return methodDescriptors;
//    }

    protected List buildPropertyDescriptors(Class beanType, BeanInfo beanInfo) throws Exception
    {
        ArrayList propertyDescriptors = new ArrayList();
        for (int i = 0; i < beanInfo.getPropertyDescriptors().length; i++)
        {
            PropertyDescriptor beanPropDescriptor = beanInfo.getPropertyDescriptors()[i];
            if (!isExcluded(beanPropDescriptor.getName(), getPropertyExcludes()))
            {
                TrailsPropertyDescriptor propDescriptor = new TrailsPropertyDescriptor(beanType, beanPropDescriptor.getPropertyType());
                BeanUtils.copyProperties(propDescriptor, beanPropDescriptor);
                propertyDescriptors.add(propDescriptor);
            }
        }
        return propertyDescriptors;
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

    public List getMethodExcludes()
    {
        return methodExcludes;
    }
    

    public void setMethodExcludes(List methodExcludes)
    {
        this.methodExcludes = methodExcludes;
    }
    

    public List getPropertyExcludes()
    {
        return propertyExcludes;
    }
    

    public void setPropertyExcludes(List propertyExcludes)
    {
        this.propertyExcludes = propertyExcludes;
    }
    
    /**
     * @param methodDescriptor
     * @param excludes
     * @return
     */
    protected boolean isExcluded(String name, List excludes)
    {
        for (Iterator iter = excludes.iterator(); iter.hasNext();)
        {
            String exclude = (String) iter.next();

            if (name.matches(exclude))
            {
                return true;
            }
        }

        return false;
    }    
   
}
