package org.tynamo.model.elasticsearch.annotations.handlers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.annotation.handlers.DescriptorAnnotationHandler;
import org.tynamo.model.elasticsearch.annotations.ElasticSearchField;
import org.tynamo.model.elasticsearch.annotations.ElasticSearchable;
import org.tynamo.model.elasticsearch.descriptor.ElasticSearchExtension;
import org.tynamo.model.elasticsearch.descriptor.ElasticSearchFieldDescriptor;
import org.tynamo.model.elasticsearch.mapping.FieldMapper;
import org.tynamo.model.elasticsearch.mapping.MapperFactory;
import org.tynamo.model.elasticsearch.util.ReflectionUtil;

public class ElasticSearchAnnotationHandler implements
	DescriptorAnnotationHandler<ElasticSearchable, TynamoClassDescriptor>
{
	private PropertyAccess propertyAccess;
	private MapperFactory mapperFactory;

	public ElasticSearchAnnotationHandler(MapperFactory mapperFactory, PropertyAccess propertyAccess) {
		this.mapperFactory = mapperFactory;
		this.propertyAccess = propertyAccess;
	}

	public void decorateFromAnnotation(final ElasticSearchable annotation, TynamoClassDescriptor descriptor)
	{
		descriptor.addExtension(ElasticSearchExtension.class, new ElasticSearchExtension(descriptor, propertyAccess,
			getMapping(descriptor.getBeanType())));

		ClassPropertyAdapter adapter = propertyAccess.getAdapter(descriptor.getBeanType());

		for (TynamoPropertyDescriptor propertyDescriptor : descriptor.getPropertyDescriptors()) {
			PropertyAdapter propertyAdapter = adapter.getPropertyAdapter(propertyDescriptor.getName());
			if (propertyAdapter == null) continue;
			if (propertyAdapter.getAnnotation(ElasticSearchField.class) == null) continue;

			ElasticSearchFieldDescriptor fieldDescriptor = new ElasticSearchFieldDescriptor(propertyAdapter.getField() == null ? propertyAdapter.getReadMethod() : propertyAdapter.getField());
			propertyDescriptor.addExtension(ElasticSearchFieldDescriptor.class, fieldDescriptor);
		}
	}

	// FIXME Refactor the play-based field mappings to use Tynamo's Propertydescriptor extensions
	@Deprecated
	protected List<FieldMapper> getMapping(Class clazz) {
		List<FieldMapper> mapping = new ArrayList<FieldMapper>();

		List<Field> indexableFields = ReflectionUtil.getFieldsWithAnnotation(clazz, ElasticSearchField.class);

		for (Field field : indexableFields) {

			FieldMapper mapper = mapperFactory.getMapper(field);
			mapping.add(mapper);
		}

		return mapping;
	}

}
