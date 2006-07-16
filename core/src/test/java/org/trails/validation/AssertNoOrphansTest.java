package org.trails.validation;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
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
		setAutowireMode(AUTOWIRE_BY_NAME);
	}

	@Override
	protected String[] getConfigLocations()
	{
		// TODO Auto-generated method stub
		return new String[] {"applicationContext-test.xml"};
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

	public void testGetIntancesWithManyToOne() throws Exception
    {
       
		Wibble gazonk = new Wibble();
		gazonk.setId(new Integer(9874));
		Bar bar = new Bar();
		
		bar = persistenceService.save(bar);
		gazonk.setBar(bar);
		gazonk = persistenceService.save(gazonk);
		DetachedCriteria criteria = DetachedCriteria.forClass(Wibble.class);
		criteria.add(Restrictions.eq("bar", bar));
		List list = persistenceService.getInstances(criteria);
		assertEquals(1, list.size());
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
