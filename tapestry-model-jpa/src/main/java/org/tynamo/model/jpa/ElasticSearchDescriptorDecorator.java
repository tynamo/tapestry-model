package org.tynamo.model.jpa;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.slf4j.Logger;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.decorators.DescriptorDecorator;
import org.tynamo.model.elasticsearch.annotations.ElasticSearchField;
import org.tynamo.model.elasticsearch.annotations.ElasticSearchable;

public class ElasticSearchDescriptorDecorator implements DescriptorDecorator {
	private final Logger logger;
	private final PropertyAccess propertyAccess;

	public ElasticSearchDescriptorDecorator(Logger logger, PropertyAccess propertyAccess) {
		this.logger = logger;
		this.propertyAccess = propertyAccess;
	}

	@Override
	public TynamoClassDescriptor decorate(TynamoClassDescriptor descriptor) {
		Class type = descriptor.getBeanType();

		if (!type.isAnnotationPresent(ElasticSearchable.class)) return descriptor;
		descriptor.setSearchable(true);

		ClassPropertyAdapter classAdapter = propertyAccess.getAdapter(type);
		// the annotation were taken from play/ElasticSearchModule but we could integrate them to use our own annotation handling machinery
		for (TynamoPropertyDescriptor propertyDescriptor : descriptor.getPropertyDescriptors())
			try {
				if (classAdapter.getPropertyAdapter(propertyDescriptor.getName()).getAnnotation(ElasticSearchField.class) != null)
					propertyDescriptor.setSearchable(true);
			} catch (Exception ex) {
				logger.warn(ExceptionUtils.getRootCauseMessage(ex));
			}
		return descriptor;
	}
}
