package org.tynamo.hibernate.services.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.Version;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.hibernate.HibernateGridDataSource;
import org.hibernate.search.FullTextSession;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.hibernate.SearchableHibernateGridDataSource;
import org.tynamo.search.SearchFilterPredicate;
import org.tynamo.services.SearchableGridDataSourceProvider;

public class SearchableHibernateGridDataSourceProvider implements SearchableGridDataSourceProvider {

	private FullTextSession session;

	public SearchableHibernateGridDataSourceProvider(FullTextSession session) {
		this.session = session;
	}

	@Override
	public GridDataSource createGridDataSource(Class entityType) {
		return new HibernateGridDataSource(session, entityType);
	}

	@Override
	public GridDataSource createGridDataSource(Class entityType, Set includedIds,
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap) {
		// FIXME ignoring included ids
		return new SearchableHibernateGridDataSource(session, entityType, propertySearchFilterMap);
	}

	@Override
	public GridDataSource createGridDataSource(Class entityType,
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap,
		List<TynamoPropertyDescriptor> searchablePropertyDescriptors, String searchString) {

		java.util.List<String> fieldNames = new ArrayList<String>();
		for (TynamoPropertyDescriptor propertyDescriptor : searchablePropertyDescriptors) {
			fieldNames.add(propertyDescriptor.getName());
		}
		// don't bother with a text query if there are no @Fields
		if (fieldNames.size() <= 0) return createGridDataSource(entityType);

		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_35, fieldNames.toArray(new String[fieldNames.size()]),
			new StandardAnalyzer(Version.LUCENE_35));
		// parser.setDefaultOperator(QueryParser.AND_OPERATOR); // overrides the default OR_OPERATOR, so that all words in the search are
		// required
		try {
			return new SearchableHibernateGridDataSource(session, entityType, session.createFullTextQuery(
				parser.parse(searchString), entityType), propertySearchFilterMap);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

	}

}
