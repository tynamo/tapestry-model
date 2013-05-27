package org.tynamo.examples.simple.services;

import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticMethod;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;
import org.tynamo.examples.simple.CustomCommitAfter;

public class CommitAfterWorker implements ComponentClassTransformWorker2
{
	private MethodAdvice advice;

	public CommitAfterWorker(MethodAdvice advice)
	{
		this.advice = advice;
	}

	public void transform(PlasticClass plasticClass, TransformationSupport support, MutableComponentModel model)
	{
		for (PlasticMethod method : plasticClass.getMethodsWithAnnotation(CustomCommitAfter.class))
		{
			method.addAdvice(advice);
		}
	}
}
