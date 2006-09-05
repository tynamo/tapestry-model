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

import ognl.Ognl;
import ognl.OgnlException;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
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
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.persistence.PersistenceException;
import org.trails.persistence.PersistenceService;
import org.trails.validation.ValidationException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style -
 *         Code Templates
 */
public class HibernatePersistenceService extends HibernateDaoSupport implements
                                                                     PersistenceService, ApplicationContextAware
{
    private ApplicationContext appContext;

    /*
    * (non-Javadoc)
    *
    * @see org.blah.service.IPersistenceService#getInstance(java.lang.Class,
    *      java.io.Serializable)
    */
    @Transactional
    public <T> T getInstance( final Class<T> type, final Serializable id )
    {
        return (T) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(Utils.checkForCGLIB(type)).add(Expression.idEq(id));
                return alterCriteria(criteria).uniqueResult();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.blah.service.IPersistenceService#getAllInstances(java.lang.Class)
     */
    @Transactional
    public <T> List<T> getAllInstances(final Class<T> type)
    {
        return (List<T>)getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria criteria = session.createCriteria(Utils.checkForCGLIB(type));
                return alterCriteria(criteria).list();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.blah.service.IPersistenceService#save(java.lang.Object)
     */
    @Transactional
    public <T> T save( T instance ) throws ValidationException
    {
        try
        {
            getHibernateTemplate().saveOrUpdate(instance);
            return instance;
        }
        catch( DataAccessException dex )
        {
            throw new PersistenceException( dex );
        }

    }

    @Transactional
    public void remove( Object instance )
    {
        // merge first to avoid NonUniqueObjectException
        getHibernateTemplate().delete( getHibernateTemplate().merge( instance ) );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.trails.persistence.PersistenceService#getInstances(org.trails.persistence.Query)
     */
    @Transactional
    public List getInstances( final DetachedCriteria criteria )
    {
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return getHibernateTemplate().findByCriteria(criteria);
    }

    public List<Class> getAllTypes()
    {
        ArrayList<Class> allTypes = new ArrayList<Class>();
        for (Object classMetadata : getSessionFactory().getAllClassMetadata().values()) {
            allTypes.add(((ClassMetadata) classMetadata).getMappedClass(EntityMode.POJO));
        }
        return allTypes;
    }

    @Transactional
    public void reattach( Object model )
    {
        getSession().lock( model, LockMode.NONE );

    }

    @Transactional
    public <T> T getInstance(final DetachedCriteria criteria)
    {
        // todo hacking useNative is a result of SPR-2499 and will be removed soon
        boolean useNative = getHibernateTemplate().isExposeNativeSession();
        getHibernateTemplate().setExposeNativeSession(true);
        Object result = getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria executableCriteria = criteria.getExecutableCriteria(session);
                return executableCriteria.uniqueResult();
            }
        });
        getHibernateTemplate().setExposeNativeSession(useNative);
        return (T) result;
    }
    
    @Transactional
    public List getInstances( final Object example )
    {
        return ( List ) getHibernateTemplate().execute( new HibernateCallback()
        {
            public Object doInHibernate( Session session ) throws HibernateException, SQLException
            {
                //create Criteria instance
                Criteria searchCriteria = session.createCriteria( example.getClass() );

                //loop over the example object's PropertyDescriptors
                DescriptorService descriptorService = ( DescriptorService ) appContext.getBean( "descriptorService" );
                Iterator propertyDescriptorIterator =
                        descriptorService.getClassDescriptor( example.getClass() ).getPropertyDescriptors().iterator();
                while( propertyDescriptorIterator.hasNext() )
                {
                    IPropertyDescriptor propertyDescriptor = ( IPropertyDescriptor ) propertyDescriptorIterator.next();

                    //only add a Criterion to the Criteria instance if this property is searchable
                    if( propertyDescriptor.isSearchable() )
                    {
                        try
                        {
                            String propertyName = propertyDescriptor.getName();
                            Class propertyClass = propertyDescriptor.getPropertyType();
                            Object value = Ognl.getValue( propertyName, example );

                            //only add a Criterion to the Criteria instance if the value for this property is non-null
                            if( value != null )
                            {
                                if( String.class.isAssignableFrom( propertyClass ) &&
                                    ( ( String ) value ).length() > 0 )
                                {
                                    searchCriteria.add( Restrictions.like( propertyName, value.toString(),
                                                                           MatchMode.ANYWHERE ) );
                                }
                                else if( propertyDescriptor.isObjectReference() )
                                {
                                    //'one'-end of many-to-one -> just match the identifier
                                    Object identifierValue = Ognl.getValue( descriptorService
                                            .getClassDescriptor( value.getClass() ).getIdentifierDescriptor().getName(),
                                                                            value );
                                    searchCriteria.createCriteria( propertyName )
                                            .add( Restrictions.idEq( identifierValue ) );
                                }
                                else if( propertyClass.isPrimitive() )
                                {
                                    //primitive types: ignore zeroes in case of numeric types, ignore booleans anyway (TODO come up with something...)
                                    if( !propertyClass.equals( boolean.class ) &&
                                        ( ( Number ) value ).longValue() != 0 )
                                    {
                                        searchCriteria.add( Restrictions.eq( propertyName, value ) );
                                    }
                                }
                                else if( Collection.class.isInstance( value ) )
                                { //one-to-many or many-to-many
                                    Collection associatedItems = ( Collection ) value;
                                    if( associatedItems != null && associatedItems.size() > 0 )
                                    {
                                        Iterator itemsIterator = associatedItems.iterator();

                                        //get the first item in order to determine the identifying property for the associated class
                                        Object item = itemsIterator.next();
                                        String identifierName = descriptorService.getClassDescriptor( item.getClass() )
                                                .getIdentifierDescriptor().getName();

                                        //build the collection of allowed values for the identifier
                                        Collection values = new ArrayList();

                                        //add the first item that was already retrieved from the iterator
                                        values.add( Ognl.getValue( identifierName, item ) );

                                        //add the other items
                                        while( itemsIterator.hasNext() )
                                        {
                                            values.add( Ognl.getValue( identifierName, itemsIterator.next() ) );
                                        }

                                        //add a 'value IN collection' restriction
                                        searchCriteria.createCriteria( propertyName )
                                                .add( Restrictions.in( identifierName, values ) );

                                    }
                                }
                            }
                        }
                        catch( OgnlException e )
                        {
                            //e.printStackTrace();//TODO properly implent logging!!!
                        }
                    }
                }
                searchCriteria.setResultTransformer( CriteriaSpecification.DISTINCT_ROOT_ENTITY );
                return alterCriteria(searchCriteria).list();
            }
        }, true );
    }

    public void setApplicationContext( ApplicationContext arg0 ) throws BeansException
    {
        this.appContext = arg0;

    }

    public int count(final DetachedCriteria criteria)
    {
        // todo hacking useNative is a result of SPR-2499 and will be removed soon
        boolean useNative = getHibernateTemplate().isExposeNativeSession();
        getHibernateTemplate().setExposeNativeSession(true);
        Integer result = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria executableCriteria = criteria.getExecutableCriteria(session).setProjection(Projections.rowCount());
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
        List result = (List) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Criteria executableCriteria = criteria.getExecutableCriteria(session);
                if (startIndex >= 0) {
                    executableCriteria.setFirstResult(startIndex);
                }
                if (maxResults > 0) {
                    executableCriteria.setMaxResults(maxResults);
                }
                return alterCriteria(executableCriteria).list();
            }
        });
        getHibernateTemplate().setExposeNativeSession(useNative);
        return result;
    }

    public <T> T reload(T instance)
    {
        final DescriptorService descriptorService = (DescriptorService)appContext.getBean("descriptorService");
        IClassDescriptor classDescriptor = descriptorService.getClassDescriptor(instance.getClass());
        try
        {
            Serializable id = (Serializable)
                Ognl.getValue(classDescriptor.getIdentifierDescriptor().getName(), instance);
            return (T)getHibernateTemplate().load(instance.getClass(), id);
        }
        catch(OgnlException oe)
        {
            throw new PersistenceException(oe);
        }
    }

    /**
     * This hook allows subclasses to modify the query criteria, such as for security
     * @param source The original Criteria query
     * @return The modified Criteria query for execution
     */
    protected Criteria alterCriteria(Criteria source) {
        return source;
    }
}
