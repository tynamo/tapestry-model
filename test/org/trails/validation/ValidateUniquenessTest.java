package org.trails.validation;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

import org.trails.descriptor.annotation.Annotated;

import org.trails.persistence.PersistenceException;
import org.trails.persistence.PersistenceService;
import org.trails.test.Baz;
import org.trails.test.Bing;
import org.trails.test.Foo;

import junit.framework.TestCase;

public class ValidateUniquenessTest extends TestCase
{
    
    public ValidateUniquenessTest()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public ValidateUniquenessTest(String arg0)
    {
 
        super(arg0);
    }
    
    ApplicationContext appContext;
    
    public void setUp() throws Exception
    {
        appContext = new ClassPathXmlApplicationContext(
        "applicationContext-test.xml");
        persistenceService = (PersistenceService) appContext.getBean("persistenceService");        
    }
    
    PersistenceService persistenceService;
    
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
            pe.printStackTrace();
        }
        assertNotNull(caught);
        assertEquals("right message", "Description must be unique.",
                caught.getMessage());
    }

    public void testUniquenessWithNonString() throws Exception
    {
        SessionFactory sessionFactory = (SessionFactory)appContext.getBean("sessionFactory");
        
        Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        Bing bing = new Bing();
        bing.setNumber(5);
        // should not blow up
        bing = persistenceService.save(bing);
        // should also still work
        bing = persistenceService.save(bing);
        SessionFactoryUtils.closeSessionIfNecessary(session, sessionFactory);
    }
}
