package org.tynamo.hibernate.components;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.search.FullTextSession;
import org.tynamo.base.GenericLuceneModelSearch;
import org.tynamo.hibernate.SearchableHibernateGridDataSource;

public class HibernateModelSearch extends GenericLuceneModelSearch {
	@Inject
	private FullTextSession session;

	protected GridDataSource createGridDataSource() {
		return new SearchableHibernateGridDataSource(session, getBeanType(), getActiveFilterMap());
	}

	protected GridDataSource createGridDataSource(org.apache.lucene.search.Query query) {
		return new SearchableHibernateGridDataSource(session, getBeanType(), session.createFullTextQuery(query,
			getBeanType()), getActiveFilterMap());
	}

}
