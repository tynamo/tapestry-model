package org.trails.validation;

import junit.framework.TestCase;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.trails.persistence.PersistenceService;
import org.trails.test.Baz;


public class HibernateValidationTest extends AbstractTransactionalSpringContextTests
{

    public HibernateValidationTest()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    PersistenceService persistenceService;
    
    @Override
    protected void onSetUpInTransaction() throws Exception
    {
        super.onSetUpInTransaction();
        persistenceService = (PersistenceService) applicationContext.getBean("persistenceService"); 
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
        //ivex.printStackTrace();
        assertNotNull(ivex);
        InvalidValue invalidValue = ivex.getInvalidValues()[0];
        assertEquals("right message", "was too long.", invalidValue.getMessage());
    }
    
    public void testValidator() throws Exception
    {
        Baz baz = new Baz();
        ClassValidator<Baz> validator = new ClassValidator<Baz>(Baz.class);
        InvalidValue[] invalidValues = validator.getInvalidValues(baz);
        assertEquals(1, invalidValues.length);
    }

    @Override
    protected String[] getConfigLocations()
    {
        return new String[] {"applicationContext-test.xml"};
    }

}
