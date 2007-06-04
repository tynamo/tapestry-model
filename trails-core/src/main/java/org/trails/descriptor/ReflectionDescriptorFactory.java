package org.trails.descriptor;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Generate descriptors using reflection on the underlying class.
 * ReflectionDescriptorFactory.buildClassDescriptor() is the only public method
 * here.
 */
public class ReflectionDescriptorFactory implements DescriptorFactory
{
	protected static final Log LOG = LogFactory.getLog(ReflectionDescriptorFactory.class);

	private List propertyExcludes = new ArrayList();

	private List methodExcludes = new ArrayList();

	/**
	 * Given a type, build a property descriptor
	 *
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
			descriptor.setPropertyDescriptors(buildPropertyDescriptors(type,beanInfo, descriptor));
			return descriptor;

		} catch (IllegalAccessException e)
		{
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e)
		{
			LOG.error(e.toString());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Build the set of property descriptors for this type
	 *
	 * @param beanType			  the aggregating class
	 * @param beanInfo			  the BeanInfo, already gathered
	 * @param parentClassDescriptor reference to the aggregating class, used for recovery with
	 *                              graph traversal
	 * @return ObjectReferenceDescriptor if this property is an association,
	 *         otherwise a TrailsPropertyDescriptor
	 * @throws Exception
	 */
	protected ArrayList<IPropertyDescriptor> buildPropertyDescriptors(
		Class beanType, BeanInfo beanInfo,
		IClassDescriptor parentClassDescriptor) throws Exception
	{
		ArrayList<IPropertyDescriptor> result = new ArrayList<IPropertyDescriptor>();
		for (PropertyDescriptor beanPropDescriptor : beanInfo.getPropertyDescriptors())
		{
			if (!isExcluded(beanPropDescriptor.getName(), getPropertyExcludes()))
			{
				TrailsPropertyDescriptor propDescriptor;
				Class<?> propertyType = beanPropDescriptor.getPropertyType();
				propDescriptor = new TrailsPropertyDescriptor(beanType,propertyType);
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
