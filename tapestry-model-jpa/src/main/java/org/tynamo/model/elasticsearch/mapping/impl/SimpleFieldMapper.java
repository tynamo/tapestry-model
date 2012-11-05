package org.tynamo.model.elasticsearch.mapping.impl;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.model.elasticsearch.mapping.MappingUtil;

/**
 * Field mapper for simple, single-valued types
 * 
 * @param <M>
 *          the generic model type which owns this field
 */
public class SimpleFieldMapper<M> extends AbstractFieldMapper<M> {

	public SimpleFieldMapper(TynamoPropertyDescriptor field, String prefix) {
		super(field, prefix);
	}

	@Override
	public void addToMapping(XContentBuilder builder) throws IOException {
		String field = getIndexField();
		String type = getIndexType();

		MappingUtil.addField(builder, field, type, meta);
	}

	@Override
	public void addToDocument(Object value, XContentBuilder builder) throws IOException {
		String field = getIndexField();
		if (value != null) {
			builder.field(field, value);
		}
	}
}
