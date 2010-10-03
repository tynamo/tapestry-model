package org.tynamo.descriptor.extension;

import org.tynamo.internal.InternalConstants;
import org.tynamo.descriptor.Descriptor;
import org.tynamo.util.BeanModelUtils;

import java.util.HashMap;
import java.util.Map;

public class BeanModelExtension implements DescriptorExtension
{

	private Map<String, String> reorderMap = new HashMap<String, String>();
	private Map<String, String> includeMap = new HashMap<String, String>();
	private Map<String, String> excludeMap = new HashMap<String, String>();

	private BeanModelExtension()
	{
	}

	public String getReorderPropertyNames(String contextKey)
	{
		return reorderMap.containsKey(contextKey) ? reorderMap.get(contextKey) : getReorder();
	}

	public String getIncludePropertyNames(String contextKey)
	{
		return includeMap.containsKey(contextKey) ? includeMap.get(contextKey) : getInclude();
	}

	public String getExcludePropertyNames(String contextKey)
	{
		return excludeMap.containsKey(contextKey) ? excludeMap.get(contextKey) : getExclude();
	}

	public void setReorderPropertyNames(String contextKey, String properties)
	{
		reorderMap.put(contextKey, properties);
	}

	public void setIncludePropertyNames(String contextKey, String properties)
	{
		includeMap.put(contextKey, properties);
	}

	public void setExcludePropertyNames(String contextKey, String properties)
	{
		excludeMap.put(contextKey, properties);
	}


	public String getReorder()
	{
		return reorderMap.get(InternalConstants.DEFAULT_BEANMODEL_CONTEXT_KEY);
	}

	public String getInclude()
	{
		return includeMap.get(InternalConstants.DEFAULT_BEANMODEL_CONTEXT_KEY);
	}

	public String getExclude()
	{
		return excludeMap.get(InternalConstants.DEFAULT_BEANMODEL_CONTEXT_KEY);
	}

	public void addToIncludeMap(String contextKey, String newProperty)
	{
		String includePropertyNames = includeMap.get(contextKey);
		includePropertyNames = includePropertyNames == null ? newProperty : BeanModelUtils.join(includePropertyNames, newProperty);
		setIncludePropertyNames(contextKey, includePropertyNames);
	}

	public void addToExcludeMap(String contextKey, String newProperty)
	{
		String excludePropertyNames = excludeMap.get(contextKey);
		excludePropertyNames = excludePropertyNames == null ? newProperty : BeanModelUtils.join(excludePropertyNames, newProperty);
		setExcludePropertyNames(contextKey, excludePropertyNames);
	}

	public static BeanModelExtension obtainBeanModelExtension(Descriptor descriptor)
	{
		BeanModelExtension beanModelExtension = descriptor.getExtension(BeanModelExtension.class);
		if (beanModelExtension == null)
		{
			beanModelExtension = new BeanModelExtension();
			descriptor.addExtension(BeanModelExtension.class, beanModelExtension);
		}

		return beanModelExtension;
	}
}
