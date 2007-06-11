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


	/**
	 * (non-Javadoc)
	 *
	 * @see org.trails.persistence.PersistenceService#getInstance(Class,Serializable)
	 */
	@Transactional
	public <T> T getInstance(final Class<T> type, final Serializable id)
	{
		return (T) getHibernateTemplate().execute(new HibernateCallback()
		{
			public Object doInHibernate(Session session) throws HibernateException, SQLException
			{
				Criteria criteria = session.createCriteria(Utils.checkForCGLIB(type)).add(Expression.idEq(id));
				return alterCriteria(criteria).uniqueResult();
			}
		});
	}

	public <T> T loadInstance(final Class<T> type, Serializable id)
	{
		return (T) getHibernateTemplate().load(type, id);
	}

	/*
		 * (non-Javadoc)
		 *
		 * @see org.blah.service.IPersistenceService#getAllInstances(java.lang.Class)
		 */
	@Transactional
	public <T> List<T> getAllInstances(final Class<T> type)
	{
		return (List<T>) getHibernateTemplate().execute(new HibernateCallback()
		{
			public Object doInHibernate(Session session) throws HibernateException, SQLException
			{
				Criteria criteria = session.createCriteria(Utils.checkForCGLIB(type));
				return alterCriteria(criteria).list();
			}
		});
	}

	public <T> List<T> getInstances(Class<T> type, int startIndex, int maxResults)
	{
		return getInstances(DetachedCriteria.forClass(type), startIndex, maxResults);
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
			if (!getDescriptorService().getClassDescriptor(instance.getClass()).getHasCyclicRelationships())
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
	public void remove(Object instance)
	{
		// merge first to avoid NonUniqueObjectException
		getHibernateTemplate().delete(getHibernateTemplate().merge(instance));
	}

	/*
		 * (non-Javadoc)
		 *
		 * @see org.trails.persistence.PersistenceService#getInstances(org.trails.persistence.Query)
		 */
	@Transactional
	public List getInstances(final DetachedCriteria criteria)
	{
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
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
		return (T) getInstance(DetachedCriteria.forClass(type));
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
	public Object getInstance(final DetachedCriteria criteria)
	{
		Object result = getHibernateTemplate().execute(new HibernateCallback()
		{
			public Object doInHibernate(Session session) throws HibernateException, SQLException
			{
				Criteria executableCriteria = criteria.getExecutableCriteria(session);
				return executableCriteria.uniqueResult();
			}
		});
		return result;
	}

	@Transactional
	public List getInstances(final Object example, final IClassDescriptor classDescriptor)
	{
		return (List) getHibernateTemplate().execute(new HibernateCallback()
		{
			public Object doInHibernate(Session session) throws HibernateException, SQLException
			{
				//create Criteria instance
				Criteria searchCriteria = session.createCriteria(example.getClass());

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
								reattach(value);
								Serializable identifierValue = getIdentifier(value);
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
								String identifierName =
									getDescriptorService().getClassDescriptor(collectionDescriptor.getElementType())
										.getIdentifierDescriptor().getName();
								Collection<Serializable> identifierValues = new ArrayList<Serializable>();
								Collection associatedItems = (Collection) value;
								if (associatedItems != null && associatedItems.size() > 0)
								{
									for (Object o : associatedItems)
									{
										reattach(o);
										identifierValues.add(getIdentifier(o));
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
				return alterCriteria(searchCriteria).list();
			}
		}, true);
	}


	public void setApplicationContext(ApplicationContext arg0) throws BeansException
	{
		this.appContext = arg0;

	}


	public int count(final DetachedCriteria criteria)
	{
		// todo hacking useNative is a result of SPR-2499 and will be removed soon
		boolean useNative = getHibernateTemplate().isExposeNativeSession();
		getHibernateTemplate().setExposeNativeSession(true);
		Integer result = (Integer) getHibernateTemplate().execute(new HibernateCallback()
		{
			public Object doInHibernate(Session session) throws HibernateException, SQLException
			{
				Criteria executableCriteria =
					criteria.getExecutableCriteria(session).setProjection(Projections.rowCount());
				return alterCriteria(executableCriteria).uniqueResult();
			}
		});
		getHibernateTemplate().setExposeNativeSession(useNative);
		return result;
	}

	public List getInstances(final DetachedCriteria criteria, final int startIndex, final int maxResults)
	{
		// todo hacking useNative is a result of SPR-2499 and will be removed soon
		boolean useNative = getHibernateTemplate().isExposeNativeSession();
		getHibernateTemplate().setExposeNativeSession(true);
		List result = (List) getHibernateTemplate().execute(new HibernateCallback()
		{
			public Object doInHibernate(Session session) throws HibernateException, SQLException
			{
				Criteria executableCriteria = criteria.getExecutableCriteria(session);
				if (startIndex >= 0)
				{
					executableCriteria.setFirstResult(startIndex);
				}
				if (maxResults > 0)
				{
					executableCriteria.setMaxResults(maxResults);
				}
				return alterCriteria(executableCriteria).list();
			}
		});
		getHibernateTemplate().setExposeNativeSession(useNative);
		return result;
	}

	/**
	 * This hook allows subclasses to modify the query criteria, such as for security
	 *
	 * @param source The original Criteria query
	 * @return The modified Criteria query for execution
	 */
	protected Criteria alterCriteria(Criteria source)
	{
		return source;
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
