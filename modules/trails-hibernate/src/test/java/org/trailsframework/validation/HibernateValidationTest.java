package org.trailsframework.validation;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.trails.persistence.HibernatePersistenceService;
import org.trails.testhibernate.Baz;


public class HibernateValidationTest extends AbstractTransactionalSpringContextTests
{

	HibernatePersistenceService persistenceService;

	@Override
	protected void onSetUpInTransaction() throws Exception
	{
		super.onSetUpInTransaction();
		persistenceService = (HibernatePersistenceService) applicationContext.getBean("persistenceService");
	}

	public void testValidation()
	{
		Baz baz = new Baz();
		baz.setDescription("this is longer than 10 characters");
		InvalidStateException ivex = null;
		try
		{
			baz = persistenceService.save(baz);
		}
		catch (InvalidStateException ex)
		{
			ivex = ex;
		}
		assertNotNull(ivex);
		InvalidValue invalidValue = ivex.getInvalidValues()[0];
		assertEquals("right message", "was too long.", invalidValue.getMessage());
	}

	public void _testValidator() throws Exception
	{
		Baz baz = new Baz();
		ClassValidator<Baz> validator = new ClassValidator<Baz>(Baz.class);
		InvalidValue[] invalidValues = validator.getInvalidValues(baz);
		assertEquals(1, invalidValues.length);
	}

	@Override
	protected String[] getConfigLocations()
	{
		return new String[]{"applicationContext-test.xml"};
	}

}
