package org.tynamo.model.elasticsearch.descriptor;

import java.io.IOException;
import java.util.List;

import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Flow;
import org.apache.tapestry5.func.Predicate;
import org.apache.tapestry5.func.Worker;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.descriptor.extension.DescriptorExtension;
import org.tynamo.model.elasticsearch.annotations.ElasticSearchable;
import org.tynamo.model.elasticsearch.mapping.MapperFactory;

public class ElasticSearchExtension implements DescriptorExtension {
	private static final long serialVersionUID = 1L;

	private final TynamoClassDescriptor ownerDescriptor;
	private String indexName;
	private String typeName;
	private String idPrefix = "";

	private PropertyAccess propertyAccess;

	public ElasticSearchExtension(TynamoClassDescriptor descriptor, PropertyAccess propertyAccess) {
		ownerDescriptor = descriptor;
		this.propertyAccess = propertyAccess;
		Class beanType = descriptor.getBeanType();
		typeName = beanType.getName().toLowerCase().trim().replace('.', '_');
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
		return idPrefix + propertyAccess.get(model, ownerDescriptor.getIdentifierDescriptor().getName()).toString();
	}

	public void addModel(final Object model, final XContentBuilder builder, final MapperFactory mapperFactory)
		throws IOException {
		builder.startObject();

		// for (FieldMapper field : mapping) {
		// field.addToDocument(model, builder);
		// }
		getElasticPropertyDescriptorFlow(ownerDescriptor.getPropertyDescriptors()).each(
			new Worker<TynamoPropertyDescriptor>() {
				@Override
				public void work(TynamoPropertyDescriptor descriptor) {
					try {
						mapperFactory.getMapper(descriptor).addToDocument(propertyAccess.get(model, descriptor.getName()), builder);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			});

		builder.endObject();
	}

	Flow<TynamoPropertyDescriptor> getElasticPropertyDescriptorFlow(List<TynamoPropertyDescriptor> propertyDescriptors) {
		return F.flow(ownerDescriptor.getPropertyDescriptors()).filter(new Predicate<TynamoPropertyDescriptor>() {
			public boolean accept(TynamoPropertyDescriptor descriptor) {
				return descriptor.supportsExtension(ElasticSearchFieldDescriptor.class);
			}
		});
	}

	public void addMapping(final XContentBuilder builder, final MapperFactory mapperFactory) throws IOException {
		builder.startObject();
		builder.startObject(getTypeName());
		// TODO do we need time-to-live annotation?
		// if (clazz.isAnnotationPresent(ElasticSearchTtl.class)) {
		// String ttlValue = ((ElasticSearchTtl) clazz.getAnnotation(ElasticSearchTtl.class)).value();
		// builder.startObject("_ttl");
		// builder.field("enabled", true);
		// builder.field("default", ttlValue);
		// builder.endObject();
		// }

		builder.startObject("properties");
		getElasticPropertyDescriptorFlow(ownerDescriptor.getPropertyDescriptors()).each(new Worker<TynamoPropertyDescriptor>() {
			@Override
			public void work(TynamoPropertyDescriptor descriptor) {
				try {
					mapperFactory.getMapper(descriptor).addToMapping(builder);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
			}});

		// for (TynamoPropertyDescriptor descriptor : ownerDescriptor.getPropertyDescriptors())
		// if (descriptor.supportsExtension(ElasticSearchFieldDescriptor.class))
		// mapperFactory.getMapper(descriptor).addToMapping(builder);

		builder.endObject();
		builder.endObject();
		builder.endObject();
	}

}
