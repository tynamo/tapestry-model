package org.tynamo.model.elasticsearch.descriptor;

import java.io.IOException;
import java.util.List;

import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.extension.DescriptorExtension;
import org.tynamo.model.elasticsearch.annotations.ElasticSearchable;
import org.tynamo.model.elasticsearch.mapping.FieldMapper;

public class ElasticSearchExtension implements DescriptorExtension {
	private static final long serialVersionUID = 1L;

	private String indexName;
	private String typeName;
	private String idPrefix = "";
	private String idPropertyName;

	private PropertyAccess propertyAccess;
	List<FieldMapper> mapping;

	public ElasticSearchExtension(TynamoClassDescriptor descriptor, PropertyAccess propertyAccess,
		List<FieldMapper> mapping) {
		this.propertyAccess = propertyAccess;
		this.mapping = mapping;
		Class beanType = descriptor.getBeanType();
		typeName = beanType.getName().toLowerCase().trim().replace('.', '_');
		idPropertyName = descriptor.getIdentifierDescriptor().getName();
		ElasticSearchable meta = (ElasticSearchable) beanType.getAnnotation(ElasticSearchable.class);
		if (meta.indexName().length() > 0) indexName = meta.indexName();
	}

	/**
	 * Gets the index name
	 *
	 * @return the index name
	 */
	public String getIndexName() {
		return indexName == null ? getTypeName() : indexName;
	}

	/**
	 * Gets the type name
	 *
	 * @return the type name
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Gets the document id
	 *
	 * @param model
	 *          the model
	 * @return the model's document id
	 */
	public String getDocumentId(Object model) {
		return idPrefix + propertyAccess.get(model, idPropertyName).toString();
	}

	public void addModel(Object model, XContentBuilder builder) throws IOException {
		builder.startObject();

		for (FieldMapper field : mapping) {
			field.addToDocument(model, builder);
		}

		builder.endObject();
	}

}
