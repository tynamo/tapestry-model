package org.tynamo.base;

import java.util.ArrayList;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.Version;
import org.apache.tapestry5.grid.GridDataSource;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.search.SearchFilterPredicate;

public abstract class GenericLuceneModelSearch extends GenericModelSearch {
	/**
	 * The source of data for the Grid to display. This will usually be a List or array but can also be an explicit GridDataSource
	 *
	 * @throws ParseException
	 */
	public GridDataSource getGridDataSource() {
		// return new TynamoGridDataSource(persistenceService, beanType);
		// return new HibernateGridDataSource(session, beanType);
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap = getActiveFilterMap();

		if (getSearchTerms() == null) return createGridDataSource();

		TynamoClassDescriptor classDescriptor = getDescriptorService().getClassDescriptor(getBeanType());
		java.util.List<String> fieldNames = new ArrayList<String>();
		java.util.List<TynamoPropertyDescriptor> propertyDescriptors = classDescriptor.getPropertyDescriptors();
		for (TynamoPropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if (propertyDescriptor.isSearchable() && propertyDescriptor.isString())
				fieldNames.add(propertyDescriptor.getName());
		}
		// don't bother with a text query if there are no @Fields
		if (fieldNames.size() <= 0) return createGridDataSource();

		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_36, fieldNames.toArray(new String[0]),
			new StandardAnalyzer(Version.LUCENE_36));
		// parser.setDefaultOperator(QueryParser.AND_OPERATOR); // overrides the default OR_OPERATOR, so that all words in the search are
		// required
		try {
			return createGridDataSource(parser.parse(getSearchTerms()));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		// NOTE Hibernate Search DSL checks that the fields exists, otherwise it throws exceptions. Lucene is more forgiving
		// QueryBuilder qb = session.getSearchFactory().buildQueryBuilder().forEntity( beanType ).get();
		// org.apache.lucene.search.Query query = qb.keyword().onFields(fieldNames.toArray(new String[0])).matching(searchTerms).createQuery();
		// return new SearchableHibernateGridDataSource(session, beanType, session.createFullTextQuery(query, beanType),
		// propertySearchFilterMap);
	}

	protected abstract GridDataSource createGridDataSource();

	protected abstract GridDataSource createGridDataSource(org.apache.lucene.search.Query query);
}