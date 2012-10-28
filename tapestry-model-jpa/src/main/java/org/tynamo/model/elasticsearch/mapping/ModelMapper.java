package org.tynamo.model.elasticsearch.mapping;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;

/**
 * Maps a model to an Elastic Search index
 * 
 * @param <M>
 *          the model being mapped
 */
public interface ModelMapper {

	/**
	 * Gets the model class
	 * 
	 * @return the model class
	 */
	public Class getModelClass();

	/**
	 * Gets the index name
	 * 
	 * @return the index name
	 */
	public String getIndexName();

	/**
	 * Gets the type name
	 * 
	 * @return the type name
	 */
	public String getTypeName();

	/**
	 * Gets the document id
	 * 
	 * @param model
	 *          the model
	 * @return the model's document id
	 */
	public String getDocumentId(Object model);

	/**
	 * Adds a mapping for this model to the builder
	 * 
	 * @param builder
	 *          the builder
	 * @throws IOException
	 */
	public void addMapping(XContentBuilder builder) throws IOException;

	/**
	 * Adds an instance of this model to the builder
	 * 
	 * @param model
	 *          an instance of this model
	 * @param builder
	 *          the builder
	 * @throws IOException
	 */
	public void addModel(Object model, XContentBuilder builder) throws IOException;

	/**
	 * Inflates a new model from a map of values
	 * 
	 * @param map
	 * @return
	 */
	public Object createModel(Map<String, Object> map);
}
