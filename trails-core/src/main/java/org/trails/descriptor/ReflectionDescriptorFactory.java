package org.trails.descriptor;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Generate descriptors using reflection on the underlying class.  ReflectionDescriptorFactory.buildClassDescriptor()
 * is the only public method here.
 */
public class ReflectionDescriptorFactory implements DescriptorFactory
{
	private List propertyExcludes = new ArrayList();
	
	private List methodExcludes = new ArrayList();

    /**
     * Given a type, build a property descriptor
     * @param type The type to build for
     * @return a completed property descriptor
     */
    public IClassDescriptor buildClassDescriptor(Class type)
    {
        try
        {
            IClassDescriptor descriptor = new TrailsClassDescriptor(type);
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            BeanUtils.copyProperties(descriptor, beanInfo.getBeanDescriptor());
            descriptor.setPropertyDescriptors(buildPropertyDescriptors(type, beanInfo, descriptor));
            return descriptor;
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Build the set of property descriptors for this type
     * @param type the aggregating class
     * @param beanInfo the BeanInfo, already gathered
     * @param parentClassDescriptor reference to the aggregating class, used for recovery with graph traversal
     * @return ObjectReferenceDescriptor if this property is an association, otherwise a TrailsPropertyDescriptor
     * @throws Exception
     */
    protected ArrayList<IPropertyDescriptor> buildPropertyDescriptors(Class type, BeanInfo beanInfo, IClassDescriptor parentClassDescriptor) throws Exception
    {
        ArrayList<IPropertyDescriptor> result = new ArrayList<IPropertyDescriptor>();
        for (PropertyDescriptor beanPropDescriptor : beanInfo.getPropertyDescriptors())
        {
            if (!isExcluded(beanPropDescriptor.getName(), getPropertyExcludes()))
            {
                TrailsPropertyDescriptor propDescriptor;
                Class<?> propertyType = beanPropDescriptor.getPropertyType();
                if (propertyType.isEnum() || propertyType.isPrimitive() || propertyType.isArray() || propertyType.getPackage().getName().startsWith("java"))
                {
                    propDescriptor = new TrailsPropertyDescriptor(type, beanPropDescriptor.getPropertyType());
                }
                else
                {
                    propDescriptor = new ObjectReferenceDescriptor(type, beanPropDescriptor.getPropertyType(), beanPropDescriptor.getPropertyType());

                }
                BeanUtils.copyProperties(propDescriptor, beanPropDescriptor);
                TrailsPropertyDescriptor newPropertyDescriptor = propDescriptor;
                result.add(newPropertyDescriptor);
            }
        }
        return result;
    }

    protected boolean isExcluded(String name, List excludes)
    {
        for (Object exclude1 : excludes)
        {
            String exclude = (String) exclude1;

            if (name.matches(exclude))
            {
                return true;
            }
        }

        return false;
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
}
