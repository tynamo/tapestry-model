package org.trailsframework.validation;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.trails.persistence.HibernatePersistenceService;
import org.trails.test.TestUtils;
import org.trails.testhibernate.Bar;
import org.trails.testhibernate.Baz;
import org.trails.testhibernate.Foo;
import org.trails.testhibernate.Wibble;

public class AssertNoOrphansTest extends
	AbstractTransactionalSpringContextTests
{


	public AssertNoOrphansTest()
	{
		super();
		setAutowireMode(AbstractDependencyInjectionSpringContextTests.AUTOWIRE_BY_NAME);
	}

	@Override
	protected String[] getConfigLocations()
	{
		// TODO Auto-generated method stub
		return new String[]{"applicationContext-test.xml"};
	}

	private HibernatePersistenceService persistenceService;

	public HibernatePersistenceService getPersistenceService()
	{
		return persistenceService;
	}

	public void setPersistenceService(HibernatePersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}

	Wibble wibble;
	Bar bar;

	@Override
	protected void onSetUpInTransaction() throws Exception
	{
		wibble = new Wibble();

		bar = new Bar();

		bar = persistenceService.save(bar);
		wibble.setBar(bar);
		wibble = persistenceService.save(wibble);
		TestUtils.cleanDescriptorInternationalizationAspect();
	}

	public void testAssertNoOrphans() throws Exception
	{


		OrphanException orphanException = null;
		try
		{
			persistenceService.remove(bar);
		}
		catch (OrphanException oe)
		{
			orphanException = oe;
		}
		assertNotNull(orphanException);
		assertEquals("This Bar cannot be removed because there is a Wibble that refers to it.",
			orphanException.getMessage());
		//persistenceService.remove(gazonk);
		//persistenceService.remove(bar);
	}

	public void testAssertNoOrphansUsingProperty() throws Exception
	{
		Foo foo = new Foo();
		Baz baz = new Baz();
		foo.getBazzes().add(baz);
		OrphanException orphanException = null;
		try
		{
			persistenceService.remove(foo);
		}
		catch (OrphanException oe)
		{
			orphanException = oe;
		}
		assertNotNull(orphanException);
		assertEquals("This is a message", orphanException.getMessage());
	}
}
