package org.trails.descriptor;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

public class ReflectionDescriptorFactory implements DescriptorFactory
{
	private List propertyExcludes = new ArrayList();
	
	private List methodExcludes = new ArrayList();
	
    public IClassDescriptor buildClassDescriptor(Class type)
    {
        try
        {
            IClassDescriptor descriptor = new TrailsClassDescriptor(type);
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
