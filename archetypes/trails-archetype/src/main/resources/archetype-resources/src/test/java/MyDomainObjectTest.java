package $packageName;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.validator.InvalidStateException;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.trails.persistence.HibernatePersistenceService;
import org.trails.validation.HibernateClassValidatorFactory;
import org.trails.i18n.SpringMessageSource;
import org.trails.i18n.TestLocaleHolder;


/**
 * Class for MyDomainObject tests. <p/> <p> This class extends AbstractTransactionalDataSourceSpringContextTests, one of
 * the valuable test superclasses provided in the org.springframework.test package. This represents best practice for
 * integration tests with Spring. The AbstractTransactionalDataSourceSpringContextTests superclass provides the
 * following services: </p> <p/> <li>Injects test dependencies, meaning that we don't need to perform application
 * context lookups. Injection uses autowiring by type.</li> <p/> <li>Executes each test method in its own transaction,
 * which is automatically rolled back by default. This means that even if tests insert or otherwise change database
 * state, there is no need for a teardown or cleanup script.</li> <p/> <ul> <li>If you want a transaction to
 * commit--unusual, but useful if you want a particular test to populate the database, for example--you can call the
 * setComplete() method inherited from AbstractTransactionalSpringContextTests. This will cause the transaction to
 * commit instead of roll back.</li> <p/> <li>There is also convenient ability to end a transaction before the test case
 * ends, through calling the endTransaction() method. This will roll back the transaction by default, and commit it only
 * if setComplete() had previously been called.</li> </ul> <p/> <li>Provides useful inherited protected fields, such as
 * a JdbcTemplate that can be used to verify database state after test operations, or verify the results of queries
 * performed by application code. An ApplicationContext is also inherited, and can be used for explicit lookup if
 * necessary.</li> <p> The AbstractTransactionalDataSourceSpringContextTests and related classes are shipped in the
 * spring-mock.jar. </p>
 * <p/>
 * http://www.theserverside.com/tt/articles/article.tss?l=PersistentDomain http://static.springframework.org/spring/docs/2.0.x/reference/testing.html
 *
 * @see org.springframework.samples.petclinic.AbstractClinicTests
 * @see org.springframework.test.AbstractTransactionalDataSourceSpringContextTests
 */
public class MyDomainObjectTest extends
		AbstractTransactionalDataSourceSpringContextTests
{

	protected void onSetUp() throws Exception
	{
		TestLocaleHolder testLocaleHolder = new TestLocaleHolder();

		HibernateClassValidatorFactory hibernateClassValidatorFactory =
				((HibernateClassValidatorFactory) applicationContext.getBean("hibernateClassValidatorFactory"));

		SpringMessageSource springMessageSource = ((SpringMessageSource) applicationContext.getBean("trailsMessageSource")); 

		hibernateClassValidatorFactory.setLocaleHolder(testLocaleHolder);
		springMessageSource.setLocaleHolder(testLocaleHolder);
	}

	@Override
	protected String[] getConfigLocations()
	{
		return new String[]{"applicationContext.xml"};
	}

	HibernatePersistenceService persistenceService;

	public void setPersistenceService(HibernatePersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}

	public void testCreate()
	{
		MyDomainObject myDomainObject = new MyDomainObject();
		myDomainObject.setName("some name");
		myDomainObject = persistenceService.save(myDomainObject);
		assertEquals(myDomainObject.getName(), "some name");
		assertNotNull(myDomainObject.getId());
	}

	public void testCreateShouldFailWithNullName()
	{
		MyDomainObject myDomainObject = new MyDomainObject();
		try
		{
			myDomainObject = persistenceService.save(myDomainObject);
			fail();
		} catch (InvalidStateException e)
		{
			assertTrue(e.getInvalidValues().length == 1);
			assertEquals("name can't be null", e.getInvalidValues()[0].getMessage());
		} catch (Exception e) {
			fail();
		}
	}


	public void testRetrieve()
	{
		MyDomainObject myDomainObject = new MyDomainObject();
		myDomainObject.setName("some name");
		myDomainObject = persistenceService.save(myDomainObject);
		assertEquals(myDomainObject.getName(), "some name");
		assertNotNull(myDomainObject.getId());

		MyDomainObject myReturnedDomainObject;
		myReturnedDomainObject = persistenceService.getInstance(MyDomainObject.class, myDomainObject.getId());

		assertEquals(myDomainObject, myReturnedDomainObject);
		assertEquals(myDomainObject.getName(), myReturnedDomainObject.getName());

	}

	public void testUpdate()
	{
		MyDomainObject myDomainObject = new MyDomainObject();
		myDomainObject.setName("some name");
		myDomainObject = persistenceService.save(myDomainObject);
		assertEquals(myDomainObject.getName(), "some name");
		assertNotNull(myDomainObject.getId());

		MyDomainObject myReturnedDomainObject;

		myReturnedDomainObject = persistenceService.getInstance(MyDomainObject.class, myDomainObject.getId());

		assertEquals(myDomainObject, myReturnedDomainObject);
		assertEquals(myDomainObject.getName(), myReturnedDomainObject.getName());

		myReturnedDomainObject.setName("some other name");
		Integer id = myReturnedDomainObject.getId();

		myReturnedDomainObject = persistenceService.save(myReturnedDomainObject);

		assertEquals(id, myReturnedDomainObject.getId());
		assertEquals("some other name", myReturnedDomainObject.getName());
	}

	public void testDelete()
	{
		MyDomainObject myDomainObject = new MyDomainObject();
		myDomainObject.setName("some name");
		myDomainObject = persistenceService.save(myDomainObject);
		assertEquals(myDomainObject.getName(), "some name");
		assertNotNull(myDomainObject.getId());

		Integer id = myDomainObject.getId();

		persistenceService.remove(myDomainObject);
		MyDomainObject myReturnedDomainObject = null;
		try
		{
			myReturnedDomainObject = persistenceService.getInstance(MyDomainObject.class, id);
		} catch (HibernateObjectRetrievalFailureException e)
		{
		}

		assertNull(myReturnedDomainObject);
	}

	public void testGetAllInstances()
	{
		MyDomainObject firstDomainObject = new MyDomainObject();
		firstDomainObject.setName("this is the first one");
		firstDomainObject = persistenceService.save(firstDomainObject);

		MyDomainObject secondDomainObject = new MyDomainObject();
		secondDomainObject.setName("this is the second object");
		secondDomainObject = persistenceService.save(secondDomainObject);

		List<MyDomainObject> objectList = persistenceService.getAllInstances(MyDomainObject.class);

		assertFalse(objectList.isEmpty());
		int i = objectList.indexOf(firstDomainObject);
		assertTrue(i >= 0);

		assertFalse(objectList.isEmpty());
		int j = objectList.indexOf(secondDomainObject);
		assertTrue(j >= 0);

		MyDomainObject myReturnedDomainObject;

		myReturnedDomainObject = objectList.get(i);

		assertEquals(firstDomainObject, myReturnedDomainObject);
		assertEquals(firstDomainObject.getName(), myReturnedDomainObject.getName());
	}

	public void testSearchByDetachedCriteria()
	{
		MyDomainObject firstDomainObject = new MyDomainObject();
		firstDomainObject.setName("this is the first one");
		firstDomainObject = persistenceService.save(firstDomainObject);

		MyDomainObject secondDomainObject = new MyDomainObject();
		secondDomainObject.setName("this is the second object");
		secondDomainObject = persistenceService.save(secondDomainObject);

		DetachedCriteria criteria = DetachedCriteria.forClass(MyDomainObject.class);
		criteria.add(Restrictions.like("name", "first", MatchMode.ANYWHERE));

		List<MyDomainObject> objectList = (List<MyDomainObject>) persistenceService.getInstances(MyDomainObject.class, criteria);

		assertFalse(objectList.isEmpty());
		int i = objectList.indexOf(firstDomainObject);
		assertTrue(i >= 0);

		int j = objectList.indexOf(secondDomainObject);
		assertTrue(j == -1);

		MyDomainObject myReturnedDomainObject;

		myReturnedDomainObject = objectList.get(i);

		assertEquals(firstDomainObject, myReturnedDomainObject);
		assertEquals(firstDomainObject.getName(), myReturnedDomainObject.getName());
	}
}