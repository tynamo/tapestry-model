package org.tynamo.model.elasticsearch.annotations.handlers;

import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.annotation.handlers.DescriptorAnnotationHandler;
import org.tynamo.model.elasticsearch.annotations.ElasticSearchField;
import org.tynamo.model.elasticsearch.annotations.ElasticSearchMultiField;
import org.tynamo.model.elasticsearch.annotations.ElasticSearchable;
import org.tynamo.model.elasticsearch.descriptor.ElasticSearchExtension;
import org.tynamo.model.elasticsearch.descriptor.ElasticSearchFieldDescriptor;

public class ElasticSearchAnnotationHandler implements DescriptorAnnotationHandler<ElasticSearchable, TynamoClassDescriptor> {
	private PropertyAccess propertyAccess;

	public ElasticSearchAnnotationHandler(PropertyAccess propertyAccess) {
		this.propertyAccess = propertyAccess;
	}

	public void decorateFromAnnotation(final ElasticSearchable annotation, TynamoClassDescriptor descriptor) {
		// TODO we should make ElasticSearchable type annotation optional - but cannot here as this handler is
		// triggered by the said annotation. Would it be worth implementing a specific property annotation handler?
		descriptor.addExtension(ElasticSearchExtension.class, new ElasticSearchExtension(descriptor, propertyAccess));
		descriptor.setSearchable(true);

		ClassPropertyAdapter adapter = propertyAccess.getAdapter(descriptor.getBeanType());

		for (TynamoPropertyDescriptor propertyDescriptor : descriptor.getPropertyDescriptors()) {
			PropertyAdapter propertyAdapter = adapter.getPropertyAdapter(propertyDescriptor.getName());
			if (propertyAdapter == null) continue;
			if (propertyAdapter.getAnnotation(ElasticSearchField.class) != null) {
				propertyDescriptor.addExtension(ElasticSearchFieldDescriptor.class,
						new ElasticSearchFieldDescriptor(propertyAdapter.getAnnotation(ElasticSearchField.class)));
			} else if (propertyAdapter.getAnnotation(ElasticSearchMultiField.class) != null) {
				propertyDescriptor.addExtension(ElasticSearchFieldDescriptor.class,
						new ElasticSearchFieldDescriptor(propertyAdapter.getAnnotation(ElasticSearchMultiField.class).value()));
			}
		}
	}
}
