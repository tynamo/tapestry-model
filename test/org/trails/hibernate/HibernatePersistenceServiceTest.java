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

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jmock.MockObjectTestCase;
import org.jmock.cglib.Mock;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.orm.hibernate3.HibernateJdbcException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.trails.persistence.PersistenceException;
import org.trails.persistence.PersistenceService;
import org.trails.test.Ancestor;
import org.trails.test.Bar;
import org.trails.test.Baz;
import org.trails.test.BlogEntry;
import org.trails.test.Descendant;
import org.trails.test.Foo;


/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernatePersistenceServiceTest extends MockObjectTestCase
{
    PersistenceService persistenceService;
    HibernatePersistenceService psvcWithMockTemplate;
    
    Mock templateMock = new Mock(HibernateTemplate.class);
    ApplicationContext appContext;
    HibernateTemplate template;

    public void setUp() throws Exception
    {
        appContext = new ClassPathXmlApplicationContext(
                "applicationContext-test.xml");
        persistenceService = (PersistenceService) appContext.getBean(
                "persistenceService");
        psvcWithMockTemplate = new HibernatePersistenceService();

        template = (HibernateTemplate) templateMock.proxy();
        Field templateField = HibernateDaoSupport.class.getDeclaredField(
                "hibernateTemplate");
        templateField.setAccessible(true);
        templateField.set(psvcWithMockTemplate, template);
    }

    public void testGetAllTypes()
    {
        List types = persistenceService.getAllTypes();
        assertTrue("contains foo", types.contains(Foo.class));
        
        assertTrue("contains Ancestor", types.contains(Ancestor.class));
        assertTrue("contains Descendant", types.contains(Descendant.class));
    }

    /**
     * @JavaBean.method
     * @throws Exception
     */
    public void testQuery() throws Exception
    {
       
        Foo foo = new Foo();
        foo.setId(new Integer(1));
        foo.setName("the foo");
        persistenceService.save(foo);
        
        DetachedCriteria criteria = DetachedCriteria.forClass(Foo.class);
        criteria.add(Restrictions.eq("name", "the foo"));
        assertEquals("Got 1", 1, 
                persistenceService.getInstances(criteria).size());

    }
    
    public void testQueryByExample() throws Exception
    {
        Foo foo = new Foo();
        foo.setId(new Integer(1));
        foo.setName("the foo");
        Bar bar = new Bar();
        bar = persistenceService.save(bar); //this is the new LINE
        foo.setBar(bar);
        persistenceService.save(foo);
        Foo foo2 = new Foo();
        foo2.setName("howdya doo");
        foo2.setId(new Integer(2));
        persistenceService.save(foo2);
        
        Foo example = new Foo();
        example.setName("the foo");
        List instances = persistenceService.getInstances(example); 
        assertEquals("found 1", 1, instances.size());
        example.setName("foo");
        instances = persistenceService.getInstances(example); 
        assertEquals("found 1", 1, instances.size());
        Foo notherExample = new Foo();
        notherExample.setBar(bar);
        instances = persistenceService.getInstances(notherExample); 
        assertEquals("found 1", 1, instances.size());       
    }
    
    public void testCGLIBDoesntPuke() throws Exception
    {
        BlogEntry entry = new BlogEntry();
        entry.setText("howdy doody");
        entry = persistenceService.save(entry);
        entry = (BlogEntry)
            persistenceService.getInstance(BlogEntry.class, entry.getId());
        // the class will be cglib enhanced at this point, so don't die
        entry = (BlogEntry)
            persistenceService.getInstance(entry.getClass(), entry.getId());
        List entries = persistenceService.getAllInstances(entry.getClass());
    }
    
    public void testHibernateAnnotations() throws Exception
    {
        BlogEntry entry = new BlogEntry();
        entry.setText("howdy doody");
        entry = persistenceService.save(entry);
        assertNotNull(entry.getId());
    }
    
    public void testSaveWithException() throws Exception
    {
        
        Foo foo = new Foo();
        
        PersistenceException persistenceException = null;
        try
        {
            foo = persistenceService.save(foo);
        }
        catch (PersistenceException pex)
        {
            //pex.printStackTrace();
            persistenceException = pex;
        }
        assertNotNull("caught exception", persistenceException);
        assertNotNull("wrapped  exception", persistenceException.getCause());
    }

    public void testSaveAndRemove() throws Exception
    {
        
        Baz baz = new Baz();
        baz.setDescription("stuff");
        baz = persistenceService.save(baz);
        persistenceService.remove(baz);
        
    }
    
    public void testBazzes() throws Exception
    {
        Foo foo = new Foo();
        foo.setId(new Integer(1));
        foo.setName("boo");
        Baz baz = new Baz();
        baz.setDescription("one");
        baz.setFoo(foo);
        foo.getBazzes().add(baz);
        persistenceService.save(foo);
        
        SessionFactory sessionFactory = (SessionFactory)appContext.getBean("sessionFactory");
        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        List foos = session.createQuery("from org.trails.test.Foo").list();
        foo = (Foo)foos.get(0);
        assertEquals("1 baz", 1, foo.getBazzes().size());
    }
}
