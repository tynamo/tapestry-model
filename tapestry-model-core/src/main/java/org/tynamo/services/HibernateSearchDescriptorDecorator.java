package org.tynamo.services;

import org.hibernate.search.annotations.Indexed;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.decorators.DescriptorDecorator;

public class HibernateSearchDescriptorDecorator implements DescriptorDecorator {

	@Override
	public TynamoClassDescriptor decorate(TynamoClassDescriptor descriptor) {
		Class type = descriptor.getBeanType();
		
		if (type.isAnnotationPresent(Indexed.class)) descriptor.setSearchable(true);
		return descriptor;
	}

}
