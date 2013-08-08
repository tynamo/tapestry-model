package org.tynamo.internal.services;

import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;
import org.tynamo.descriptor.annotation.beaneditor.BeanModel;
import org.tynamo.descriptor.annotation.beaneditor.BeanModels;

public class BeanModelWorker implements ComponentClassTransformWorker2
{
	private BeanModelsAnnotationBMModifier beanModelProvider;

	public BeanModelWorker(BeanModelsAnnotationBMModifier beanModelProvider)
	{
		this.beanModelProvider = beanModelProvider;
	}

	@Override
	public void transform(PlasticClass plasticClass, TransformationSupport support, MutableComponentModel model)
	{
		if (plasticClass.hasAnnotation(BeanModels.class))
		{
			BeanModels bms = plasticClass.getAnnotation(BeanModels.class);
			for (BeanModel bm : bms.value())
			{
				beanModelProvider.put(bm);
			}
		}
	}
}
