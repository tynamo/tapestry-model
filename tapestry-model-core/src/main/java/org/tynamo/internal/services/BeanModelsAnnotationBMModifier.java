package org.tynamo.internal.services;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.internal.beaneditor.BeanModelUtils;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.tynamo.descriptor.annotation.beaneditor.BeanModel;
import org.tynamo.services.BeanModelModifier;

import java.util.HashMap;
import java.util.Map;

public class BeanModelsAnnotationBMModifier implements BeanModelModifier
{
	private Map<Class, Map<String, BeanModel>> beanModels = CollectionFactory.newConcurrentMap();

	@Override
	public boolean modify(org.apache.tapestry5.beaneditor.BeanModel<?> dataModel, String key) {

		BeanModel bm = get(dataModel.getBeanType(), key);
		if (bm == null) return false;

		String exclude = StringUtils.isNotEmpty(bm.exclude()) ? bm.exclude() : null;
		String include = StringUtils.isNotEmpty(bm.include()) ? bm.include() : null;
		String reorder = StringUtils.isNotEmpty(bm.reorder()) ? bm.reorder() : null;

		BeanModelUtils.modify(dataModel, null, include, exclude, reorder);
		return true;
	}

	private BeanModel get(Class clazz, String key)
	{
		if (beanModels.containsKey(clazz))
		{
			return beanModels.get(clazz).get(key);
		}

		return null;
	}

	public void put(BeanModel bm)
	{
		Map<String, BeanModel> map;

		if (beanModels.containsKey(bm.beanType()))
		{
			map = beanModels.get(bm.beanType());
		} else
		{
			map = new HashMap<String, BeanModel>();
			beanModels.put(bm.beanType(), map);
		}

		map.put(bm.context(), bm);
	}

	/**
	 * Clears the cache of models
	 */
	public synchronized void clearCache()
	{
		beanModels.clear();
	}

}
