package org.tynamo.model.jpa.internal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.eclipse.persistence.descriptors.DescriptorEvent;
import org.eclipse.persistence.descriptors.DescriptorEventAdapter;
import org.eclipse.persistence.sessions.Session;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.elasticsearch.node.Node;
import org.slf4j.Logger;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.model.elasticsearch.descriptor.ElasticSearchExtension;
import org.tynamo.model.elasticsearch.mapping.MapperFactory;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;

public class ElasticSearchIndexMaintainer {
	private final Node node;
	private final EntityManager entityManager;
	private final DescriptorService descriptorService;
	private final Logger logger;
	private final PersistenceService persistenceService;
	private final MapperFactory mapperFactory;
	private volatile boolean running = false;

	public ElasticSearchIndexMaintainer(Logger logger, RegistryShutdownHub hub, PersistenceService persistenceService,
		EntityManager entityManager, Node node,
		DescriptorService descriptorService,
		MapperFactory mapperFactory) {
		this.logger = logger;
		this.persistenceService = persistenceService;
		this.entityManager = entityManager;
		this.node = node;
		this.descriptorService = descriptorService;
		this.mapperFactory = mapperFactory;

		hub.addRegistryShutdownListener(new Runnable() {
			public void run() {
				running = false;
			}
		});
	}

	/**
	 * Triggered after a JPA entity is persisted or updated
	 *
	 * @param entity
	 *          The entity to index
	 */
	@PostPersist
	@PostUpdate
	public void postWrite(Object entity) {
		Client client = node.client();
		try {
			indexEntity(client, entity, getElasticSearchDescriptor(entity));
		} catch (Exception e) {
			logger.error(String.format("Failed to index entity %s of type %s", entity, entity.getClass().getSimpleName()), e);
		}
	}

	ElasticSearchExtension getElasticSearchDescriptor(Object entity) {
		return descriptorService.getClassDescriptor(entity.getClass()).getExtension(ElasticSearchExtension.class);
	}

	/**
	 * Triggered after a JPA entity is deleted
	 *
	 * @param entity
	 *          The entity to remove from the index
	 */
	@PostRemove
	public void postDelete(Object entity) {
		removeEntityFromIndex(node.client(), entity, getElasticSearchDescriptor(entity));
	}

	public void removeEntityFromIndex(Client client, Object entity, ElasticSearchExtension elasticSearchExtension) {
		logger.debug("Delete entity %s of type %s from elasticsearch index", entity, entity.getClass().getName());
		ElasticSearchExtension descriptor = getElasticSearchDescriptor(entity);
		client.prepareDelete(descriptor.getIndexName(), descriptor.getTypeName(), descriptor.getDocumentId(entity))
			.execute();
	}


	public void start() {
		try {
			Class.forName("org.eclipse.persistence.sessions.Session");
		} catch (ClassNotFoundException e) {
			logger.warn("Elasticsearch integration is currently for EclipseLink only. Only JPA search is available ");
			return;
		}

		Thread indexCreator = new Thread(new Runnable() {
			public void run() {
				running = true;
				createIndices();
			}
		});
		indexCreator.start();
	}

	protected void createIndices() {
		Client client = node.client();
		client.admin().cluster().prepareHealth().setWaitForYellowStatus().execute().actionGet();

		// List<String> indexNames = new ArrayList<String>(descriptorService.getAllDescriptors().size());
		// we can't use IndicesExists for all at the same time since it returns a simple boolean, rather than an array
		// IndicesExistsResponse response = client.admin().indices()
		// .exists(new IndicesExistsRequest(indexNames.toArray(new String[0]))).actionGet();
		for (TynamoClassDescriptor descriptor : descriptorService.getAllDescriptors()) {
			if (!running) {
				logger.info(String.format(
					"%s isn't in a running state anymore, indexing stopped while processing descriptor for type %s", getClass()
						.getSimpleName(), descriptor.getBeanType().getName()));
				break;
			}
			if (!descriptor.supportsExtension(ElasticSearchExtension.class)) continue;
			// register to listen to events of each entity separately
			// http://eclipse.1072660.n5.nabble.com/Specific-EntityListener-Instance-td4159.html
			entityManager.unwrap(Session.class).getDescriptor(descriptor.getBeanType()).getEventManager()
				.addListener(new EclipseLinkDescriptorEventListener());
			ElasticSearchExtension descriptorExtension = descriptor.getExtension(ElasticSearchExtension.class);
			if (!client.admin().indices().prepareExists(descriptorExtension.getIndexName()).execute().actionGet().isExists()) {
				// create index and start indexing entity
				createIndex(client, descriptorExtension);
				if (!indexEntities(client, descriptor.getBeanType(), descriptorExtension)) {
					if (!running) {
						logger.info(String.format(
							"%s isn't in a running state anymore, indexing stopped while processing descriptor for type %s",
							getClass()
							.getSimpleName(), descriptor.getBeanType().getName()));
						break;
					} else {
						logger.info(String.format(
							"%s failed batch indexing type %s, skipping over remaining entities of the same type", getClass()
								.getSimpleName(), descriptor
							.getBeanType().getName()));
					}
				}
			}
		}
	}

	protected boolean indexEntities(Client client, Class beanType, ElasticSearchExtension descriptorExtension) {
		List entities = persistenceService.getInstances(beanType);
		try {
			for (Object entity : entities) {
				if (!running) return false;
				indexEntity(client, entity, descriptorExtension);
			}
		} catch (Exception e) {
			// FIXME should we delete index?
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	protected void indexEntity(Client client, Object model, ElasticSearchExtension descriptor) throws Exception {
		logger.debug("Index Model: %s", model);

		XContentBuilder contentBuilder = null;

		// Index Model
		try {
			contentBuilder = XContentFactory.jsonBuilder().prettyPrint();
			descriptor.addModel(model, contentBuilder, mapperFactory);
			logger.debug("Index json: %s", contentBuilder.string());
			IndexResponse response = client
				.prepareIndex(descriptor.getIndexName(), descriptor.getTypeName(), descriptor.getDocumentId(model))
				.setSource(contentBuilder).execute()
				.actionGet();

			logger.debug("Index Response: %s", response);

		} finally {
			if (contentBuilder != null) contentBuilder.close();
		}
	}

	private void createIndex(Client client, ElasticSearchExtension descriptor) {
		String indexName = descriptor.getIndexName();
		try {
			logger.debug("Starting Elastic Search Index %s", indexName);
			CreateIndexResponse response = client.admin().indices().create(new CreateIndexRequest(indexName)).actionGet();
			logger.debug("Response: %s", response);

		} catch (IndexAlreadyExistsException iaee) {
			logger.debug("Index already exists: %s", indexName);
			return;
		} catch (Throwable t) {
			logger.warn(ExceptionUtils.getStackTrace(t));
			return;
		}
		createType(client, descriptor);
	}

	private void createType(Client client, ElasticSearchExtension descriptor) {
		String indexName = descriptor.getIndexName();
		String typeName = descriptor.getTypeName();

		try {
			logger.debug("Create Elastic Search Type %s/%s", indexName, typeName);
			PutMappingRequest request = Requests.putMappingRequest(indexName).type(typeName);
			XContentBuilder contentBuilder = XContentFactory.jsonBuilder().prettyPrint();
			descriptor.addMapping(contentBuilder, mapperFactory);
			logger.debug("Type mapping: \n %s", contentBuilder.string());
			request.source(contentBuilder);
			PutMappingResponse response = client.admin().indices().putMapping(request).actionGet();
			logger.debug("Response: %s", response);

		} catch (IndexAlreadyExistsException iaee) {
			logger.debug("Index already exists: %s", indexName);

		} catch (Throwable t) {
			logger.warn(ExceptionUtils.getStackTrace(t));
		}
	}

	class EclipseLinkDescriptorEventListener extends DescriptorEventAdapter {
		@Override
		public void postInsert(DescriptorEvent event) {
			ElasticSearchIndexMaintainer.this.postWrite(event.getSource());
		}

		@Override
		public void postUpdate(DescriptorEvent event) {
			ElasticSearchIndexMaintainer.this.postWrite(event.getSource());
		}

		@Override
		public void postDelete(DescriptorEvent event) {
			ElasticSearchIndexMaintainer.this.postDelete(event.getSource());
		}
	}

}
