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

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.type.AssociationType;
import org.hibernate.type.Type;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.bsf.util.MethodUtils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;

import org.trails.TrailsRuntimeException;

import org.trails.component.Utils;

import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.DescriptorDecorator;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IDescriptorFactory;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.ObjectReferenceDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.TrailsClassDescriptor;

import java.beans.BeanInfo;
import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

import org.trails.TrailsRuntimeException;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.type.AssociationType;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.ObjectReferenceDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.TrailsPropertyDescriptor;


/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernateDescriptorDecorator
    implements DescriptorDecorator
{
    private LocalSessionFactoryBean localSessionFactoryBean;
    private List types;
    
    private IDescriptorFactory descriptorFactory;
    
    private HashMap descriptors = new HashMap();
    
    private int largeColumnLength = 100;

    /**
     * The default way to order our property descriptors is by the 
     * order they appear in the hibernate config, with id first.  Any non-mapped
     * properties are tacked on at the end, til I think of a better way.
     * @param propertyDescriptors
     * @return
     */
    protected List sortPropertyDescriptors(Class type, List propertyDescriptors)
    {
        ArrayList sortedPropertyDescriptors = new ArrayList();
        
        try
        {
            sortedPropertyDescriptors.add(Ognl.getValue("#this.{? identifier == true}[0]", propertyDescriptors));
            for (Iterator iter = getMapping(type).getPropertyIterator(); iter
                    .hasNext();)
            {
                Property mapping = (Property) iter.next();
                sortedPropertyDescriptors.addAll((List)Ognl.getValue("#this.{ ? name == \"" + mapping.getName() + "\"}", propertyDescriptors));
            }
        }
        catch(Exception ex)
        {
            throw new TrailsRuntimeException(ex);
        }
        return sortedPropertyDescriptors;
    }

    /**
     * Find the Hibernate metadata for this type, traversing up
     * the hierarchy to supertypes if necessary
     * @param type
     * @return
     */
    protected ClassMetadata findMetadata(Class type) throws MetadataNotFoundException
    {
        ClassMetadata metaData = getSessionFactory().getClassMetadata(type);
        if (metaData != null ) return metaData;
        if (metaData == null && !type.equals(Object.class))
        {
            return findMetadata(type.getSuperclass());
        }
        else throw new MetadataNotFoundException("Failed to find metadata.");
    }
    
    /**
     * @param type The class of the descriptor containing this property
     * @param descriptor the descriptor to decorate
     */
    protected IPropertyDescriptor decoratePropertyDescriptor(Class type,
        IPropertyDescriptor descriptor)
    {

        try
        {
            ClassMetadata classMetaData = findMetadata(type);
            if (descriptor.getName().equals(getIdentifierProperty(type)))
            {
                return buildIdentifierDescriptor(type, descriptor);
            }
            if (notAHibernateProperty(classMetaData, descriptor))
            {
                return descriptor;
            }
            Property mappingProperty = getMapping(type).getProperty(descriptor.getName());
            descriptor.setLength(findColumnLength(mappingProperty));
            descriptor.setLarge(isLarge(mappingProperty));
            if (!mappingProperty.isOptional())
            {
                descriptor.setRequired(true);
            }
            
            if (!mappingProperty.isInsertable() &&
                !mappingProperty.isUpdateable())
            {
                descriptor.setReadOnly(true);
            }
            Type hibernateType = classMetaData.getPropertyType(descriptor.getName());
            if (Collection.class.isAssignableFrom(descriptor.getPropertyType()))
            {
                return buildCollectionDescriptor(type, descriptor);
            }else if (hibernateType.isAssociationType())
            {
                return buildReferenceDescriptor(type, descriptor,
                    (AssociationType) hibernateType);
            }
            
        }catch (HibernateException e)
        {
            throw new TrailsRuntimeException(e);
        }
        return descriptor;
    }

    private boolean isLarge(Property mappingProperty)
    {
        // Hack to avoid setting large property if length
        // is exactly equal to Hibernate default column length
        return findColumnLength(mappingProperty) != Column.DEFAULT_LENGTH &&
            findColumnLength(mappingProperty) > getLargeColumnLength();
    }

    private int findColumnLength(Property mappingProperty)
    {
        int length = 0;
        for (Iterator iter = mappingProperty.getColumnIterator(); iter.hasNext();)
        {
            Column column = (Column) iter.next();
            length += column.getLength();
        }
        return length;
    }

    /**
     * @param classMetaData
     * @param type
     * @return
     */
    protected boolean notAHibernateProperty(ClassMetadata classMetaData, IPropertyDescriptor descriptor)
    {
        try
        {
            return ((Boolean)Ognl.getValue("propertyNames.{ ? #this == \"" +
                descriptor.getName() + "\"}.size() == 0", classMetaData)).booleanValue();
        }
        catch (OgnlException oe)
        {
            throw new TrailsRuntimeException(oe);
        }
    }

    /**
     * @param descriptor
     * @param type
     * @return
     */
    private IPropertyDescriptor buildReferenceDescriptor(
       Class beanType, IPropertyDescriptor descriptor, AssociationType type)
    {
        return new ObjectReferenceDescriptor(beanType, descriptor, type.getReturnedClass());
    }

    /**
     * @param type
     * @param descriptor
     * @return
     */
    private IdentifierDescriptor buildIdentifierDescriptor(Class type,
        IPropertyDescriptor descriptor)
    {
        IdentifierDescriptor identifierDescriptor = new IdentifierDescriptor(type, descriptor);
        PersistentClass mapping = getMapping(type);

        if (((SimpleValue)mapping.getIdentifier()).getIdentifierGeneratorStrategy().equals("assigned"))
        {
            identifierDescriptor.setGenerated(false);
        }

        return identifierDescriptor;
    }

    /**
     * @param type
     * @return
     */
    protected PersistentClass getMapping(Class type)
    {
        Configuration cfg = getLocalSessionFactoryBean().getConfiguration();
        PersistentClass mapping = cfg.getClassMapping(type.getName());

        return mapping;
    }

    /**
     * @param type
     * @param newDescriptor
     */
    private CollectionDescriptor buildCollectionDescriptor(Class type,
        IPropertyDescriptor descriptor)
    {
        try
        {
            Map allCollectionMeta = getSessionFactory().getAllCollectionMetadata();
            CollectionDescriptor collectionDescriptor = new CollectionDescriptor(type, descriptor);
            org.hibernate.mapping.Collection collectionMapping = findCollectionMapping(type, descriptor.getName());
            // It is a child relationship if it has delete-orphan specified in the mapping
            collectionDescriptor.setChildRelationship(collectionMapping.hasOrphanDelete());
            CollectionMetadata collectionMetaData = getSessionFactory()
                                                        .getCollectionMetadata(collectionMapping.getRole());
                
            collectionDescriptor.setElementType(collectionMetaData.getElementType()
                                                                  .getReturnedClass());

            return collectionDescriptor;
        }catch (HibernateException e)
        {
            throw new TrailsRuntimeException(e);
        }
    }
    
    protected org.hibernate.mapping.Collection findCollectionMapping(Class type, String name)
    {
        String roleName = type.getName() + "." + name;
        org.hibernate.mapping.Collection collectionMapping = 
            getLocalSessionFactoryBean().getConfiguration().getCollectionMapping(roleName);
        if (collectionMapping != null)
        {
            return collectionMapping;
        }
        else if (!type.equals(Object.class))
        {
            return findCollectionMapping(type.getSuperclass(), name);
        }
        else 
        {
            throw new MetadataNotFoundException("Metadata not found.");
        }

    }

    /* (non-Javadoc)
     * @see org.trails.descriptor.PropertyDescriptorService#getIdentifierProperty(java.lang.Class)
     */
    public String getIdentifierProperty(Class type)
    {
        try
        {
            Map allMeta = getSessionFactory().getAllClassMetadata();

            return getSessionFactory().getClassMetadata(type)
                       .getIdentifierPropertyName();
        }catch (HibernateException e)
        {
            throw new TrailsRuntimeException(e);
        }
    }

    /**
     * @return Returns the sessionFactory.
     */
    public SessionFactory getSessionFactory()
    {
        return (SessionFactory) getLocalSessionFactoryBean().getObject();
    }

    public IClassDescriptor getClassDescriptor(Class type)
    {
        return (IClassDescriptor)descriptors.get(type);
    }
    
    /**
     * @return Returns the localSessionFactoryBean.
     */
    public LocalSessionFactoryBean getLocalSessionFactoryBean()
    {
        return localSessionFactoryBean;
    }

    /**
     * @param localSessionFactoryBean The localSessionFactoryBean to set.
     */
    public void setLocalSessionFactoryBean(
        LocalSessionFactoryBean localSessionFactoryBean)
    {
        this.localSessionFactoryBean = localSessionFactoryBean;
    }

    /* (non-Javadoc)
     * @see org.trails.descriptor.TrailsDescriptorService#getAllDescriptors()
     */
    public List getAllDescriptors()
    {
        return new ArrayList(descriptors.values());
    }

    public IDescriptorFactory getDescriptorFactory()
    {
        return descriptorFactory;
    }
    

    public void setDescriptorFactory(IDescriptorFactory descriptorFactory)
    {
        this.descriptorFactory = descriptorFactory;
    }

    public List getTypes()
    {
        return types;
    }
    

    public void setTypes(List types)
    {
        this.types = types;
    }

    public IClassDescriptor decorate(IClassDescriptor descriptor)
    {
        ArrayList decoratedPropertyDescriptors = new ArrayList();
        for (Iterator iter = descriptor.getPropertyDescriptors().iterator(); iter.hasNext();)
        {
            IPropertyDescriptor propertyDescriptor = 
                (IPropertyDescriptor) iter.next();
            decoratedPropertyDescriptors.add(decoratePropertyDescriptor(
                    descriptor.getType(), propertyDescriptor));
        }
        descriptor.setPropertyDescriptors(decoratedPropertyDescriptors);
        return descriptor;
    }

    public int getLargeColumnLength()
    {
        return largeColumnLength;
    }
    
    /**
     * Columns longer than this will have their large property set
     * to true.
     * @param largeColumnLength 
     */
    public void setLargeColumnLength(int largeColumnLength)
    {
        this.largeColumnLength = largeColumnLength;
    }
    
    
}
