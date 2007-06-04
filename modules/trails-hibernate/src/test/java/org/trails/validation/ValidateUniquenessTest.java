package org.trails.validation;

import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.trails.persistence.HibernatePersistenceService;
import org.trails.testhibernate.Baz;
import org.trails.testhibernate.Bing;

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
