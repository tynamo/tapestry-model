package org.trailsframework.descriptor;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trailsframework.util.Utils;

/**
 * Generate descriptors using reflection on the underlying class.
 * ReflectionDescriptorFactory.buildClassDescriptor() is the only public method
 * here.
 */
public class ReflectionDescriptorFactory implements DescriptorFactory
{

	protected static final Log LOG = LogFactory.getLog(ReflectionDescriptorFactory.class);

	private final Collection<String> propertyExcludes;

	private static List<String> methodExcludes = new ArrayList<String>();

	static
	{
		methodExcludes.add("shouldExclude");
		methodExcludes.add("set.*");
		methodExcludes.add("get.*");
		methodExcludes.add("is.*");
		methodExcludes.add("equals");
		methodExcludes.add("wait");
		methodExcludes.add("toString");
		methodExcludes.add("notify.*");
		methodExcludes.add("hashCode");
	}

	public ReflectionDescriptorFactory(final Collection<String> propertyExcludes)
	{
		this.propertyExcludes = propertyExcludes;
	}

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
			BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
			// Note there's various places and ways to uncamelcase the display name. However
			// we don't want to un-camelcase the possibly customized displayName
			// Also, because Introspector uses static methods, it's less clean to replace it 
			// with a custom implementation. Proxy doesn't scale well and an aspect would 
			// only work if it's run. So decided to deal with uncamelcasing displayname here
			beanDescriptor.setDisplayName(Utils.unCamelCase(beanDescriptor.getDisplayName()) );
			BeanUtils.copyProperties(descriptor, beanInfo.getBeanDescriptor());
			descriptor.setPropertyDescriptors(buildPropertyDescriptors(type,beanInfo, descriptor));
			descriptor.setMethodDescriptors(buildMethodDescriptors(type, beanInfo, descriptor));
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
			if (!isExcluded(beanPropDescriptor.getName(), propertyExcludes))
			{
				beanPropDescriptor.setDisplayName(Utils.unCamelCase(beanPropDescriptor.getDisplayName()) );
				
				Class<?> propertyType = beanPropDescriptor.getPropertyType();
				TrailsPropertyDescriptor propDescriptor = new TrailsPropertyDescriptor(beanType,propertyType);
				BeanUtils.copyProperties(propDescriptor, beanPropDescriptor);
				result.add(propDescriptor);
			}
		}
		return result;
	}

	protected ArrayList<IMethodDescriptor> buildMethodDescriptors(Class type, BeanInfo beanInfo,
																  IClassDescriptor parentClassDescriptor)
			throws Exception
	{
		ArrayList<IMethodDescriptor> result = new ArrayList<IMethodDescriptor>();
		for (MethodDescriptor beanMethodDescriptor : beanInfo.getMethodDescriptors())
		{
			if (!isExcluded(beanMethodDescriptor.getMethod().getName(), methodExcludes))
			{
				TrailsMethodDescriptor methodDescriptor = new TrailsMethodDescriptor(type,
						beanMethodDescriptor.getMethod().getName(), beanMethodDescriptor.getMethod().getReturnType(),
						beanMethodDescriptor.getMethod().getParameterTypes());
				methodDescriptor.setDisplayName(Utils.unCamelCase(beanMethodDescriptor.getDisplayName()));
				result.add(methodDescriptor);
			}
		}
		return result;
	}

	protected boolean isExcluded(String name, Collection<String> excludes)
	{
		for (String exclude : excludes)
		{
			if (name.matches(exclude))
			{
				return true;
			}
		}

		return false;
	}
}
