package org.tynamo.bindings;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.internal.bindings.PropBindingFactory;
import org.apache.tapestry5.internal.services.StringInterner;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.PropertyConduitSource;

public class ModelBindingFactory extends PropBindingFactory implements BindingFactory
{

	public ModelBindingFactory(PropertyConduitSource propertyConduitSource, StringInterner interner)
	{
		super(propertyConduitSource, interner);
	}

	@Override
	public Binding newBinding(String description, ComponentResources container, ComponentResources component,
	                          String expression, Location location)
	{
		return new ModelBinding(super.newBinding(description, container, component, expression, location));
	}
}