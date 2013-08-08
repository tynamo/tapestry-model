package org.tynamo.internal.services;

import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;
import org.tynamo.descriptor.annotation.beaneditor.BeanModel;
import org.tynamo.descriptor.annotation.beaneditor.BeanModels;

public class BeanModelWorker implements ComponentClassTransformWorker2
{
	private final BeanModelsAnnotationBMModifier modifier;
	private final ComponentClassResolver componentClassResolver;

	public BeanModelWorker(BeanModelsAnnotationBMModifier modifier, ComponentClassResolver componentClassResolver)
	{
		this.modifier = modifier;
		this.componentClassResolver = componentClassResolver;
	}

	@Override
	public void transform(PlasticClass plasticClass, TransformationSupport support, MutableComponentModel model)
	{
		if (plasticClass.hasAnnotation(BeanModels.class))
		{
			String pageName = componentClassResolver.resolvePageClassNameToPageName(plasticClass.getClassName());
			String canonicalized = componentClassResolver.canonicalizePageName(pageName);

			BeanModels bms = plasticClass.getAnnotation(BeanModels.class);
			for (BeanModel bm : bms.value())
			{
				modifier.put(canonicalized, bm);
			}
		}
	}
}
