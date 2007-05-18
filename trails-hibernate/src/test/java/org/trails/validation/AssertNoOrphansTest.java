package org.trails.validation;

import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.trails.persistence.PersistenceService;
import org.trails.test.Bar;
import org.trails.test.Baz;
import org.trails.test.Foo;
import org.trails.test.Wibble;

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

	private PersistenceService persistenceService;

	public PersistenceService getPersistenceService()
	{
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService)
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
		Assert.assertNotNull(orphanException);
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
		Assert.assertNotNull(orphanException);
		assertEquals("This is a message", orphanException.getMessage());
	}
}
