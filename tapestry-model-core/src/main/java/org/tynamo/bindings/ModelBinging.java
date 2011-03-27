package org.tynamo.bindings;

import org.apache.tapestry5.Binding;

import java.lang.annotation.Annotation;

public class ModelBinging implements Binding
{
	private final Binding binding;

	public ModelBinging(Binding binding)
	{
		this.binding = binding;
	}

	public Class getBindingType()
	{
		Object object = binding.get();
		if (object == null) throw new NullPointerException("ModelBinging's value can't be null!");
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
