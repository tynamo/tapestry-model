package org.tynamo.model.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.search.SearchFilterPredicate;

public class SearchableJpaGridDataSource implements GridDataSource {

	private Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap;
	private EntityManager entityManager;
	private Class entityType;
	private int startIndex;
	private List preparedResults;
	private String[] searchTerms;
	private List<TynamoPropertyDescriptor> searchablePropertyDescriptors;
	private Set includedIds;

	public SearchableJpaGridDataSource(EntityManager entityManager, Class entityType,
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap,
		List<TynamoPropertyDescriptor> searchablePropertyDescriptors, String... searchTerms) {
		this(entityManager, entityType, propertySearchFilterMap);
		this.searchablePropertyDescriptors = searchablePropertyDescriptors;
		this.searchTerms = searchTerms == null ? new String[0] : searchTerms;
	}

	public SearchableJpaGridDataSource(EntityManager entityManager, Class entityType, Set includedIds,
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap) {
		this(entityManager, entityType, propertySearchFilterMap);
		this.includedIds = includedIds;
	}

	private SearchableJpaGridDataSource(EntityManager entityManager, Class entityType,
		Map<TynamoPropertyDescriptor, SearchFilterPredicate> propertySearchFilterMap) {
		this.entityManager = entityManager;
		this.entityType = entityType;
		this.propertySearchFilterMap = propertySearchFilterMap;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getAvailableRows() {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

		final Root<?> root = criteria.from(entityType);

		criteria = criteria.select(builder.count(root));

		applyAdditionalConstraints(criteria, root, builder);

		return entityManager.createQuery(criteria).getSingleResult().intValue();
	}

	/**
	 * {@inheritDoc}
	 */
	public void prepare(final int startIndex, final int endIndex, final List<SortConstraint> sortConstraints) {
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		final CriteriaQuery criteria = builder.createQuery(entityType);

		final Root root = criteria.from(entityType);

		applyAdditionalConstraints(criteria.select(root), root, builder);

		for (final SortConstraint constraint : sortConstraints) {

			final String propertyName = constraint.getPropertyModel().getPropertyName();

			final Path<Object> propertyPath = root.get(propertyName);

			switch (constraint.getColumnSort()) {

			case ASCENDING:

				criteria.orderBy(builder.asc(propertyPath));
				break;

			case DESCENDING:
				criteria.orderBy(builder.desc(propertyPath));
				break;

			default:
			}
		}

		final TypedQuery<?> query = entityManager.createQuery(criteria);

		query.setFirstResult(startIndex);
		query.setMaxResults(endIndex - startIndex + 1);

		this.startIndex = startIndex;

		preparedResults = query.getResultList();

	}

	protected void applyAdditionalConstraints(final CriteriaQuery<?> criteria, final Root<?> root,
	                                          final CriteriaBuilder builder) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (searchTerms != null && searchTerms.length > 0) {
			for (TynamoPropertyDescriptor searchableProperty : searchablePropertyDescriptors) {
				for (String searchTerm : searchTerms)
					predicates.add(builder.like(root.<String>get(searchableProperty.getName()), searchTerm));
			}
			Predicate predicate = builder.or(predicates.toArray(new Predicate[predicates.size()]));
			criteria.where(applyAdditionalConstraints(builder, root, predicate));
		} else {
			Predicate predicate = applyAdditionalConstraints(builder, root, null);
			if (predicate != null) criteria.where(predicate);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getRowValue(final int index) {
		return preparedResults.get(index - startIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?> getRowType() {
		return entityType;
	}

	/**
	 * Invoked after the main criteria has been set up (firstResult, maxResults and any sort contraints). This gives subclasses a chance to
	 * apply additional constraints before the list of results is obtained from the criteria. This implementation does nothing and may be
	 * overridden.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Predicate applyAdditionalConstraints(CriteriaBuilder builder, Root<?> root, Predicate existingPredicate)
  {
		if ((includedIds == null || includedIds.size() <= 0)
				&& (propertySearchFilterMap == null || propertySearchFilterMap.size() <= 0)) return existingPredicate;

		List<Predicate> predicates = new ArrayList<Predicate>(propertySearchFilterMap.entrySet().size());
		if (existingPredicate != null) predicates.add(existingPredicate);
		if (includedIds != null) {
			EntityType entityType = root.getModel();
			SingularAttribute idAttr = entityType.getId(entityType.getIdType().getJavaType());
			predicates.add(root.get(idAttr).in(includedIds));
		}
		for (Entry<TynamoPropertyDescriptor, SearchFilterPredicate> entry : propertySearchFilterMap.entrySet())
			predicates.add(createPredicate(builder, root, entry.getKey().getName(), entry.getValue()));

		return builder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Predicate createPredicate(CriteriaBuilder builder, Root<?> root, String propertyName, SearchFilterPredicate predicate) {
		switch (predicate.getOperator()) {
		case eq:
			return builder.equal(root.get(propertyName), predicate.getLowValue());
		case ne:
			return builder.notEqual(root.get(propertyName), predicate.getLowValue());
		case gt:
			return builder.greaterThan((Path<Comparable>) root.<Comparable> get(propertyName),
				(Comparable) predicate.getLowValue());
		case ge:
			return builder.greaterThanOrEqualTo((Path<Comparable>) root.<Comparable> get(propertyName),
				(Comparable) predicate.getLowValue());
		case lt:
			return builder.lessThan((Path<Comparable>) root.<Comparable> get(propertyName),
				(Comparable) predicate.getLowValue());
		case le:
			return builder.lessThanOrEqualTo((Path<Comparable>) root.<Comparable> get(propertyName),
				(Comparable) predicate.getLowValue());
		default:
			throw new IllegalArgumentException("Search filtering for operator '" + predicate.getOperator()
				+ "' is not yet supported");
		}
	}
}
