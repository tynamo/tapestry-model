package org.tynamo.descriptor.decorators;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.beaneditor.NonVisual;
import org.apache.tapestry5.beaneditor.ReorderProperties;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.extension.BeanModelExtension;

public class TapestryDecorator implements DescriptorDecorator
{

	PropertyAccess propertyAccess;

	public TapestryDecorator(PropertyAccess propertyAccess)
	{
		this.propertyAccess = propertyAccess;
	}

	public TynamoClassDescriptor decorate(TynamoClassDescriptor descriptor)
	{
		ClassPropertyAdapter adapter = propertyAccess.getAdapter(descriptor.getBeanType());

		for (final String propertyName : adapter.getPropertyNames())
		{
			PropertyAdapter pa = adapter.getPropertyAdapter(propertyName);

			if (pa.getAnnotation(NonVisual.class) != null)
			{
				descriptor.getPropertyDescriptor(pa.getName()).setNonVisual(true);
			} else if (pa.getAnnotation(ReorderProperties.class) != null)
			{
				String reorder = pa.getAnnotation(ReorderProperties.class).value();

				BeanModelExtension beanModelExtension = BeanModelExtension.obtainBeanModelExtension(descriptor);

				if (StringUtils.isNotEmpty(reorder))
					beanModelExtension.setReorderPropertyNames(reorder);
			}

		}

		return descriptor;
	}
}
