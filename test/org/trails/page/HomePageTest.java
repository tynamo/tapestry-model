/*
 * Created on 20/12/2005 by Eduardo Piva <eduardo@gwe.com.br>
 *
 */
package org.trails.page;

import java.util.Iterator;
import java.util.List;

import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContextImpl;
import org.apache.tapestry.test.Creator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.trails.component.ComponentTest;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.persistence.PersistenceService;
import org.trails.security.SecurityAuthorities;
import org.trails.security.test.FooSecured;
import org.trails.servlet.TrailsApplicationServlet;

/**
 * 
 * @author Eduardo Fernandes Piva (eduardo@gwe.com.br)
 *
 */
public class HomePageTest extends ComponentTest {

	private ApplicationContext appContext;
	private DescriptorService service;
	private PersistenceService persistenceService;
	private SecurityAuthorities autorities;
	private HomePage home;
    private Creator creator = new Creator();

	@Override
	public void setUp() {
		// appContext will initialize the aspect
		autorities = new SecurityAuthorities();
	    appContext = new ClassPathXmlApplicationContext(
        "applicationContext-test.xml");
		TrailsApplicationServlet.setCurrentLocale(null);
		service = (DescriptorService) appContext.getBean("descriptorService");
		persistenceService = (PersistenceService)appContext.getBean("persistenceService");
		home = buildHomePage();
	}

	public void testGetAllDescriptions() {
		List homeDescriptors = home.getAllDescriptors();
		List descriptors = service.getAllDescriptors();
		assertNotNull(homeDescriptors);
		assertNotNull(descriptors);

		Iterator i = descriptors.iterator();
		while (i.hasNext()) {
			IClassDescriptor tmp = (IClassDescriptor) i.next();
			assertTrue(descriptorContains(homeDescriptors,tmp));			
		}
	}

	public void testGetAllDescriptionsWithoutRole() {
		SecurityContextImpl context = new SecurityContextImpl();
		context.setAuthentication(autorities.noAdminAuthentication);
		SecurityContextHolder.setContext(context);
		List homeDescriptors = home.getAllDescriptors();
		List descriptors = service.getAllDescriptors();
		boolean hasSecurityAnnotated = false; /* we have to ensure we are testing some class that has security on it */
		
		Iterator i = descriptors.iterator();
		while (i.hasNext()) {
			IClassDescriptor tmp = (IClassDescriptor) i.next();
			if (tmp.getType().equals(FooSecured.class)) {
				assertTrue(!descriptorContains(homeDescriptors,tmp));
				assertTrue(tmp.isHidden());
				hasSecurityAnnotated = true;
			} else {
				if (!tmp.isHidden()) assertTrue(descriptorContains(homeDescriptors,tmp));
			}
		}
		
		assertTrue(hasSecurityAnnotated);
	}

	public void testGetAllDescriptionsWithRole() {
		SecurityContextImpl context = new SecurityContextImpl();
		context.setAuthentication(autorities.rootAuthentication);
		SecurityContextHolder.setContext(context);
		List homeDescriptors = home.getAllDescriptors();
		List descriptors = service.getAllDescriptors();
		boolean hasSecurityAnnotated = false; /* we have to ensure we are testing some class that has security on it */
		
		/* admin can view everything. It is the same test as testGetAllDescriptions */
		Iterator i = descriptors.iterator();
		while (i.hasNext()) {
			IClassDescriptor tmp = (IClassDescriptor) i.next();
			if (tmp.getType().equals(FooSecured.class)) {
				hasSecurityAnnotated = true;
				assertFalse(tmp.isHidden());
			}
			if (!tmp.isHidden()) assertTrue(descriptorContains(homeDescriptors,tmp));
		}
		
		assertTrue(hasSecurityAnnotated);
	}
	
    private HomePage buildHomePage() {
    	HomePage page = (HomePage) creator.newInstance(HomePage.class, 
                new Object[] {
                "descriptorService", service,
                "persistenceService", persistenceService
            });
    	return page;
    }
    
	private boolean descriptorContains(List homeDescriptors, IClassDescriptor tmp) {
		Iterator i = homeDescriptors.iterator();
		while (i.hasNext()) {
			IClassDescriptor descriptor = (IClassDescriptor) i.next();
			if (descriptor.getType().equals(tmp.getType())) {
				return true;
			}			
		}
		return false;
	}

}
