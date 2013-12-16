/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.tynamo.model.jpa.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.jpa.JpaGridDataSource;
import org.slf4j.Logger;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.internal.services.GenericPersistenceService;
import org.tynamo.model.jpa.internal.ConfigurableEntityManagerProvider;
import org.tynamo.services.DescriptorService;

@SuppressWarnings("unchecked")
public class JpaPersistenceServiceImpl extends GenericPersistenceService implements JpaPersistenceService {

	private Logger logger;
	private DescriptorService descriptorService;
	private EntityManager em;

	public JpaPersistenceServiceImpl(Logger logger, DescriptorService descriptorService, @Autobuild ConfigurableEntityManagerProvider entityManagerProvider, PropertyAccess propertyAccess) {
		super(propertyAccess);
		this.logger = logger;
		this.descriptorService = descriptorService;
		this.em = entityManagerProvider.getEntityManager();
	}

	/**
	 * https://trails.dev.java.net/servlets/ReadMsg?listName=users&msgNo=1226
	 * <p/>
	 * Very often I find myself writing:
	 * <code>
	 * Object example = new Object(); example.setProperty(uniqueValue);
	 * List objects = ((TynamoPage)getPage()).getPersistenceService().getInstances(example);
	 * (MyObject)objects.get(0);
	 * </code>
	 * when, in fact, I know that the single property I populated my example object with should be unique, and thus only
	 * one object should be returned
	 *
	 * @param type		  The type to use to check for security restrictions.
	 * @param detachedQuery
	 * @return
	 */
	public <T> T getInstance(final Class<T> type, CriteriaQuery detachedQuery) {
		final CriteriaQuery<T> query = alterCriteria(type, detachedQuery);
		return em.createQuery(query).getSingleResult();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.tynamo.services.PersistenceService#getInstance(Class,Serializable)
	 */

	public <T> T getInstance(final Class<T> type, final Serializable id) {
		return em.find(type, id);
	}

	public <T> T loadInstance(final Class<T> type, Serializable id) {
		T entity = (T) em.find(type, id);
		if (entity == null) {
			throw new NoResultException();
		}
		return entity;
	}

	/**
	 * Execute an HQL query.
	 *
	 * @param queryString a query expressed in Hibernate's query language
	 * @return a List of entities containing the results of the query execution
	 */
	public List findByQuery(String queryString) {
		return findByQuery(queryString, new QueryParameter[0]);
	}

	/**
	 * Execute an HQL query.
	 *
	 * @param queryString a query expressed in Hibernate's query language
	 * @param parameters  the (optional) parameters for the query.
	 * @return a List of entities containing the results of the query execution
	 */
	public List findByQuery(String queryString, QueryParameter... parameters) {
		return findByQuery(queryString, 0, 0, parameters);
	}

	/**
	 * Execute an HQL query.
	 *
	 * @param queryString a query expressed in Hibernate's query language
	 * @param startIndex  the index of the first item to be retrieved
	 * @param maxResults  the number of items to be retrieved, if <code>0</code> it retrieves ALL the items
	 * @param parameters  the (optional) parameters for the query.
	 * @return a List of entities containing the results of the query execution
	 */
	public List findByQuery(String queryString, int startIndex, int maxResults, QueryParameter... parameters) {
		Query query = em.createQuery(queryString);
		for (QueryParameter parameter : parameters) {
			parameter.applyNamedParameterToQuery(query);
		}

		if (maxResults > 0)
			query.setMaxResults(maxResults);

		if (startIndex > 0)
			query.setFirstResult(startIndex);

		if (logger.isDebugEnabled())
			logger.debug(query.toString());

		return query.getResultList();
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see org.tynamo.services.PersistenceService#getInstances(java.lang.Class)
	 */

	public <T> List<T> getInstances(final Class<T> type) {
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<T> query = qb.createQuery(type);
		//Root<T> entity = query.from(type);
		return em.createQuery(query).getResultList();
	}


	public <T> List<T> getInstances(final Class<T> type, int startIndex, int maxResults) {
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<T> query = qb.createQuery(type);
		Root<T> entity = query.from(type);
		return getInstances(type, query, startIndex, maxResults);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.tynamo.services.PersistenceService#save(java.lang.Object)
	 */
	public <T> T save(T instance) // throws ValidationException
	{
/*
		try
		{
*/
		TynamoClassDescriptor TynamoClassDescriptor = descriptorService.getClassDescriptor(instance.getClass());
		/* check isTransient to avoid merging on entities not persisted yet. TRAILS-33 */
		if (!TynamoClassDescriptor.getHasCyclicRelationships() || isTransient(instance, TynamoClassDescriptor)) {
			em.persist(instance);
		} else {
			instance = (T) em.merge(instance);
		}
		return instance;
//		}
/*
		catch (DataAccessException dex)
		{
			throw new PersistenceException(dex);
		}
*/
	}


	public void removeAll(Collection collection) {
//		em.deleteAll(collection);
	}


	public void remove(Object instance) {
		em.remove(instance);
	}


	public <T> List<T> getInstances(Class<T> type, CriteriaQuery query) {
		query = alterCriteria(type, query);
		query.distinct(true);
		//query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return em.createQuery(query).getResultList();
		//return query.getExecutableCriteria(entityManagerSource.getSession()).list();
	}

	/** (non-Javadoc) */
/*
	public List<Class> getAllTypes()
	{
		ArrayList<Class> allTypes = new ArrayList<Class>();
		for (Object classMetadata : getSessionFactory().getAllClassMetadata().values())
		{
			allTypes.add(((ClassMetadata) classMetadata).getMappedClass(EntityMode.POJO));
		}
		return allTypes;
	}
*/
	public void reattach(Object model) {
		em.lock(model, LockModeType.NONE);
	}


	/**
	 * (non-Javadoc)
	 */

	public <T> T getInstance(final Class<T> type) {
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<T> query = qb.createQuery(type);
		return (T) getInstance(type, query);
	}

	/**
	 * Returns an entity's pk.
	 *
	 * @param data
	 * @param classDescriptor
	 * @return
	 * @note (ascandroli): I tried to implement it using something like:
	 */
	public Serializable getIdentifier(final Object data, final TynamoClassDescriptor classDescriptor) {
		// Ignore classdescriptor for now
		return getIdentifier(data);
	}

	public List getOrphanInstances(CollectionDescriptor descriptor, Object owner) {
		//FIXME Not implemented yet
		return Collections.EMPTY_LIST; // TYNAMO-228
	}

	public int count(Class type) {
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = qb.createQuery(Long.class);
		Root entity = query.from(type);
		Expression<Long> count = qb.count(entity);
		query.select(count);
		Long size = em.createQuery(query).getSingleResult();
		return size.intValue();
	}


	public Serializable getIdentifier(final Object data) {
		return (Serializable) em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(data);
	}

	public boolean isTransient(Object data, TynamoClassDescriptor classDescriptor) {
		return getIdentifier(data, classDescriptor) == null;
	}

	public List getInstances(final Object example, final TynamoClassDescriptor classDescriptor) {
		//create Criteria instance
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery searchQuery = cb.createQuery(example.getClass());
		searchQuery = alterCriteria(example.getClass(), searchQuery);

		Root entity = searchQuery.from(example.getClass());

		//loop over the example object's PropertyDescriptors
		for (TynamoPropertyDescriptor propertyDescriptor : classDescriptor.getPropertyDescriptors()) {
			//only add a Criterion to the Criteria instance if this property is searchable
			if (propertyDescriptor.isSearchable()) {
				String propertyName = propertyDescriptor.getName();
				Class propertyClass = propertyDescriptor.getPropertyType();
				Object value = null; //PropertyUtils.read(example, propertyName);

				//only add a Criterion to the Criteria instance if the value for this property is non-null
				if (value != null) {
					if (String.class.isAssignableFrom(propertyClass) && ((String) value).length() > 0) {
						searchQuery.where(cb.like(entity.get(propertyName), value.toString()));
						//.add(Restrictions.like(propertyName, value.toString(), MatchMode.ANYWHERE));
					}
					/**
					 * 'one'-end of many-to-one, one-to-one
					 *
					 * Just match the identifier
					 */
					else if (propertyDescriptor.isObjectReference()) {
						Serializable identifierValue = getIdentifier(value,
																	 descriptorService.getClassDescriptor(propertyDescriptor.getBeanType()));
						Type t = entity.getModel().getIdType();
						SingularAttribute idAttribute = entity.getModel().getId(t.getJavaType());
						searchQuery.where(cb.equal(entity.get(idAttribute), identifierValue));
					} else if (propertyClass.isPrimitive()) {
						//primitive types: ignore zeroes in case of numeric types, ignore booleans anyway (TODO come up with something...)
						if (!propertyClass.equals(boolean.class) && ((Number) value).longValue() != 0) {
							searchQuery.where(cb.equal(entity.get(propertyName), value));
							//searchQuery.add(Restrictions.eq(propertyName, value));
						}
					} else if (propertyDescriptor.isCollection()) {
						//one-to-many or many-to-many
						CollectionDescriptor collectionDescriptor =
								(CollectionDescriptor) propertyDescriptor;
						TynamoClassDescriptor collectionClassDescriptor = descriptorService.getClassDescriptor(collectionDescriptor.getElementType());
						if (collectionClassDescriptor != null) {
							String identifierName = collectionClassDescriptor.getIdentifierDescriptor().getName();
							Collection<Serializable> identifierValues = new ArrayList<Serializable>();
							Collection associatedItems = (Collection) value;
							if (associatedItems != null && associatedItems.size() > 0) {
								for (Object o : associatedItems) {
									identifierValues.add(getIdentifier(o, collectionClassDescriptor));
								}
								//add a 'value IN collection' restriction
								searchQuery.where(cb.in(entity.get(identifierName)).value(identifierValues));
								/*searchQuery.createCriteria(propertyName)
										.add(Restrictions.in(identifierName, identifierValues));*/
							}
						}
					}
				}
			}
		}
		//searchQuery.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		searchQuery.distinct(true);
		// FIXME This won't work because the shadow proxy doesn't implement SessionImplementor
		// that em is casted to. Maybe we should inject SessionManager instead
		// and obtain the Session from it
		return em.createQuery(searchQuery).getResultList();
		//return searchQuery.getExecutableCriteria(entityManagerSource.getSession()).list();
	}


	public int count(Class type, CriteriaQuery detachedCriteria) {
		//detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		detachedCriteria.distinct(true);
		final CriteriaQuery criteria = alterCriteria(type, detachedCriteria);
		//Criteria executableCriteria = criteria.getExecutableCriteria(entityManagerSource.getSession()).setProjection(Projections.rowCount());
		return em.createQuery(criteria).getMaxResults();
		//criteria.
		//return (Integer) executableCriteria.uniqueResult();
	}


	public <T> List<T> getInstances(Class<T> type, final CriteriaQuery detachedCriteria, final int startIndex, final int maxResults) {
		return getInstances(alterCriteria(type, detachedCriteria), startIndex, maxResults);
	}


	public List getInstances(final CriteriaQuery detachedQuery, final int startIndex, final int maxResults) {
		detachedQuery.distinct(true);//setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		//Criteria executableCriteria = detachedQuery.getExecutableCriteria(entityManagerSource.getSession());
		Query q = em.createQuery(detachedQuery);
		if (startIndex >= 0) {
			q.setFirstResult(startIndex);
		}
		if (maxResults > 0) {
			q.setMaxResults(maxResults);
		}
		return q.getResultList();
	}

	/**
	 * This hook allows subclasses to modify the query criteria, such as for security
	 *
	 * @param detachedCriteria The original Criteria query
	 * @return The modified Criteria query for execution
	 */
	protected CriteriaQuery alterCriteria(Class type, CriteriaQuery detachedCriteria) {
		return detachedCriteria;
	}

	@Override
	public <T> T merge(T instance) {
		return (T) em.merge(instance);
	}

	@Override
	public <T> T saveOrUpdate(T instance) {
		em.persist(instance);
		return instance;
	}

	@Override
	public <T> GridDataSource getGridDataSource(Class<T> type)
	{
		return new JpaGridDataSource<T>(em, type);
	}

//	public <T> T saveCollectionElement(String addExpression, T member, Object parent) {
//		T instance = save(member);
//		Utils.executeOgnlExpression(addExpression, member, parent);
//		save(parent);
//		return instance;
//	}
//
//
//	public void removeCollectionElement(String removeExpression, Object member, Object parent) {
//		Utils.executeOgnlExpression(removeExpression, member, parent);
//		save(parent);
//		remove(member);
//	}

}
