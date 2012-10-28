package org.tynamo.model.jpa.internal;

import javax.persistence.EntityManager;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;
import org.elasticsearch.node.Node;
import org.tynamo.services.SearchIndexListener;

public class ElasticSearchIndexListener implements SearchIndexListener {
	private Node node;
	private EntityManager entityManager;

	public ElasticSearchIndexListener(EntityManager entityManager, Node node) {
		this.entityManager = entityManager;
		this.node = node;
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
		// FIXME does an index need to be created before hand?
		// client.admin().indices().prepareCreate("adverts").execute().actionGet();

		// DocumentObjectBinder binder = solrServer.getBinder();
		// SolrInputDocument document = binder.toSolrInputDocument(entity);
		// SolrInputField field = document.getField("id");
		// SolrDocument doc = new SolrDocument();
		//
		// solrServer.addBean(entity);
		// solrServer.commit();
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

	public void startListening() {
		// http://ipapaste.wordpress.com/2012/10/19/highlight-annotations-for-solr-please/

		// FIXME exit if org.eclipse.persistence.jpa.JpaEntityManager isn't available

		((JpaEntityManager) entityManager.getDelegate()).getSession().getEventManager()
			.addListener(new EclipseLinkSessionListener());
	}

	class EclipseLinkSessionListener extends SessionEventAdapter {
		@Override
		public void postCommitUnitOfWork(SessionEvent event) {
			ElasticSearchIndexListener.this.postPersist(event.getSource());
		}

		@Override
		public void postMergeUnitOfWorkChangeSet(SessionEvent event) {
			ElasticSearchIndexListener.this.postPersist(event.getSource());
		}

	}

}
