package org.tynamo.model.elasticsearch.mapping.impl;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;

/**
 * Mapper implmementation used for retrieval from multiple indices
 *
 * @author filip.stefanak@gmail.com
 *
 */
public class UniversalModelMapper /* implement ModelMapper */{

	public Class getModelClass() {
		return Object.class;
	}

	public String getIndexName() {
		return "_all";
	}

	public String getTypeName() {
		return "_all";
	}

	public String getDocumentId(final Object model) {
		return "_all";
	}

	public void addMapping(final XContentBuilder builder) throws IOException {
		throw new UnsupportedOperationException("Unsupported call to UniversalModelMapper");
	}

	public void addModel(final Object model, final XContentBuilder builder) throws IOException {
		throw new UnsupportedOperationException("Unsupported call to UniversalModelMapper");
	}

	public Object createModel(final Map<String, Object> map) {
		throw new UnsupportedOperationException("Model mapping is not supported with UniversalModelMapper");
	}

}
