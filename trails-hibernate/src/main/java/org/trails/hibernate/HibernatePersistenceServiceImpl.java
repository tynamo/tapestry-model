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

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ognl.Ognl;
import ognl.OgnlException;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.TransientObjectException;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import org.trails.component.Utils;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.persistence.HibernatePersistenceService;
import org.trails.persistence.PersistenceException;
import org.trails.validation.ValidationException;

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

	@Transactional
	public <T> T getInstance(final Class<T> type, DetachedCriteria detachedCriteria) {
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
	@Transactional
	public <T> T getInstance(final Class<T> type, final Serializable id)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Utils.checkForCGLIB(type)).add(Expression.idEq(id));
		return getInstance(type, criteria);
	}

	public <T> T loadInstance(final Class<T> type, Serializable id)
	{
		return (T) getHibernateTemplate().load(type, id);
	}
	
	public List find(String queryString) {
		return getHibernateTemplate().find(queryString);
	}
	public List find(String queryString, Object value) {
		return getHibernateTemplate().find(queryString, value);
	}
	public List find(String queryString, Object[] values) {
		return getHibernateTemplate().find(queryString, values);
	}

	
	/*
		 * (non-Javadoc)
		 *
		 * @see org.blah.service.IPersistenceService#getAllInstances(java.lang.Class)
		 */
	@Transactional
	public <T> List<T> getAllInstances(final Class<T> type)
	{
		DetachedCriteria criteria = DetachedCriteria.forClass(Utils.checkForCGLIB(type));
		return getInstances(type, criteria);
	}

	public <T> List<T> getInstances(final Class<T> type, int startIndex, int maxResults)
	{
		return getInstances(type, DetachedCriteria.forClass(type), startIndex, maxResults);
	}

	/*
		 * (non-Javadoc)
		 *
		 * @see org.blah.service.IPersistenceService#save(java.lang.Object)
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
		// merge first to avoid NonUniqueObjectException
		getHibernateTemplate().delete(getHibernateTemplate().merge(instance));
	}

	@Transactional
	public <T> List<T> getInstances(Class<T> type, DetachedCriteria criteria) {
		criteria = alterCriteria(type, criteria);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
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
	@Transactional
	public <T> T getInstance(final Class<T> type)
	{
		return (T) getInstance(type, DetachedCriteria.forClass(type));
	}

	public Serializable getIdentifier(final Object data, final IClassDescriptor classDescriptor)
	{
		try
		{
			/** This is only until I figure out where are the Callbacks persisting its properties **/

			return (Serializable) Ognl.getValue(classDescriptor.getIdentifierDescriptor().getName(), data);

		} catch (OgnlException e)
		{
			return null;
		}
	}


	private Serializable getIdentifier(final Object data)
	{
		return (Serializable) getHibernateTemplate().execute(new HibernateCallback()
		{
			public Object doInHibernate(Session session) throws HibernateException, SQLException
			{
				return session.getIdentifier(data);
			}
		});
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

	@Transactional
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
						Object value = null;
						try
						{
							value = Ognl.getValue(propertyName, example);
						} catch (OgnlException e)
						{ /* do nothing! */ }

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
				searchCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
				return searchCriteria.getExecutableCriteria(session).list();
			}
		}, true);
	}


	public void setApplicationContext(ApplicationContext arg0) throws BeansException
	{
		this.appContext = arg0;

	}


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

	public <T> List<T> getInstances(Class<T> type, final DetachedCriteria detachedCriteria, final int startIndex, final int maxResults)
	{
		return getInstances(alterCriteria(type, detachedCriteria), startIndex, maxResults);
		
	}
	
	
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
}
