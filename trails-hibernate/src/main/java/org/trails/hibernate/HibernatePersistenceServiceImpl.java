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
package org.trails.hibernate;

import org.apache.hivemind.util.PropertyUtils;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.persistence.HibernatePersistenceService;
import org.trails.persistence.PersistenceException;
import org.trails.util.Utils;
import org.trails.validation.ValidationException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HibernatePersistenceServiceImpl extends HibernateDaoSupport implements
		HibernatePersistenceService, ApplicationContextAware
{

	ApplicationContext appContext = null;
	private DescriptorService _descriptorService = null;

	/**
	 * We need this because cylcic reference between HibernatePersistenceServiceImpl and TrailsDescriptorService
	 */
	public DescriptorService getDescriptorService()
	{
		if (_descriptorService == null)
		{
			_descriptorService = (DescriptorService) appContext.getBean("descriptorService");
		}
		return _descriptorService;
	}

	/**
	 * https://trails.dev.java.net/servlets/ReadMsg?listName=users&msgNo=1226
	 * <p/>
	 * Very often I find myself writing: <code> Object example = new Object(); example.setProperty(uniqueValue); List
	 * objects = ((TrailsPage)getPage()).getPersistenceService().getInstances(example); (MyObject)objects.get(0); </code>
	 * when, in fact, I know that the single property I populated my example object with should be unique, and thus only
	 * one object should be returned
	 *
	 * @param type			 The type to use to check for security restrictions.
	 * @param detachedCriteria
	 * @return
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public <T> T getInstance(final Class<T> type, DetachedCriteria detachedCriteria)
	{
		final DetachedCriteria criteria = alterCriteria(type, detachedCriteria);

		return (T) getHibernateTemplate().execute(new HibernateCallback()
		{
			public Object doInHibernate(Session session) throws HibernateException, SQLException
			{
				return criteria.getExecutableCriteria(session).uniqueResult();
			}
		});
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.trails.persistence.PersistenceService#getInstance(Class,Serializable)
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public <T> T getInstance(final Class<T> type, final Serializable id)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Utils.checkForCGLIB(type)).add(Expression.idEq(id));
		return getInstance(type, criteria);
	}


	/**
	 * <strong>Description copied from:</strong> {@link org.springframework.orm.hibernate3.HibernateTemplate#load(Class,java.io.Serializable)}
	 * and {@link org.hibernate.Session#load(Class,java.io.Serializable)}
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
	 * @see org.springframework.orm.hibernate3.HibernateTemplate#load(Class,java.io.Serializable)
	 * @see org.hibernate.Session#load(Class,java.io.Serializable)
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public <T> T loadInstance(final Class<T> type, Serializable id)
	{
		return (T) getHibernateTemplate().load(type, id);
	}

	/**
	 * Execute an HQL query.
	 *
	 * @param queryString a query expressed in Hibernate's query language
	 * @return a List containing the results of the query execution
	 * @see org.springframework.orm.hibernate3.HibernateTemplate#find(String)
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List find(String queryString)
	{
		return getHibernateTemplate().find(queryString);
	}

	/**
	 * Execute an HQL query, binding one value to a "?" parameter in the query string.
	 *
	 * @param queryString a query expressed in Hibernate's query language
	 * @param value	   the query parameter
	 * @return a List containing the results of the query execution
	 * @see org.springframework.orm.hibernate3.HibernateTemplate#find(String,Object)
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List find(String queryString, Object value)
	{
		return getHibernateTemplate().find(queryString, value);
	}

	/**
	 * Execute an HQL query, binding a number of values to "?" parameters in the query string.
	 *
	 * @param queryString a query expressed in Hibernate's query language
	 * @param values	  the query parameters
	 * @return a List containing the results of the query execution
	 * @see org.springframework.orm.hibernate3.HibernateTemplate#find(String,Object[])
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List find(String queryString, Object[] values)
	{
		return getHibernateTemplate().find(queryString, values);
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see org.trails.persistence.PersistenceService#getAllInstances(java.lang.Class)
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public <T> List<T> getAllInstances(final Class<T> type)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Utils.checkForCGLIB(type));
		return getInstances(type, criteria);
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public <T> List<T> getInstances(final Class<T> type, int startIndex, int maxResults)
	{
		return getInstances(type, DetachedCriteria.forClass(type), startIndex, maxResults);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.trails.persistence.PersistenceService#save(java.lang.Object)
	 */
	@Transactional
	public <T> T save(T instance) throws ValidationException
	{
		try
		{
			IClassDescriptor iClassDescriptor = getDescriptorService().getClassDescriptor(instance.getClass());
			/* check isTransient to avoid merging on entities not persisted yet. TRAILS-33 */
			if (!iClassDescriptor.getHasCyclicRelationships() || isTransient(instance, iClassDescriptor))
			{
				getHibernateTemplate().saveOrUpdate(instance);
			} else
			{
				instance = (T) getHibernateTemplate().merge(instance);
			}
			return instance;
		}
		catch (DataAccessException dex)
		{
			throw new PersistenceException(dex);
		}
	}

	@Transactional
	public void removeAll(Collection collection)
	{
		getHibernateTemplate().deleteAll(collection);
	}

	@Transactional
	public void remove(Object instance)
	{
		getHibernateTemplate().delete(instance, LockMode.READ);
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public <T> List<T> getInstances(Class<T> type, DetachedCriteria criteria)
	{
		criteria = alterCriteria(type, criteria);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.trails.persistence.PersistenceService#getAllTypes()
	 */
	public List<Class> getAllTypes()
	{
		ArrayList<Class> allTypes = new ArrayList<Class>();
		for (Object classMetadata : getSessionFactory().getAllClassMetadata().values())
		{
			allTypes.add(((ClassMetadata) classMetadata).getMappedClass(EntityMode.POJO));
		}
		return allTypes;
	}

	@Transactional
	public void reattach(Object model)
	{
		getSession().lock(model, LockMode.NONE);
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see org.trails.persistence.PersistenceService#getInstance(Class<T>)
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public <T> T getInstance(final Class<T> type)
	{
		return (T) getInstance(type, DetachedCriteria.forClass(type));
	}

	/**
	 * Returns an entity's pk.
	 *
	 * @note (ascandroli): I tried to implement it using something like:

	 * <code>
	 *
	 * @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	 * private Serializable getIdentifier(final Object data)
	 * {
	 *   return (Serializable) getHibernateTemplate().execute(new HibernateCallback()
	 *    {
	 *      public Object doInHibernate(Session session) throws HibernateException, SQLException
	 *        {
	 *          return session.getIdentifier(data);
	 *        }
	 *    });
	 * }
	 *
	 * </code>
	 *
	 * but it didn't work.
	 * "session.getIdentifier(data)" thows TransientObjectException when the Entity is not loaded by the current session,
	 * which is pretty usual in Trails.
	 *
	 *
	 * @param data
	 * @param classDescriptor
	 * @return
	 */
	public Serializable getIdentifier(final Object data, final IClassDescriptor classDescriptor)
	{
		return (Serializable) PropertyUtils.read(data, classDescriptor.getIdentifierDescriptor().getName());
	}

	public boolean isTransient(Object data, IClassDescriptor classDescriptor)
	{
		try
		{
			return getIdentifier(data, classDescriptor) == null;
		} catch (TransientObjectException e)
		{
			return true;
		}
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List getInstances(final Object example, final IClassDescriptor classDescriptor)
	{
		return (List) getHibernateTemplate().execute(new HibernateCallback()
		{
			public Object doInHibernate(Session session) throws HibernateException, SQLException
			{
				//create Criteria instance
				DetachedCriteria searchCriteria = DetachedCriteria.forClass(Utils.checkForCGLIB(example.getClass()));
				searchCriteria = alterCriteria(example.getClass(), searchCriteria);

				//loop over the example object's PropertyDescriptors
				for (IPropertyDescriptor propertyDescriptor : classDescriptor.getPropertyDescriptors())
				{
					//only add a Criterion to the Criteria instance if this property is searchable
					if (propertyDescriptor.isSearchable())
					{
						String propertyName = propertyDescriptor.getName();
						Class propertyClass = propertyDescriptor.getPropertyType();
						Object value = PropertyUtils.read(example, propertyName);

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
										getDescriptorService().getClassDescriptor(propertyDescriptor.getBeanType()));
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
								IClassDescriptor classDescriptor = getDescriptorService().getClassDescriptor(collectionDescriptor.getElementType());
								if (classDescriptor != null)
								{
									String identifierName = classDescriptor.getIdentifierDescriptor().getName();
									Collection<Serializable> identifierValues = new ArrayList<Serializable>();
									Collection associatedItems = (Collection) value;
									if (associatedItems != null && associatedItems.size() > 0)
									{
										for (Object o : associatedItems)
										{
											identifierValues.add(getIdentifier(o, classDescriptor));
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
				return searchCriteria.getExecutableCriteria(session).list();
			}
		}, true);
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public int count(Class type, DetachedCriteria detachedCriteria)
	{
		// todo hacking useNative is a result of SPR-2499 and will be removed soon
		boolean useNative = getHibernateTemplate().isExposeNativeSession();
		getHibernateTemplate().setExposeNativeSession(true);
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		final DetachedCriteria criteria = alterCriteria(type, detachedCriteria);
		Integer result = (Integer) getHibernateTemplate().execute(new HibernateCallback()
		{
			public Object doInHibernate(Session session) throws HibernateException, SQLException
			{
				Criteria executableCriteria =
						criteria.getExecutableCriteria(session).setProjection(Projections.rowCount());
				return executableCriteria.uniqueResult();
			}
		});
		getHibernateTemplate().setExposeNativeSession(useNative);
		return result;
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public <T> List<T> getInstances(Class<T> type, final DetachedCriteria detachedCriteria, final int startIndex, final int maxResults)
	{
		return getInstances(alterCriteria(type, detachedCriteria), startIndex, maxResults);
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List getInstances(final DetachedCriteria detachedCriteria, final int startIndex, final int maxResults)
	{
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		// todo hacking useNative is a result of SPR-2499 and will be removed soon
		boolean useNative = getHibernateTemplate().isExposeNativeSession();
		getHibernateTemplate().setExposeNativeSession(true);
		List result = (List) getHibernateTemplate().execute(new HibernateCallback()
		{
			public Object doInHibernate(Session session) throws HibernateException, SQLException
			{
				Criteria executableCriteria = detachedCriteria.getExecutableCriteria(session);
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
		});
		getHibernateTemplate().setExposeNativeSession(useNative);
		return result;
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
	 * @see org.trails.persistence.HibernatePersistenceService#saveOrUpdate(java.lang.Object)
	 */
	@Transactional
	public <T> T merge(T instance)
	{
		try
		{
			return (T) getHibernateTemplate().merge(instance);
		}
		catch (DataAccessException dex)
		{
			throw new PersistenceException(dex);
		}
	}

	/**
	 * @see org.trails.persistence.HibernatePersistenceService#saveOrUpdate(java.lang.Object)
	 */
	@Transactional
	public <T> T saveOrUpdate(T instance) throws ValidationException
	{
		try
		{
			getHibernateTemplate().saveOrUpdate(instance);
			return instance;
		}
		catch (DataAccessException dex)
		{
			throw new PersistenceException(dex);
		}
	}

	@Transactional
	public <T> T saveCollectionElement(String addExpression, T member, Object parent)
	{
		T instance = save(member);
		Utils.executeOgnlExpression(addExpression, member, parent);
		save(parent);
		return instance;
	}

	@Transactional
	public void removeCollectionElement(String removeExpression, Object member, Object parent)
	{
		Utils.executeOgnlExpression(removeExpression, member, parent);
		save(parent);
		remove(member);
	}

	public void setApplicationContext(ApplicationContext arg0) throws BeansException
	{
		this.appContext = arg0;

	}
}
