package org.tynamo.model.elasticsearch.mapping;

import org.tynamo.descriptor.TynamoPropertyDescriptor;

/**
 * Factory for retrieving {@link FieldMapper}s
 */
public interface MapperFactory {
	/**
	 * Gets a {@link ModelMapper} for the specified model class
	 * 
	 * @param <M>
	 *          the model type
	 * @param clazz
	 *          the model class
	 * @throws MappingException
	 *           in case of mapping problems
	 * @return the model mapper
	 */
	// ModelMapper getMapper(Class clazz) throws MappingException;

	/**
	 * Gets a {@link FieldMapper} for the specified field
	 * 
	 * @param <M>
	 *          the model type
	 * @param field
	 *          the field
	 * @throws MappingException
	 *           in case of mapping problems
	 * @return the field mapper
	 */
	<M> FieldMapper<M> getMapper(TynamoPropertyDescriptor field) throws MappingException;

	/**
	 * Gets a {@link FieldMapper} for the specified field, using a prefix in the index
	 * 
	 * @param <M>
	 *          the model type
	 * @param field
	 *          the field
	 * @throws MappingException
	 *           in case of mapping problems
	 * @return the field mapper
	 */
	<M> FieldMapper<M> getMapper(TynamoPropertyDescriptor field, String prefix) throws MappingException;
}
