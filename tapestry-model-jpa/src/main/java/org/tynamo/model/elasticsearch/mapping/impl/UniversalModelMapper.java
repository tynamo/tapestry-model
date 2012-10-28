package org.tynamo.model.elasticsearch.mapping.impl;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.tynamo.model.elasticsearch.mapping.ModelMapper;

/**
 * Mapper implmementation used for retrieval from multiple indices
 *
 * @author filip.stefanak@gmail.com
 *
 */
public class UniversalModelMapper implements ModelMapper {

	@Override
	public Class getModelClass() {
		return Object.class;
	}

	@Override
	public String getIndexName() {
		return "_all";
	}

	@Override
	public String getTypeName() {
		return "_all";
	}

	@Override
	public String getDocumentId(final Object model) {
		return "_all";
	}

	@Override
	public void addMapping(final XContentBuilder builder) throws IOException {
		throw new UnsupportedOperationException("Unsupported call to UniversalModelMapper");
	}

	@Override
	public void addModel(final Object model, final XContentBuilder builder) throws IOException {
		throw new UnsupportedOperationException("Unsupported call to UniversalModelMapper");
	}

	@Override
	public Object createModel(final Map<String, Object> map) {
		throw new UnsupportedOperationException("Model mapping is not supported with UniversalModelMapper");
	}

}
