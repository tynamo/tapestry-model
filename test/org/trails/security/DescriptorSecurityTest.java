/*
 * Created on 14/12/2005 by Eduardo Piva (eduardo@gwe.com.br)
 *
 */
package org.trails.security;

import java.util.Iterator;
import java.util.List;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContextImpl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.security.test.FooSecured;
import org.trails.servlet.TrailsApplicationServlet;
import org.trails.test.Foo;

import junit.framework.TestCase;

public class DescriptorSecurityTest extends SecurityRestrictionTest {

	private ApplicationContext appContext;
	private DescriptorService service;

	@Override
	public void setUp() {
		// appContext will initialize the aspect
		super.setUp();
        appContext = new ClassPathXmlApplicationContext(
        "applicationContext-test.xml");
		TrailsApplicationServlet.setCurrentLocale(null);
		service = (DescriptorService) appContext.getBean("descriptorService");
	}

	public void testClassWithoutAnotation() {
		IClassDescriptor classDescriptor = service.getClassDescriptor(Foo.class);
		checkConstraintsWithoutAnotation(classDescriptor);
	}

	public void testClassWithAnotation() {
		IClassDescriptor classDescriptor;

		SecurityContextImpl context = new SecurityContextImpl();
		context.setAuthentication(autorities.adminAuthentication);
		SecurityContextHolder.setContext(context);		
		assertNotNull(context.getAuthentication());

		classDescriptor = service.getClassDescriptor(FooSecured.class);
		assertTrue(classDescriptor.isAllowRemove());
		assertTrue(classDescriptor.isAllowSave());
		assertTrue(classDescriptor.isHidden());

		context.setAuthentication(autorities.rootAuthentication);
		classDescriptor = service.getClassDescriptor(FooSecured.class);
		assertTrue(classDescriptor.isAllowRemove());
		assertTrue(!classDescriptor.isAllowSave());
		assertTrue(!classDescriptor.isHidden());

		context.setAuthentication(autorities.noAdminAuthentication);

		classDescriptor = service.getClassDescriptor(FooSecured.class);
		assertTrue(classDescriptor.isAllowRemove());
		assertTrue(!classDescriptor.isAllowSave());
		assertTrue(classDescriptor.isHidden());
	}

	public void testGetAllDescriptors() {
		IClassDescriptor classDescriptor;
		List descriptors;
		Iterator i;
		boolean passed = false;

		SecurityContextImpl context = new SecurityContextImpl();
		context.setAuthentication(autorities.adminAuthentication);
		SecurityContextHolder.setContext(context);		
		assertNotNull(context.getAuthentication());

		descriptors = service.getAllDescriptors();
		i = descriptors.iterator();
		while (i.hasNext()) {
			classDescriptor = (IClassDescriptor) i.next();
			if (classDescriptor.getType().equals(FooSecured.class)) {
				passed = true;
				assertTrue(classDescriptor.isAllowRemove());
				assertTrue(classDescriptor.isAllowSave());
				assertTrue(classDescriptor.isHidden());				
			} else {
				checkConstraintsWithoutAnotation(classDescriptor);
			}
		}

		assertTrue(passed);
		passed = false;
		context.setAuthentication(autorities.rootAuthentication);
		descriptors = service.getAllDescriptors();
		i = descriptors.iterator();
		while (i.hasNext()) {
			classDescriptor = (IClassDescriptor) i.next();
			if (classDescriptor.getType().equals(FooSecured.class)) {
				passed = true;
				assertTrue(classDescriptor.isAllowRemove());
				assertTrue(!classDescriptor.isAllowSave());
				assertTrue(!classDescriptor.isHidden());
			} else {
				checkConstraintsWithoutAnotation(classDescriptor);
			}
		}

		assertTrue(passed);
		passed = false;
		context.setAuthentication(autorities.noAdminAuthentication);
		descriptors = service.getAllDescriptors();
		i = descriptors.iterator();
		while (i.hasNext()) {
			classDescriptor = (IClassDescriptor) i.next();
			if (classDescriptor.getType().equals(FooSecured.class)) {
				passed = true;
				assertTrue(classDescriptor.isAllowRemove());
				assertTrue(!classDescriptor.isAllowSave());
				assertTrue(classDescriptor.isHidden());
			} else {
				checkConstraintsWithoutAnotation(classDescriptor);
			}
		}		
		assertTrue(passed);
	}

	private void checkConstraintsWithoutAnotation(IClassDescriptor classDescriptor) {
		assertTrue(classDescriptor.isAllowRemove());
		assertTrue(classDescriptor.isAllowSave());
		assertTrue(!classDescriptor.isHidden());
	}
}
