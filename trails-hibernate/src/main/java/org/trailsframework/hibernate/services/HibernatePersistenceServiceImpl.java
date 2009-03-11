/*
 * Copyright 2004 Chris Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.trailsframework.hibernate.services;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.trailsframework.services.DescriptorService;
import org.trailsframework.util.Utils;
import org.trailsframework.descriptor.TrailsClassDescriptor;
import org.trailsframework.descriptor.TrailsPropertyDescriptor;
import org.trailsframework.descriptor.CollectionDescriptor;
import org.slf4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unchecked")
public class HibernatePersistenceServiceImpl implements HibernatePersistenceService
{

	private Logger logger;
	private DescriptorService descriptorService;
	private Session session;
	private HibernateSessionManager sessionManager;

	public HibernatePersistenceServiceImpl(Logger logger, DescriptorService descriptorService, Session session, HibernateSessionManager sessionManager)
	{
		this.logger = logger;
		this.descriptorService = descriptorService;
		this.session = session;
		// we need a sessionmanager as well (only?) because Tapestry session proxy doesn't implement Hibernate's SessionImplementator interface
		this.sessionManager = sessionManager;
	}

	/**
	 * https://trails.dev.java.net/servlets/ReadMsg?listName=users&msgNo=1226
	 * <p/>
	 * Very often I find myself writing:
	 * <code>
	 * Object example = new Object(); example.setProperty(uniqueValue);
	 * List objects = ((TrailsPage)getPage()).getPersistenceService().getInstances(example);
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
		return (T) criteria.getExecutableCriteria(sessionManager.getSession()).uniqueResult();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.trailsframework.services.PersistenceService#getInstance(Class,Serializable)
	 */

	public <T> T getInstance(final Class<T> type, final Serializable id)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Utils.checkForCGLIB(type)).add(Restrictions.idEq(id));
		return getInstance(type, criteria);
	}


	/**
	 * <strong>Description copied from:</strong> {@link org.hibernate.Session#load(Class,java.io.Serializable)}
	 * <p/>
	 * Return the persistent instance of the given entity class with the given identifier, assuming that the instance
	 * exists, throwing an exception if not found.
	 * <p/>
	 * You should not use this method to determine if an instance exists (use get() instead). Use this only to retrieve an
	 * instance that you assume exists, where non-existence would be an actual error.
	 * <p/>
	 * <p>This method is a thin wrapper around {@link org.hibernate.Session#load(Class,java.io.Serializable)} for
	 * convenience. For an explanation of the exact semantics of this method, please do refer to the Hibernate API
	 * documentation in the first instance.
	 *
	 * @param type a persistent class
	 * @param id   the identifier of the persistent instance
	 * @return the persistent instance
	 * @see org.hibernate.Session#load(Class,java.io.Serializable)
	 */

	public <T> T loadInstance(final Class<T> type, Serializable id)
	{
		return (T) session.load(type, id);
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
		Query query = session.createQuery(queryString);
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
	 * @see org.trailsframework.services.PersistenceService#getInstances(java.lang.Class)
	 */

	public <T> List<T> getInstances(final Class<T> type)
	{
		return session.createCriteria(type).list();
	}


	public <T> List<T> getInstances(final Class<T> type, int startIndex, int maxResults)
	{
		return getInstances(type, DetachedCriteria.forClass(type), startIndex, maxResults);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.trailsframework.services.PersistenceService#save(java.lang.Object)
	 */
	public <T> T save(T instance) // throws ValidationException
	{
/*
		try
		{
*/
		TrailsClassDescriptor TrailsClassDescriptor = descriptorService.getClassDescriptor(instance.getClass());
		/* check isTransient to avoid merging on entities not persisted yet. TRAILS-33 */
		if (!TrailsClassDescriptor.getHasCyclicRelationships() || isTransient(instance, TrailsClassDescriptor))
		{
			session.saveOrUpdate(instance);
		} else
		{
			instance = (T) session.merge(instance);
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


	public void removeAll(Collection collection)
	{
//		session.deleteAll(collection);
	}


	public void remove(Object instance)
	{
		session.delete(instance);
	}


	public <T> List<T> getInstances(Class<T> type, DetachedCriteria criteria)
	{
		criteria = alterCriteria(type, criteria);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.getExecutableCriteria(sessionManager.getSession()).list();
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.trailsframework.services.PersistenceService#getAllTypes()
	 */
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
	public void reattach(Object model)
	{
		session.lock(model, LockMode.NONE);
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see org.trailsframework.services.PersistenceService#getInstance(Class<T>)
	 */

	public <T> T getInstance(final Class<T> type)
	{
		return (T) getInstance(type, DetachedCriteria.forClass(type));
	}

	/**
	 * Returns an entity's pk.
	 *
	 * @param data
	 * @param classDescriptor
	 * @return
	 * @note (ascandroli): I tried to implement it using something like:
	 * <p/>
	 * <code>
	 * <p/>
	 * <p/>
	 * private Serializable getIdentifier(final Object data)
	 * {
	 * return (Serializable) session.execute(new HibernateCallback()
	 * {
	 * public Object doInHibernate(Session session) throws HibernateException, SQLException
	 * {
	 * return session.getIdentifier(data);
	 * }
	 * });
	 * }
	 * <p/>
	 * </code>
	 * <p/>
	 * but it didn't work.
	 * "session.getIdentifier(data)" thows TransientObjectException when the Entity is not loaded by the current session,
	 * which is pretty usual in Trails.
	 */
	public Serializable getIdentifier(final Object data, final TrailsClassDescriptor classDescriptor)
	{
//		return (Serializable) PropertyUtils.read(data, classDescriptor.getIdentifierDescriptor().getName());
		return 0;
	}

	public boolean isTransient(Object data, TrailsClassDescriptor classDescriptor)
	{
		try
		{
			return getIdentifier(data, classDescriptor) == null;
		} catch (TransientObjectException e)
		{
			return true;
		}
	}


	public List getInstances(final Object example, final TrailsClassDescriptor classDescriptor)
	{
		//create Criteria instance
		DetachedCriteria searchCriteria = DetachedCriteria.forClass(Utils.checkForCGLIB(example.getClass()));
		searchCriteria = alterCriteria(example.getClass(), searchCriteria);

		//loop over the example object's PropertyDescriptors
		for (TrailsPropertyDescriptor propertyDescriptor : classDescriptor.getPropertyDescriptors())
		{
			//only add a Criterion to the Criteria instance if this property is searchable
			if (propertyDescriptor.isSearchable())
			{
				String propertyName = propertyDescriptor.getName();
				Class propertyClass = propertyDescriptor.getPropertyType();
				Object value = null; //PropertyUtils.read(example, propertyName);

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
						TrailsClassDescriptor collectionClassDescriptor = descriptorService.getClassDescriptor(collectionDescriptor.getElementType());
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
		return searchCriteria.getExecutableCriteria(sessionManager.getSession()).list();
	}


	public int count(Class type, DetachedCriteria detachedCriteria)
	{
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		final DetachedCriteria criteria = alterCriteria(type, detachedCriteria);
		Criteria executableCriteria = criteria.getExecutableCriteria(sessionManager.getSession()).setProjection(Projections.rowCount());
		return (Integer) executableCriteria.uniqueResult();
	}


	public <T> List<T> getInstances(Class<T> type, final DetachedCriteria detachedCriteria, final int startIndex, final int maxResults)
	{
		return getInstances(alterCriteria(type, detachedCriteria), startIndex, maxResults);
	}


	public List getInstances(final DetachedCriteria detachedCriteria, final int startIndex, final int maxResults)
	{
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		Criteria executableCriteria = detachedCriteria.getExecutableCriteria(sessionManager.getSession());
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
	 * @see org.trailsframework.hibernate.services.HibernatePersistenceService#saveOrUpdate(java.lang.Object)
	 */

	public <T> T merge(T instance)
	{
		return (T) session.merge(instance);
	}

	/**
	 * @see org.trailsframework.hibernate.services.HibernatePersistenceService#saveOrUpdate(java.lang.Object)
	 */

	public <T> T saveOrUpdate(T instance)  // throws ValidationException
	{
		session.saveOrUpdate(instance);
		return instance;

/*
		catch (DataAccessException dex)
		{
			throw new PersistenceException(dex);
		}
*/
	}


	public <T> T saveCollectionElement(String addExpression, T member, Object parent)
	{
		T instance = save(member);
		Utils.executeOgnlExpression(addExpression, member, parent);
		save(parent);
		return instance;
	}


	public void removeCollectionElement(String removeExpression, Object member, Object parent)
	{
		Utils.executeOgnlExpression(removeExpression, member, parent);
		save(parent);
		remove(member);
	}

}
