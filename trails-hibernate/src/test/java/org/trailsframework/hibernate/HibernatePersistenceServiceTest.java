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
package org.trailsframework.hibernate;

import java.util.List;
import java.lang.reflect.Method;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.validator.InvalidStateException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.persistence.HibernatePersistenceService;
import org.trails.persistence.PersistenceException;
import org.trails.testhibernate.Ancestor;
import org.trails.testhibernate.Bar;
import org.trails.testhibernate.Baz;
import org.trails.testhibernate.BlogEntry;
import org.trails.testhibernate.Descendant;
import org.trails.testhibernate.Foo;
import org.trails.testhibernate.Wibble;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;


/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernatePersistenceServiceTest extends AbstractTransactionalSpringContextTests
{
	private HibernatePersistenceService persistenceService;
	private DescriptorService descriptorService;
	private SessionFactory sessionFactory;

	public void onSetUpInTransaction() throws Exception
	{
		persistenceService = (HibernatePersistenceService) applicationContext.getBean("persistenceService");
		sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");
		descriptorService = (DescriptorService) applicationContext.getBean("descriptorService");
	}

	public void testIgnoreCGLIBEnhancements()
	{
		Foo foo = new Foo();
		foo.setId(new Integer(1));
		foo.setName("boo");
		persistenceService.save(foo);
		foo = (Foo) persistenceService.getInstance(Foo.class, new Integer(1));
		assertNotNull(descriptorService.getClassDescriptor(foo.getClass()));
	}

	public void testGetAllTypes()
	{
		List types = persistenceService.getAllTypes();
		assertTrue("contains foo", types.contains(Foo.class));

		assertTrue("contains Ancestor", types.contains(Ancestor.class));
		assertTrue("contains Descendant", types.contains(Descendant.class));
	}

	/**
	 * @throws Exception
	 * @JavaBean.method
	 */
	public void testQuery() throws Exception
	{

		Foo foo = new Foo();
		foo.setId(new Integer(1));
		foo.setName("the foo");
		persistenceService.save(foo);

		DetachedCriteria criteria = DetachedCriteria.forClass(Foo.class);
		criteria.add(Restrictions.eq("name", "the foo"));
		assertEquals("Got 1", 1, persistenceService.getInstances(Foo.class, criteria).size());

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
		assertNotNull(persistenceService.getInstance(Foo.class, criteria));

		Foo foo2 = new Foo();
		foo2.setId(2);
		foo2.setName("the foo");
		foo2.setId(2);
		persistenceService.save(foo2);
		try
		{
			persistenceService.getInstance(Foo.class, criteria);
		}
		catch (IncorrectResultSizeDataAccessException e)
		{
			return;
		}
		fail("IncorrectResultSizeDataAccessExcpetion not thrown, but two results should have been found");
	}

	public void testGetIntancesWithManyToOne() throws Exception
	{
		Bar bar = new Bar();

		persistenceService.save(bar);
		Wibble wibble = new Wibble();
		persistenceService.save(wibble);
		Session session = SessionFactoryUtils.getSession(getSessionFactory(), true);
		session.flush();
		assertNotNull(wibble.getId());
		wibble.setBar(bar);
		DetachedCriteria criteria = DetachedCriteria.forClass(Wibble.class);
		criteria.add(Restrictions.eq("bar", bar));
		List list = persistenceService.getInstances(Foo.class, criteria);
		assertEquals(1, list.size());
	}

	public void testQueryByExample() throws Exception
	{
		TrailsClassDescriptor fooClassDescriptor = descriptorService.getClassDescriptor(Foo.class);

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
		List instances = persistenceService.getInstances(example, fooClassDescriptor);
		assertEquals("found 1", 1, instances.size());
		example.setName("foo");
		instances = persistenceService.getInstances(example, fooClassDescriptor);
		assertEquals("found 1", 1, instances.size());
		Foo notherExample = new Foo();
		notherExample.setBar(bar);
		instances = persistenceService.getInstances(notherExample, fooClassDescriptor);
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
		persistenceService.getAllInstances(entry.getClass());
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

	public void testMergeWithException() throws Exception
	{

		Foo foo = new Foo();

		PersistenceException persistenceException = null;
		try
		{
			foo = persistenceService.merge(foo);
		}
		catch (PersistenceException pex)
		{
			//pex.printStackTrace();
			persistenceException = pex;
		}
		assertNotNull("caught exception", persistenceException);
		assertNotNull("wrapped  exception", persistenceException.getCause());
	}

	public void testSaveOrUpdateWithException() throws Exception
	{

		Foo foo = new Foo();

		PersistenceException persistenceException = null;
		try
		{
			foo = persistenceService.saveOrUpdate(foo);
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
		try
		{
			persistenceService.save(baz);
		}
		catch (InvalidStateException e)
		{
			// success
			return;
		}
		fail(InvalidStateException.class.getSimpleName() + " should have been thrown");
	}

	public void testSaveAndRemove() throws Exception
	{

		Baz baz = new Baz();
		baz.setDescription("stuff");
		baz = persistenceService.save(baz);
		persistenceService.remove(baz);

	}

	public void testMerge() throws Exception
	{
		Baz baz = new Baz();
		baz.setDescription("whatever");
		Baz merged = persistenceService.merge(baz);
		assertNotNull(merged.getId());
		assertFalse(baz == merged);
	}

	public void testSaveOrUpdate() throws Exception
	{
		Baz baz = new Baz();
		baz.setDescription("whatever");
		Baz saved = persistenceService.saveOrUpdate(baz);
		assertNotNull(saved.getId());
		assertTrue(baz == saved);
	}

/*
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
*/

	public void testCount() throws Exception
	{
		Baz baz = new Baz();
		baz.setDescription("stuff");
		Baz baz2 = new Baz();
		baz2.setDescription("stuff2");
		persistenceService.save(baz);
		persistenceService.save(baz2);
		DetachedCriteria criteria = DetachedCriteria.forClass(Baz.class);
		assertEquals(2, persistenceService.count(Baz.class, criteria));
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
		assertEquals(1, persistenceService.getInstances(Baz.class, criteria, 0, 1).size());
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

		SessionFactory sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		List foos = session.createQuery("from org.trails.testhibernate.Foo").list();
		foo = (Foo) foos.get(0);
		assertEquals("1 baz", 1, foo.getBazzes().size());
	}

	public void testRemoveCollectionElement() throws Exception
	{
		Foo foo = new Foo();
		foo.setId(new Integer(1));
		foo.setName("boo");
		persistenceService.save(foo);

		Baz baz = new Baz();
		baz.setDescription("one");
		persistenceService.saveCollectionElement("bazzes.add", baz, foo);

		SessionFactory sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		List foos = session.createQuery("from org.trails.testhibernate.Foo").list();
		foo = (Foo) foos.get(0);
		assertEquals("1 baz", 1, foo.getBazzes().size());

		persistenceService.removeCollectionElement("bazzes.remove", baz, foo);

		foo = (Foo) foos.get(0);

		assertEquals("no bazzes", 0, foo.getBazzes().size());
	}

	public void testSaveCollectionElement() throws Exception
	{
		Foo foo = new Foo();
		foo.setId(new Integer(1));
		foo.setName("boo");
		persistenceService.save(foo);

		Baz baz = new Baz();
		baz.setDescription("one");
		persistenceService.saveCollectionElement("bazzes.add", baz, foo);

		SessionFactory sessionFactory = (SessionFactory) applicationContext.getBean("sessionFactory");
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		List foos = session.createQuery("from org.trails.testhibernate.Foo").list();
		foo = (Foo) foos.get(0);
		assertEquals("1 baz", 1, foo.getBazzes().size());
	}


	@Override
	protected String[] getConfigLocations()
	{
		return new String[]{"applicationContext-test.xml"};
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
