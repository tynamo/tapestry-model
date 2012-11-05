package org.tynamo.model.elasticsearch.mapping.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.model.elasticsearch.annotations.ElasticSearchEmbedded;
import org.tynamo.model.elasticsearch.annotations.ElasticSearchEmbedded.Mode;
import org.tynamo.model.elasticsearch.mapping.FieldMapper;
import org.tynamo.model.elasticsearch.mapping.MapperFactory;
import org.tynamo.model.elasticsearch.mapping.MappingException;
import org.tynamo.model.elasticsearch.util.ReflectionUtil;

/**
 * Field mapper for embedded objects
 *
 * @param <M>
 *          the generic model type which owns this field
 */
public class EmbeddedFieldMapper<M> extends AbstractFieldMapper<M> {

	private final ElasticSearchEmbedded embed;
	private final List<FieldMapper<Object>> fields;

	public EmbeddedFieldMapper(MapperFactory factory, TynamoPropertyDescriptor field, String prefix) {
		super(field, prefix);

		// FIXME no support for EmbeddedFieldMapper at the moment
		// embed = field.getAnnotation(ElasticSearchEmbedded.class);
		embed = null;

		// Set correct prefix in case we are in embedded mode
		String embedPrefix = null;
		if (embed.mode() == Mode.embedded) {
			if (embed.prefix().length() > 0) {
				embedPrefix = prefix(embed.prefix());
			} else {
				embedPrefix = getFieldName() + ".";
			}
		}

		// Add fieldmappers for embedded fields
		fields = new ArrayList<FieldMapper<Object>>();
		for (Field embeddedField : getFieldsToIndex(field.getPropertyType(), embed)) {
			// FIXME no support for EmbeddedFieldMapper at the moment
			// fields.add(factory.getMapper(embeddedField, embedPrefix));
		}
	}

	static List<Field> getFieldsToIndex(Class<?> clazz, ElasticSearchEmbedded meta) {
		@SuppressWarnings("unchecked")
		List<String> fieldsToIndex = Arrays.asList(meta.fields());
		List<Field> clazzFields = ReflectionUtil.getAllFields(clazz);
		List<Field> fields = new ArrayList<Field>();

		// Make sure the user has not requested unknown fields
		if (fieldsToIndex.size() > 0) {
			for (String fieldName : fieldsToIndex) {
				boolean knownField = false;
				for (Field clazzField : clazzFields) {
					if (clazzField.getName().equals(fieldName)) {
						knownField = true;
						break;
					}
				}

				if (!knownField) {
					throw new MappingException("Unknown field specified in " + meta);
				}
			}
		}

		// Set up fields
		for (Field embeddedField : clazzFields) {
			// // FIXME questionable, decide later how to deal properly
			// // shouldIgnoreField is true when @Transient
			// if (PlayModelMapper.shouldIgnoreField(embeddedField)) {
			// continue;
			// }
			//
			// // If no fields were requested and it's marked, ignore it
			// // userRequestedIgnoreField is true when @ElasticSearchIgnore
			// if (fieldsToIndex.size() == 0
			// && PlayModelMapper.userRequestedIgnoreField(embeddedField)) {
			// continue;
			// }

			// If specific fields are requested, and this is not one of them,
			// ignore it
			if (fieldsToIndex.size() > 0 && !fieldsToIndex.contains(embeddedField.getName())) {
				continue;
			}

			// Add it
			fields.add(embeddedField);
		}

		return fields;
	}

	@Override
	public void addToMapping(XContentBuilder builder) throws IOException {
		String indexFieldName = getIndexField();

		switch (embed.mode()) {
		case embedded:
			for (FieldMapper<?> mapper : fields) {
				mapper.addToMapping(builder);
			}
			break;
		case object:
		case nested:
			builder.startObject(indexFieldName);
			builder.field("type", embed.mode().toString());
			builder.startObject("properties");
			for (FieldMapper<?> mapper : fields) {
				mapper.addToMapping(builder);
			}
			builder.endObject();
			builder.endObject();
			break;
		}
	}

	@Override
	public void addToDocument(Object value, XContentBuilder builder) throws IOException {
		String name = getIndexField();

		if (value != null) {
			switch (embed.mode()) {
			case embedded:
				for (FieldMapper<Object> mapper : fields) {
					mapper.addToDocument(value, builder);
				}
				break;
			case object:
			case nested:
				builder.startObject(name);
				for (FieldMapper<Object> mapper : fields) {
					mapper.addToDocument(value, builder);
				}
				builder.endObject();
				break;
			}
		}
	}
}
