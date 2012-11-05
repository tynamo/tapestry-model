package org.tynamo.model.elasticsearch.mapping.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.model.elasticsearch.annotations.ElasticSearchEmbedded;
import org.tynamo.model.elasticsearch.mapping.FieldMapper;
import org.tynamo.model.elasticsearch.mapping.MapperFactory;
import org.tynamo.model.elasticsearch.mapping.MappingException;
import org.tynamo.model.elasticsearch.mapping.MappingUtil;

/**
 * Field mapper for collection type; maps to array by default
 *
 * @param <M>
 *          the generic model type which owns this field
 */
public class CollectionFieldMapper<M> extends AbstractFieldMapper<M> {

	private final boolean nestedMode;
	private final String type;
	private final List<FieldMapper<Object>> fields;

	public CollectionFieldMapper(MapperFactory factory, TynamoPropertyDescriptor field, String prefix) {
		super(field, prefix);

		if (!Collection.class.isAssignableFrom(field.getPropertyType())) {
			throw new MappingException("field must be of Collection type");
		}

		// FIXME not support for ElasticSearchEmbedded at the moment
		// ElasticSearchEmbedded embed = field.getAnnotation(ElasticSearchEmbedded.class);
		ElasticSearchEmbedded embed = null;
		nestedMode = (embed != null);

		// Detect object type in collection
		type = MappingUtil.detectFieldType(getCollectionType());

		// Find fields to use for embedded objects
		if (nestedMode) {
			Class<?> itemClass = getCollectionType();
			List<Field> fieldsToIndex = EmbeddedFieldMapper.getFieldsToIndex(itemClass, embed);
			fields = new ArrayList<FieldMapper<Object>>();

			for (Field embeddedField : fieldsToIndex) {
				// FIXME not support for ElasticSearchEmbedded at the moment
				// fields.add(factory.getMapper(embeddedField));
			}
		} else {
			fields = null;
		}
	}

	private Class<?> getCollectionType() {
		return ((CollectionDescriptor) field).getElementType();
		// ParameterizedType type = (ParameterizedType) field.getGenericType();
		// return (Class<?>) type.getActualTypeArguments()[0];
	}

	@Override
	public void addToMapping(XContentBuilder builder) throws IOException {
		String indexFieldName = getIndexField();

		if (nestedMode) {
			// Embedded mode
			builder.startObject(indexFieldName);
			builder.startObject("properties");
			for (FieldMapper<?> mapper : fields) {
				mapper.addToMapping(builder);
			}
			builder.endObject();
			builder.endObject();
		} else {
			// Flat mode (array of primitives)
			MappingUtil.addField(builder, indexFieldName, type, meta);
		}
	}

	@Override
	public void addToDocument(Object o, XContentBuilder builder) throws IOException {
		String indexFieldName = getIndexField();
		Collection<?> value = (Collection<?>) o;

		if (value != null) {
			builder.startArray(indexFieldName);

			if (nestedMode) {
				// Embedded mode uses mapping
				for (Object object : (Collection<?>) value) {
					builder.startObject();
					for (FieldMapper<Object> mapper : fields) {
						mapper.addToDocument(object, builder);
					}
					builder.endObject();
				}
			} else {
				boolean isStringType = type.equals("string");

				// Flat mode uses primitive values or toString
				for (Object object : (Collection<?>) value) {
					// Use toString for string type
					if (isStringType) {
						builder.value(object.toString());
					} else {
						builder.value(object);
					}
				}
			}

			builder.endArray();
		}
	}
}
