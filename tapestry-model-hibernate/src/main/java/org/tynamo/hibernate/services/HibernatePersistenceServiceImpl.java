/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.tynamo.hibernate.services;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.hibernate.HibernateGridDataSource;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.services.PropertyConduitSource;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.TransientObjectException;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.hibernate.QueryParameter;
import org.tynamo.internal.services.GenericPersistenceService;
import org.tynamo.services.DescriptorService;

@SuppressWarnings("unchecked")
public class HibernatePersistenceServiceImpl extends GenericPersistenceService implements HibernatePersistenceService
{

	private Logger logger;
	private DescriptorService descriptorService;
	private HibernateSessionManager sessionManager;
	private PropertyConduitSource propertyConduitSource;

	public HibernatePersistenceServiceImpl(Logger logger,
	                                       DescriptorService descriptorService,
	                                       HibernateSessionManager sessionManager,
	                                       PropertyAccess propertyAccess,
	                                       PropertyConduitSource propertyConduitSource)
	{
		super(propertyAccess);
		this.logger = logger;
		this.descriptorService = descriptorService;
		this.propertyConduitSource = propertyConduitSource;

		// we need a sessionmanager because Tapestry session proxy doesn't implement Hibernate's SessionImplementator interface
		this.sessionManager = sessionManager;
	}

	private Session getSession()
	{
		return sessionManager.getSession();
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
	 * @param type			 The type to use to check for security restrictions.
	 * @param detachedCriteria
	 * @return
	 */
	public <T> T getInstance(final Class<T> type, DetachedCriteria detachedCriteria)
	{
		final DetachedCriteria criteria = alterCriteria(type, detachedCriteria);
		return (T) criteria.getExecutableCriteria(getSession()).uniqueResult();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.tynamo.services.PersistenceService#getInstance(Class,Serializable)
	 */

	public <T> T getInstance(final Class<T> type, final Serializable id)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(type).add(Restrictions.idEq(id));
		return getInstance(type, criteria);
	}

	/**
	 * Execute an HQL query.
	 *
	 * @param queryString a query expressed in Hibernate's query language
	 * @return a List of entities containing the results of the query execution
	 */
	public List findByQuery(String queryString)
	{
		return findByQuery(queryString, new QueryParameter[0]);
	}

	/**
	 * Execute an HQL query.
	 *
	 * @param queryString a query expressed in Hibernate's query language
	 * @param parameters  the (optional) parameters for the query.
	 * @return a List of entities containing the results of the query execution
	 */
	public List findByQuery(String queryString, QueryParameter... parameters)
	{
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
	public List findByQuery(String queryString, int startIndex, int maxResults, QueryParameter... parameters)
	{
		Query query = getSession().createQuery(queryString);
		for (QueryParameter parameter : parameters)
		{
			parameter.applyNamedParameterToQuery(query);
		}

		if (maxResults > 0)
			query.setMaxResults(maxResults);

		if (startIndex > 0)
			query.setFirstResult(startIndex);

		if (logger.isDebugEnabled())
			logger.debug(query.getQueryString());

		return query.list();
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see org.tynamo.services.PersistenceService#getInstances(java.lang.Class)
	 */

	public <T> List<T> getInstances(final Class<T> type)
	{
		return getSession().createCriteria(type).list();
	}


	public <T> List<T> getInstances(final Class<T> type, int startIndex, int maxResults)
	{
		return getInstances(type, DetachedCriteria.forClass(type), startIndex, maxResults);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.tynamo.services.PersistenceService#save(java.lang.Object)
	 */
	public <T> T save(T instance)
	{
		TynamoClassDescriptor tynamoclassdescriptor = descriptorService.getClassDescriptor(instance.getClass());
		// @todo: org.hibernate.MappingException: Unknown entity

		/* check isTransient to avoid merging on entities not persisted yet. TRAILS-33 */
		if (!tynamoclassdescriptor.getHasCyclicRelationships() || isTransient(instance, tynamoclassdescriptor))
		{
			getSession().saveOrUpdate(instance);
		} else
		{
			instance = (T) getSession().merge(instance);
		}
		return instance;
	}


	public void removeAll(Collection collection)
	{
//		getSession().deleteAll(collection);
	}


	public void remove(Object instance)
	{
		getSession().delete(instance);
	}


	public <T> List<T> getInstances(Class<T> type, DetachedCriteria criteria)
	{
		criteria = alterCriteria(type, criteria);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.getExecutableCriteria(getSession()).list();
	}

	public void reattach(Object model)
	{
		getSession().lock(model, LockMode.NONE);
	}


	public Serializable getIdentifier(final Object data, final TynamoClassDescriptor classDescriptor)
	{
		return (Serializable) getPropertyAccess().get(data, classDescriptor.getIdentifierDescriptor().getName());
	}

	public Serializable getIdentifier(final Object data)
	{
		return getIdentifier(data, descriptorService.getClassDescriptor(data.getClass()));
	}

	public boolean isTransient(Object data, TynamoClassDescriptor classDescriptor)
	{
		try
		{
			return getIdentifier(data, classDescriptor) == null;
		} catch (TransientObjectException e)
		{
			return true;
		}
	}


	public List getInstances(final Object example, final TynamoClassDescriptor classDescriptor)
	{
		//create Criteria instance
		DetachedCriteria searchCriteria = DetachedCriteria.forClass(example.getClass());
		searchCriteria = alterCriteria(example.getClass(), searchCriteria);

		//loop over the example object's PropertyDescriptors
		for (TynamoPropertyDescriptor propertyDescriptor : classDescriptor.getPropertyDescriptors())
		{
			//only add a Criterion to the Criteria instance if this property is searchable
			if (propertyDescriptor.isSearchable())
			{
				String propertyName = propertyDescriptor.getName();
				Class propertyClass = propertyDescriptor.getPropertyType();
				Object value = getPropertyAccess().get(example, propertyName);

				//only add a Criterion to the Criteria instance if the value for this property is non-null
				if (value != null)
				{
					if (String.class.isAssignableFrom(propertyClass) && ((String) value).length() > 0)
					{
						searchCriteria
								.add(Restrictions.like(propertyName, value.toString(), MatchMode.ANYWHERE));
					}
					/**
					 * 'one'-end of many-to-one, one-to-one
					 *
					 * Just match the identifier
					 */
					else if (propertyDescriptor.isObjectReference())
					{
						Serializable identifierValue = getIdentifier(value,
								descriptorService.getClassDescriptor(propertyDescriptor.getBeanType()));
						searchCriteria.createCriteria(propertyName).add(Restrictions.idEq(identifierValue));
					} else if (propertyClass.isPrimitive())
					{
						//primitive types: ignore zeroes in case of numeric types, ignore booleans anyway (TODO come up with something...)
						if (!propertyClass.equals(boolean.class) && ((Number) value).longValue() != 0)
						{
							searchCriteria.add(Restrictions.eq(propertyName, value));
						}
					} else if (propertyDescriptor.isCollection())
					{
						//one-to-many or many-to-many
						CollectionDescriptor collectionDescriptor =
								(CollectionDescriptor) propertyDescriptor;
						TynamoClassDescriptor collectionClassDescriptor = descriptorService.getClassDescriptor(collectionDescriptor.getElementType());
						if (collectionClassDescriptor != null)
						{
							String identifierName = collectionClassDescriptor.getIdentifierDescriptor().getName();
							Collection<Serializable> identifierValues = new ArrayList<Serializable>();
							Collection associatedItems = (Collection) value;
							if (associatedItems != null && associatedItems.size() > 0)
							{
								for (Object o : associatedItems)
								{
									identifierValues.add(getIdentifier(o, collectionClassDescriptor));
								}
								//add a 'value IN collection' restriction
								searchCriteria.createCriteria(propertyName)
										.add(Restrictions.in(identifierName, identifierValues));
							}
						}
					}
				}
			}
		}
		searchCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		// FIXME This won't work because the shadow proxy doesn't implement SessionImplementor
		// that session is casted to. Maybe we should inject SessionManager instead
		// and obtain the Session from it
		return searchCriteria.getExecutableCriteria(getSession()).list();
	}

	public int count(Class type)
	{
		return count(type, DetachedCriteria.forClass(type));
	}

	public int count(Class type, DetachedCriteria detachedCriteria)
	{
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		final DetachedCriteria criteria = alterCriteria(type, detachedCriteria);
		Criteria executableCriteria = criteria.getExecutableCriteria(getSession()).setProjection(Projections.rowCount());
		return ((Long) executableCriteria.uniqueResult()).intValue();
	}

	public <T> List<T> getInstances(Class<T> type, final DetachedCriteria detachedCriteria, final int startIndex, final int maxResults)
	{
		return getInstances(alterCriteria(type, detachedCriteria), startIndex, maxResults);
	}


	public List getInstances(final DetachedCriteria detachedCriteria, final int startIndex, final int maxResults)
	{
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		Criteria executableCriteria = detachedCriteria.getExecutableCriteria(getSession());
		if (startIndex >= 0)
		{
			executableCriteria.setFirstResult(startIndex);
		}
		if (maxResults > 0)
		{
			executableCriteria.setMaxResults(maxResults);
		}
		return executableCriteria.list();
	}

	/**
	 * This hook allows subclasses to modify the query criteria, such as for security
	 *
	 * @param detachedCriteria The original Criteria query
	 * @return The modified Criteria query for execution
	 */
	protected DetachedCriteria alterCriteria(Class type, DetachedCriteria detachedCriteria)
	{
		return detachedCriteria;
	}

	/**
	 * @see org.tynamo.hibernate.services.HibernatePersistenceService#saveOrUpdate(java.lang.Object)
	 */

	public <T> T merge(T instance)
	{
		return (T) getSession().merge(instance);
	}

	/**
	 * @see org.tynamo.hibernate.services.HibernatePersistenceService#saveOrUpdate(java.lang.Object)
	 */
	public <T> T saveOrUpdate(T instance)
	{
		getSession().saveOrUpdate(instance);
		return instance;
	}

	public <T> T addToCollection(CollectionDescriptor descriptor, T element, Object collectionOwner)
	{
		Class elementType = descriptor.getElementType();
		String addMethod = descriptor.getAddExpression() != null ? descriptor.getAddExpression() : "add" + elementType.getSimpleName();

		try
		{
			Method method = descriptor.getBeanType().getMethod(addMethod, new Class[]{elementType});
			method.invoke(collectionOwner, element);
			return element;

		} catch (NoSuchMethodException e)
		{
			// do nothing;
		} catch (InvocationTargetException e)
		{
			throw new RuntimeException(e);
		} catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}

		Collection collection = (Collection) getPropertyAccess().get(collectionOwner, descriptor.getName());
		if (!(descriptor.isChildRelationship() && (collection instanceof List) && (collection.contains(element))))
		{
			collection.add(element);
		}
		return element;

	}

	public List getOrphanInstances(CollectionDescriptor descriptor, Object owner)
	{
		if (descriptor.getInverseProperty() != null && descriptor.isOneToMany())
		{
			Criteria criteria = getSession().createCriteria(descriptor.getElementType());
			TynamoClassDescriptor elementDescriptor = descriptorService.getClassDescriptor(descriptor.getBeanType());
			String idProperty = elementDescriptor.getIdentifierDescriptor().getName();
			if (owner != null)
			{
				criteria.add(
						Restrictions.disjunction()
								.add(Restrictions.isNull(descriptor.getInverseProperty()))
								.add(Restrictions.eq(descriptor.getInverseProperty() + "." + idProperty, getIdentifier(owner, elementDescriptor))));
			} else
			{
				criteria.add(Restrictions.isNull(descriptor.getInverseProperty()));
			}
			return criteria.list();
		}
		return getInstances(descriptor.getElementType());
	}

	@Override
	public <T> GridDataSource getGridDataSource(Class<T> type)
	{
		return new HibernateGridDataSource(getSession(), type);
	}
}
