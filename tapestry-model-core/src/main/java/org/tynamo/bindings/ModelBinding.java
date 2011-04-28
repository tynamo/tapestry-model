package org.tynamo.bindings;

import org.apache.tapestry5.Binding;

import java.lang.annotation.Annotation;

/**
 * Binding to use when you don't know the final type of the property bound.
 * The bindingType will be computed on the fly based on the binding actual value.
 *
 */
public class ModelBinding implements Binding
{
	private final Binding binding;

	public ModelBinding(Binding binding)
	{
		this.binding = binding;
	}

	public Class getBindingType()
	{
		Object object = binding.get();
		if (object == null) throw new NullPointerException("ModelBinding's value can't be null!");
		return object.getClass();
	}

	public Object get()
	{
		return binding.get();
	}

	public void set(Object value)
	{
		binding.set(value);
	}

	public boolean isInvariant()
	{
		return binding.isInvariant();
	}

	public <T extends Annotation> T getAnnotation(Class<T> tClass)
	{
		return binding.getAnnotation(tClass);
	}
}