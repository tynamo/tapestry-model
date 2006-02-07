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
import org.hibernate.criterion.MatchMode;
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
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.persistence.PersistenceException;
import org.trails.persistence.PersistenceService;
import org.trails.validation.ValidationException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
    public <T> T getInstance( Class<T> type, Serializable id )
    {
        return ( T ) getHibernateTemplate().load( Utils.checkForCGLIB( type ), id );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.blah.service.IPersistenceService#getAllInstances(java.lang.Class)
     */
    @Transactional
    public List getAllInstances( Class type )
    {
        // return getHibernateTemplate().find("from "+ type.getName());
        return new ArrayList( new HashSet( getHibernateTemplate().loadAll( Utils.checkForCGLIB( type ) ) ) );
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
            return ( T ) getHibernateTemplate().merge( instance );
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
        // TODO Auto-generated method stub
        return ( List ) getHibernateTemplate().execute( new HibernateCallback()
        {
            public Object doInHibernate( Session session )
                    throws HibernateException, SQLException
            {
                return criteria.getExecutableCriteria( session ).list();
            }
        }, true );
    }

    public List getAllTypes()
    {
        ArrayList allTypes = new ArrayList();
        for( Iterator iter = getSessionFactory().getAllClassMetadata().values()
                .iterator(); iter.hasNext(); )
        {
            ClassMetadata classMeta = ( ClassMetadata ) iter.next();
            allTypes.add( classMeta.getMappedClass( EntityMode.POJO ) );
        }
        return allTypes;
    }

    @Transactional
    public void reattach( Object model )
    {
        getSession().lock( model, LockMode.NONE );

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
                            e.printStackTrace();//TODO properly implent logging!!!
                        }
                    }
                }
                searchCriteria.setResultTransformer( CriteriaSpecification.DISTINCT_ROOT_ENTITY );
                return searchCriteria.list();
            }
        }, true );
    }

    public void setApplicationContext( ApplicationContext arg0 ) throws BeansException
    {
        this.appContext = arg0;

    }
}
