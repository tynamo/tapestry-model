package org.tynamo.validation;

import java.util.List;

import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.tynamo.persistence.HibernatePersistenceService;
import org.tynamo.testhibernate.Baz;
import org.tynamo.testhibernate.Bing;

public class ValidateUniquenessTest extends AbstractTransactionalSpringContextTests
{

	public ValidateUniquenessTest()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String[] getConfigLocations()
	{
		return new String[]{"applicationContext-test.xml"};
	}

	public void onSetUpInTransaction() throws Exception
	{
		persistenceService = (HibernatePersistenceService) applicationContext.getBean(
			"persistenceService");
	}

	HibernatePersistenceService persistenceService;

	public void testUniquenessQuery() {
		Baz baz = new Baz();
		baz.setDescription("one");
		persistenceService.save(baz);
		Baz baz2 = new Baz();
		baz2.setDescription("second");
		persistenceService.save(baz2);

		Object[] values = {baz.getDescription(), baz2.getId()};
		// Should refect the query used in ValidateUniqueAspect.aj
		List result = persistenceService.find("select count(*) from " 
        + baz.getClass().getName() + " where description " 
        + " = ? and id " + " != ?", values);
		assertTrue((Long)result.get(0) == 1);

		Object[] values2 = {baz.getDescription(), baz.getId()};
		result = persistenceService.find("select count(*) from " 
        + baz.getClass().getName() + " where description " 
        + " = ? and id " + " != ?", values2);
		assertTrue((Long)result.get(0) == 0);

	}
	
	public void testUniqueness() throws Exception
	{

		Baz baz = new Baz();
		baz.setDescription("same");
		persistenceService.save(baz);
		Baz baz2 = new Baz();
		baz2.setDescription("same");
		UniquenessException caught = null;
		try
		{
			persistenceService.save(baz2);
		}
		catch (UniquenessException pe)
		{
			caught = pe;
			//pe.printStackTrace();
		}
		assertNotNull(caught);
		assertEquals("right message", "Description must be unique.",
			caught.getMessage());
	}	

	public void testUniquenessWithNonString() throws Exception
	{
		Bing bing = new Bing();
		bing.setNumber(5);
		// should not blow up
		bing = persistenceService.save(bing);
		// should also still work
		bing = persistenceService.save(bing);
	}
}
