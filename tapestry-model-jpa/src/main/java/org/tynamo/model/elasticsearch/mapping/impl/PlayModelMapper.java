package org.tynamo.model.elasticsearch.mapping.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.tynamo.model.elasticsearch.annotations.ElasticSearchIgnore;
import org.tynamo.model.elasticsearch.annotations.ElasticSearchTtl;
import org.tynamo.model.elasticsearch.annotations.ElasticSearchable;
import org.tynamo.model.elasticsearch.mapping.FieldMapper;
import org.tynamo.model.elasticsearch.mapping.MapperFactory;
import org.tynamo.model.elasticsearch.mapping.ModelMapper;
import org.tynamo.model.elasticsearch.util.ReflectionUtil;

/**
 * ModelMapper for play.db.Model subclasses.
 * 
 * @param <M>
 *          the model type
 */
public class PlayModelMapper implements ModelMapper {

	/** The play-specific fields to ignore. */
	private static List<String> IGNORE_FIELDS = new ArrayList<String>();
	static {
		IGNORE_FIELDS.add("avoidCascadeSaveLoops");
		IGNORE_FIELDS.add("willBeSaved");
		IGNORE_FIELDS.add("serialVersionId");
		IGNORE_FIELDS.add("serialVersionUID");
	}

	private final Class clazz;
	private final ElasticSearchable meta;
	private final List<FieldMapper> mapping;

	public PlayModelMapper(MapperFactory factory, Class clazz) {
		Validate.notNull(clazz, "Clazz cannot be null");
		this.clazz = clazz;
		this.meta = (ElasticSearchable) clazz.getAnnotation(ElasticSearchable.class);

		// Create mapping
		mapping = getMapping(factory, clazz);
	}

	static boolean shouldIgnoreField(Field field) {
		String name = field.getName();

		return StringUtils.isBlank(name) || IGNORE_FIELDS.contains(name)
				|| shouldIgnoreJPAField(field);
	}

	/**
	 * Checks if a field should be ignored based on JPA-specifics
	 * 
	 * @param field
	 *          the field to check
	 * @return true if the field should be ignored, false otherwise
	 */
	static boolean shouldIgnoreJPAField(Field field) {
		return field.isAnnotationPresent(Transient.class);
	}

	static boolean userRequestedIgnoreField(Field field) {
		return field.isAnnotationPresent(ElasticSearchIgnore.class);
	}

	/**
	 * Gets a list of {@link FieldMapper}s for the given model class
	 * 
	 * @param <M>
	 *          the model type
	 * @param factory
	 *          the mapper factory
	 * @param clazz
	 *          the model class
	 * @return the list of FieldMappers
	 */
	private static final List<FieldMapper> getMapping(MapperFactory factory,
 Class clazz) {
		List<FieldMapper> mapping = new ArrayList<FieldMapper>();

		List<Field> indexableFields = ReflectionUtil.getAllFields(clazz);

		for (Field field : indexableFields) {

			// Exclude fields on our ignore list
			if (shouldIgnoreField(field) || userRequestedIgnoreField(field)) {
				continue;
			}

			FieldMapper mapper = factory.getMapper(field);
			mapping.add(mapper);
		}

		return mapping;
	}

	@Override
	public Class getModelClass() {
		return clazz;
	}

	@Override
	public String getIndexName() {
		if (meta.indexName().length() > 0) {
			return meta.indexName();
		} else {
			return getTypeName();
		}
	}

	@Override
	public String getTypeName() {
		return clazz.getName().toLowerCase().trim().replace('.', '_');
	}

	@Override
	public String getDocumentId(Object model) {
		// FIXME how to return docid?
		// return String.valueOf(model._key());
		return null;
	}

	@Override
	public void addMapping(XContentBuilder builder) throws IOException {
		builder.startObject(getTypeName());
		if (clazz.isAnnotationPresent(ElasticSearchTtl.class)) {
			String ttlValue = ((ElasticSearchTtl) clazz.getAnnotation(ElasticSearchTtl.class)).value();
			builder.startObject("_ttl");
			builder.field("enabled", true);
			builder.field("default", ttlValue);
			builder.endObject();
		}
		builder.startObject("properties");

		for (FieldMapper field : mapping) {
			field.addToMapping(builder);
		}

		builder.endObject();
		builder.endObject();
	}

	@Override
	public void addModel(Object model, XContentBuilder builder) throws IOException {
		builder.startObject();

		for (FieldMapper field : mapping) {
			field.addToDocument(model, builder);
		}

		builder.endObject();
	}

	@Override
	public Object createModel(Map<String, Object> map) {
		Object model = ReflectionUtil.newInstance(clazz);

		for (FieldMapper field : mapping) {
			field.inflate(model, map);
		}

		return model;
	}

}
