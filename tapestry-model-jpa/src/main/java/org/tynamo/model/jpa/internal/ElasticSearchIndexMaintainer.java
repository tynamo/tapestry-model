package org.tynamo.model.jpa.internal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.persistence.descriptors.DescriptorEvent;
import org.eclipse.persistence.descriptors.DescriptorEventAdapter;
import org.eclipse.persistence.sessions.Session;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
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
	private Node node;
	private EntityManager entityManager;
	private DescriptorService descriptorService;
	private MapperFactory mapperFactory;
	private Logger logger;
	private PersistenceService persistenceService;

	public ElasticSearchIndexMaintainer(Logger logger, PersistenceService persistenceService,
		EntityManager entityManager, Node node,
		DescriptorService descriptorService,
		MapperFactory mapperFactory) {
		this.logger = logger;
		this.persistenceService = persistenceService;
		this.entityManager = entityManager;
		this.node = node;
		this.descriptorService = descriptorService;
		this.mapperFactory = mapperFactory;

	}

	/**
	 * Triggered after a JPA entity is persisted or updated
	 *
	 * @param entity
	 *          The entity to index
	 */
	@PostPersist
	@PostUpdate
	public void postPersist(Object entity) {
		Client client = node.client();
		try {
			indexEntity(client, entity, descriptorService.getClassDescriptor(entity.getClass()).getExtension(ElasticSearchExtension.class));
		} catch (Exception e) {
			logger.error(String.format("Failed to index entity %s of type %s", entity, entity.getClass().getSimpleName()), e);
		}
	}

	/**
	 * Triggered after a JPA entity is deleted
	 *
	 * @param entity
	 *          The entity to remove from the index
	 */
	@PostRemove
	public void postRemove(Object entity) {
		// search.onSearchableRemoved(entity);
		// solrServer.
		// solrServer.commit();
	}

	public void start() {
		// FIXME exit if org.eclipse.persistence.jpa.JpaEntityManager isn't available

		// ((JpaEntityManager) entityManager.getDelegate()).getSession().getEventManager()
		// .addListener(new EclipseLinkSessionListener());

		Thread indexCreator = new Thread(new Runnable() {
			public void run() {
				createIndices();
			}
		});

		indexCreator.setDaemon(true);
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
			if (!descriptor.supportsExtension(ElasticSearchExtension.class)) continue;
			// register to listen to events of each entity separately
			entityManager.unwrap(Session.class).getDescriptor(descriptor.getBeanType()).getEventManager()
				.addListener(new EclipseLinkDescriptorEventListener());
			ElasticSearchExtension descriptorExtension = descriptor.getExtension(ElasticSearchExtension.class);
			if (!client.admin().indices().prepareExists(descriptorExtension.getIndexName()).execute().actionGet().exists()) {
				// create index and start indexing entity
				createIndex(client, descriptorExtension.getIndexName());
				indexEntities(client, descriptor.getBeanType(), descriptorExtension);
			}
		}
	}

	public void indexEntities(Client client, Class beanType, ElasticSearchExtension descriptorExtension) {
		List entities = persistenceService.getInstances(beanType);
		try {
			for (Object entity : entities)
				indexEntity(client, entity, descriptorExtension);
		} catch (Exception e) {
			// FIXME should we delete index?
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void indexEntity(Client client, Object model, ElasticSearchExtension descriptor) throws Exception {
		logger.debug("Index Model: %s", model);

		// Define Content Builder
		XContentBuilder contentBuilder = null;

		// Index Model
		try {
			// Define Index Name
			String indexName = descriptor.getIndexName();
			String typeName = descriptor.getTypeName();
			String documentId = descriptor.getDocumentId(model);
			logger.debug("Index Name: %s", indexName);

			contentBuilder = XContentFactory.jsonBuilder().prettyPrint();
			descriptor.addModel(model, contentBuilder);
			logger.debug("Index json: %s", contentBuilder.string());
			IndexResponse response = client.prepareIndex(indexName, typeName, documentId).setSource(contentBuilder).execute()
				.actionGet();

			logger.debug("Index Response: %s", response);

		} finally {
			if (contentBuilder != null) {
				contentBuilder.close();
			}
	}
	}

	private void createIndex(Client client, String indexName) {
		try {
			logger.debug("Starting Elastic Search Index %s", indexName);
			CreateIndexResponse response = client.admin().indices().create(new CreateIndexRequest(indexName)).actionGet();
			logger.debug("Response: %s", response);

		} catch (IndexAlreadyExistsException iaee) {
			logger.debug("Index already exists: %s", indexName);

		} catch (Throwable t) {
			logger.warn(ExceptionUtils.getStackTrace(t));
		}
	}

	class EclipseLinkDescriptorEventListener extends DescriptorEventAdapter {
		@Override
		public void postWrite(DescriptorEvent event) {
			ElasticSearchIndexMaintainer.this.postPersist(event.getSource());
		}
		// @Override
		// public void postCommitUnitOfWork(SessionEvent event) {
		// ElasticSearchIndexMaintainer.this.postPersist(event.getSource());
		// }
		//
		// @Override
		// public void postMergeUnitOfWorkChangeSet(SessionEvent event) {
		// ElasticSearchIndexMaintainer.this.postPersist(event.getSource());
		// }

	}

}
