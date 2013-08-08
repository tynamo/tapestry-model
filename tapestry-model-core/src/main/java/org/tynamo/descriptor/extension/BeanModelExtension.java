package org.tynamo.descriptor.extension;

import org.tynamo.PageType;
import org.tynamo.descriptor.Descriptor;

import java.util.HashMap;
import java.util.Map;

public class BeanModelExtension implements DescriptorExtension
{

	private final static String defaultKey = PageType.DEFAULT.getContextKey();

	private Map<String, String> reorderMap = new HashMap<String, String>();
	private Map<String, String> includeMap = new HashMap<String, String>();
	private Map<String, String> excludeMap = new HashMap<String, String>();

	private BeanModelExtension(){}

	public String getReorderPropertyNames(String contextKey)
	{
		return reorderMap.containsKey(contextKey) ? reorderMap.get(contextKey) : getReorderPropertyNames();
	}

	public String getIncludePropertyNames(String contextKey)
	{
		return includeMap.containsKey(contextKey) ? includeMap.get(contextKey) : getIncludePropertyNames();
	}

	public String getExcludePropertyNames(String contextKey)
	{
		return excludeMap.containsKey(contextKey) ? excludeMap.get(contextKey) : getExcludePropertyNames();
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


	public String getReorderPropertyNames()
	{
		return reorderMap.get(defaultKey);
	}

	public String getIncludePropertyNames()
	{
		return includeMap.get(defaultKey);
	}

	public String getExcludePropertyNames()
	{
		return excludeMap.get(defaultKey);
	}

	public void setReorderPropertyNames(String properties)
	{
		reorderMap.put(defaultKey, properties);
	}

	public boolean hasModifiersForKey(String key) {
		return reorderMap.containsKey(key) ||
				includeMap.containsKey(key) ||
				excludeMap.containsKey(key) ||
				reorderMap.containsKey(defaultKey) ||
				includeMap.containsKey(defaultKey) ||
				excludeMap.containsKey(defaultKey);
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
