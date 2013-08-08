package org.tynamo.internal.services;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.internal.beaneditor.BeanModelUtils;
import org.apache.tapestry5.ioc.annotations.PostInjection;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.ComponentClasses;
import org.apache.tapestry5.services.InvalidationEventHub;
import org.apache.tapestry5.services.InvalidationListener;
import org.apache.tapestry5.services.RequestGlobals;
import org.tynamo.PageType;
import org.tynamo.descriptor.annotation.beaneditor.BeanModel;
import org.tynamo.services.BeanModelModifier;

import java.util.HashMap;
import java.util.Map;

public class BeanModelsAnnotationBMModifier implements BeanModelModifier, InvalidationListener
{
	private final static String defaultKey = PageType.DEFAULT.getContextKey();

	private final Map<String, Map<Class, Map<String, BeanModel>>> beanModels = CollectionFactory.newConcurrentMap();
	private final RequestGlobals globals;


	public BeanModelsAnnotationBMModifier(RequestGlobals globals)
	{
		this.globals = globals;
	}

	@Override
	public boolean modify(org.apache.tapestry5.beaneditor.BeanModel<?> dataModel, String key) {

		String pageName = globals.getActivePageName();

		BeanModel bm = get(pageName, dataModel.getBeanType(), key);
		if (bm == null) bm = get(pageName, dataModel.getBeanType(), defaultKey);
		if (bm == null) return false;

		String exclude = StringUtils.isNotEmpty(bm.exclude()) ? bm.exclude() : null;
		String include = StringUtils.isNotEmpty(bm.include()) ? bm.include() : null;
		String reorder = StringUtils.isNotEmpty(bm.reorder()) ? bm.reorder() : null;

		BeanModelUtils.modify(dataModel, null, include, exclude, reorder);
		return true;
	}

	private BeanModel get(String page, Class beanType, String key)
	{
		if (beanModels.containsKey(page))
		{
			return get(beanModels.get(page), beanType, key);
		}

		return null;
	}

	private BeanModel get(Map<Class, Map<String, BeanModel>> beanModelsInPage, Class clazz, String key)
	{
		if (beanModelsInPage.containsKey(clazz))
		{
			return beanModelsInPage.get(clazz).get(key);
		}

		return null;
	}

	public void put(String page, BeanModel bm)
	{
		Map<Class, Map<String, BeanModel>> map;

		if (beanModels.containsKey(page))
		{
			map = beanModels.get(page);
		} else
		{
			map = new HashMap<Class, Map<String, BeanModel>>();
			beanModels.put(page, map);
		}

		put(map, bm);
	}


	private void put(Map<Class, Map<String, BeanModel>> beanModelsInPage, BeanModel bm)
	{
		Map<String, BeanModel> map;

		if (beanModelsInPage.containsKey(bm.beanType()))
		{
			map = beanModelsInPage.get(bm.beanType());
		} else
		{
			map = new HashMap<String, BeanModel>();
			beanModelsInPage.put(bm.beanType(), map);
		}

		map.put(getKey(bm.context()), bm);
	}

	private String getKey(String context)
	{
		return StringUtils.isNotEmpty(context) ? context : defaultKey;
	}

	/**
	 * Clears the cache of models
	 */
	@Override
	public void objectWasInvalidated()
	{
		beanModels.clear();
	}

	@PostInjection
	public void listenForInvalidations(@ComponentClasses InvalidationEventHub hub)
	{
		hub.addInvalidationListener(this);
	}

}
