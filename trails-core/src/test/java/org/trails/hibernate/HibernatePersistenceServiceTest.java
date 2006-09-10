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

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.validator.InvalidStateException;
import org.jmock.cglib.Mock;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.trails.descriptor.DescriptorService;
import org.trails.persistence.PersistenceException;
import org.trails.persistence.PersistenceService;
import org.trails.security.domain.Role;
import org.trails.security.domain.User;
import org.trails.test.Ancestor;
import org.trails.test.Bar;
import org.trails.test.Baz;
import org.trails.test.BlogEntry;
import org.trails.test.Descendant;
import org.trails.test.Foo;
import org.trails.test.Wibble;


/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernatePersistenceServiceTest extends AbstractTransactionalSpringContextTests
{
    PersistenceService persistenceService;
    HibernatePersistenceService psvcWithMockTemplate;
    
    Mock templateMock = new Mock(HibernateTemplate.class);
    
    HibernateTemplate template;

    private SessionFactory sessionFactory;
    
    public void onSetUpInTransaction() throws Exception
    {
        persistenceService = (PersistenceService) applicationContext.getBean(
                "persistenceService");
        sessionFactory = (SessionFactory)applicationContext.getBean("sessionFactory");
    }

    public void testIgnoreCGLIBEnhancements()
    {
        PersistenceService psvc = (PersistenceService)applicationContext.getBean("persistenceService");
        DescriptorService descSvc = (DescriptorService)applicationContext.getBean("descriptorService");
        Foo foo = new Foo();
        foo.setId(new Integer(1));
        foo.setName("boo");
        psvc.save(foo);
        foo = (Foo)psvc.getInstance(Foo.class, new Integer(1));
        assertNotNull(descSvc.getClassDescriptor(foo.getClass()));
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

    /**
     * @throws Exception
     */
    public void testSingleResultQuery() throws Exception
    {
       
        Foo foo = new Foo();
        foo.setId(1);
        foo.setName("the foo");
        persistenceService.save(foo);
        
        DetachedCriteria criteria = DetachedCriteria.forClass(Foo.class);
        criteria.add(Restrictions.eq("name", "the foo"));
        assertNotNull(persistenceService.getInstance(criteria));
        
        Foo foo2 = new Foo();
        foo2.setId(2);
        foo2.setName("the foo");
        foo2.setId(2);
        persistenceService.save(foo2);
        try {
        	persistenceService.getInstance(criteria);
        }
        catch (IncorrectResultSizeDataAccessException e) {
        	return;
        }
        fail("NonUniqueResultException not thrown, but two results should have been found");
    }
    
    public void testGetIntancesWithManyToOne() throws Exception
    {
		Bar bar = new Bar();
		
		persistenceService.save(bar);
        Wibble wibble = new Wibble();
        persistenceService.save(wibble);
        Session session =SessionFactoryUtils.getSession(getSessionFactory(), true);
        session.flush();
        assertNotNull(wibble.getId());
		wibble.setBar(bar);
		DetachedCriteria criteria = DetachedCriteria.forClass(Wibble.class);
		criteria.add(Restrictions.eq("bar", bar));
		List list = persistenceService.getInstances(criteria);
		assertEquals(1, list.size());
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

    public void testValidation() throws Exception
    {
        Baz baz = new Baz();
        InvalidStateException invalidStateException = null;
        try
        {
            persistenceService.save(baz);
        }
        catch (InvalidStateException ie)
        {
            invalidStateException = ie;
        }
        assertNotNull(invalidStateException);
    }
    
    public void testSaveAndRemove() throws Exception
    {
        
        Baz baz = new Baz();
        baz.setDescription("stuff");
        baz = persistenceService.save(baz);
        persistenceService.remove(baz);
        
    }
    
    public void testReload() throws Exception
    {
        Foo foo = new Foo();
        foo.setId(1);
        foo.setName("foo");
        persistenceService.save(foo);
        Foo foo2 = new Foo();
        foo2.setId(1);
        foo2.setName("otherfoo");
        Foo loadedFoo = persistenceService.reload(foo2);
        assertEquals("foo", loadedFoo.getName());
    }
    
    public void testCount() throws Exception
    {
        Baz baz = new Baz();
        baz.setDescription("stuff");
        Baz baz2 = new Baz();
        baz2.setDescription("stuff2");
        persistenceService.save(baz);
        persistenceService.save(baz2);
        DetachedCriteria criteria = DetachedCriteria.forClass(Baz.class);
        assertEquals(2, persistenceService.count(criteria));
    }
    
    public void testGetInstancesWithLimit() throws Exception
    {
        Baz baz = new Baz();
        baz.setDescription("stuff");
        Baz baz2 = new Baz();
        baz2.setDescription("stuff2");
        persistenceService.save(baz);
        persistenceService.save(baz2);
        DetachedCriteria criteria = DetachedCriteria.forClass(Baz.class);
        assertEquals(1, persistenceService.getInstances(criteria, 0, 1).size());        
    }
    
    public void testInheritance() throws Exception
    {
    	Descendant descendant = new Descendant();
    	descendant.setName("what");
    	descendant = persistenceService.save(descendant);
    }
    
    public void testGetInstance() throws Exception
    {
    	Foo foo = new Foo();
    	foo.setId(new Integer(1));
    	persistenceService.save(foo);
    	assertNotNull(persistenceService.getInstance(Foo.class, new Integer(1)));
    	assertNull(persistenceService.getInstance(Foo.class, new Integer(-999)));
    }
    
    public void testGetInstances() throws Exception
    {
    	User user = new User();
    	Role role = new Role();
    	role.setName("one");
    	role.setDescription("one");
    	Role role2 = new Role();
    	role2.setName("two");
    	role2.setDescription("two");
    	role = persistenceService.save(role);
    	role2 = persistenceService.save(role2);
    	user.setUsername("user");
    	user.setFirstName("what");
    	user.setLastName("blah");
    	user.setPassword("user");
    	user.setConfirmPassword("user");
    	user.getRoles().add(role);
    	user.getRoles().add(role2);
    	persistenceService.save(user);
    	assertEquals(1, persistenceService.getInstances(DetachedCriteria.forClass(User.class)).size());
    	
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
        
        SessionFactory sessionFactory = (SessionFactory)applicationContext.getBean("sessionFactory");
        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        List foos = session.createQuery("from org.trails.test.Foo").list();
        foo = (Foo)foos.get(0);
        assertEquals("1 baz", 1, foo.getBazzes().size());
    }

	@Override
	protected String[] getConfigLocations()
	{
		return new String[] {"applicationContext-test.xml"};
	}

    public SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }
}
