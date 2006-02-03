package org.trails.validation;

import junit.framework.TestCase;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.trails.persistence.PersistenceService;
import org.trails.test.Baz;


public class HibernateValidationTest extends TestCase
{

    public HibernateValidationTest()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    PersistenceService persistenceService;
    
    public void setUp() throws Exception
    {
        ApplicationContext appContext = new ClassPathXmlApplicationContext(
        "applicationContext-test.xml");
        persistenceService = (PersistenceService) appContext.getBean("persistenceService");        
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
        try
        {
            baz = persistenceService.save(baz);
        }
        catch (InvalidStateException ex)
        {
            //ex.printStackTrace();
        }
        ClassValidator<Baz> validator = new ClassValidator<Baz>(Baz.class);
        InvalidValue[] invalidValues = validator.getInvalidValues(baz);
        assertEquals(1, invalidValues.length);
    }

}
