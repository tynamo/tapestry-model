/*
 * Created on 14/12/2005 by Eduardo Piva (eduardo@gwe.com.br)
 *
 */
package org.tynamo.security;

import java.util.Iterator;
import java.util.List;

import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContextImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tynamo.descriptor.DescriptorService;
import org.tynamo.descriptor.IClassDescriptor;
import org.tynamo.security.test.FooSecured;
import org.tynamo.test.Foo;

public class DescriptorSecurityTest extends SecurityRestrictionTest
{

	private ApplicationContext appContext;
	private DescriptorService service;

	@Override
	protected void setUp() throws java.lang.Exception
	{
		// appContext will initialize the aspect
		super.setUp();
		appContext = new ClassPathXmlApplicationContext(
			"applicationContext-test.xml");
		service = (DescriptorService) appContext.getBean("descriptorService");
	}

	public void testClassWithoutAnnotation()
	{
		IClassDescriptor classDescriptor = service.getClassDescriptor(Foo.class);
		checkConstraintsWithoutAnnotation(classDescriptor);
	}

	public void testClassWithAnnotation()
	{
		IClassDescriptor classDescriptor;

		SecurityContextImpl context = new SecurityContextImpl();
		context.setAuthentication(authorities.adminAuthentication);
		SecurityContextHolder.setContext(context);
		assertNotNull(context.getAuthentication());

		classDescriptor = service.getClassDescriptor(FooSecured.class);
		assertTrue(classDescriptor.isAllowRemove());
		assertTrue(classDescriptor.isAllowSave());
		assertTrue(classDescriptor.isHidden());

		context.setAuthentication(authorities.rootAuthentication);
		classDescriptor = service.getClassDescriptor(FooSecured.class);
		assertTrue(classDescriptor.isAllowRemove());
		assertTrue(!classDescriptor.isAllowSave());
		assertTrue(!classDescriptor.isHidden());

		context.setAuthentication(authorities.noAdminAuthentication);

		classDescriptor = service.getClassDescriptor(FooSecured.class);
		assertTrue(classDescriptor.isAllowRemove());
		assertTrue(!classDescriptor.isAllowSave());
		assertTrue(classDescriptor.isHidden());
	}

	public void testGetAllDescriptors()
	{
		IClassDescriptor classDescriptor;
		List descriptors;
		Iterator i;
		boolean passed = false;

		SecurityContextImpl context = new SecurityContextImpl();
		context.setAuthentication(authorities.adminAuthentication);
		SecurityContextHolder.setContext(context);
		assertNotNull(context.getAuthentication());

		descriptors = service.getAllDescriptors();
		i = descriptors.iterator();
		while (i.hasNext())
		{
			classDescriptor = (IClassDescriptor) i.next();
			if (classDescriptor.getType().equals(FooSecured.class))
			{
				passed = true;
				assertTrue(classDescriptor.isAllowRemove());
				assertTrue(classDescriptor.isAllowSave());
				assertTrue(classDescriptor.isHidden());
			} else
			{
				checkConstraintsWithoutAnnotation(classDescriptor);
			}
		}

		assertTrue(passed);
		passed = false;
		context.setAuthentication(authorities.rootAuthentication);
		descriptors = service.getAllDescriptors();
		i = descriptors.iterator();
		while (i.hasNext())
		{
			classDescriptor = (IClassDescriptor) i.next();
			if (classDescriptor.getType().equals(FooSecured.class))
			{
				passed = true;
				assertTrue(classDescriptor.isAllowRemove());
				assertTrue(!classDescriptor.isAllowSave());
				assertTrue(!classDescriptor.isHidden());
			} else
			{
				checkConstraintsWithoutAnnotation(classDescriptor);
			}
		}

		assertTrue(passed);
		passed = false;
		context.setAuthentication(authorities.noAdminAuthentication);
		descriptors = service.getAllDescriptors();
		i = descriptors.iterator();
		while (i.hasNext())
		{
			classDescriptor = (IClassDescriptor) i.next();
			if (classDescriptor.getType().equals(FooSecured.class))
			{
				passed = true;
				assertTrue(classDescriptor.isAllowRemove());
				assertTrue(!classDescriptor.isAllowSave());
				assertTrue(classDescriptor.isHidden());
			} else
			{
				checkConstraintsWithoutAnnotation(classDescriptor);
			}
		}
		assertTrue(passed);
	}

	private void checkConstraintsWithoutAnnotation(IClassDescriptor classDescriptor)
	{
		if (!classDescriptor.getType().getPackage().getName().equals("org.tynamo.security.domain"))
		{
			assertTrue(classDescriptor.isAllowRemove());
			assertTrue(classDescriptor.isAllowSave());
			assertTrue(!classDescriptor.isHidden());
		}
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
		SecurityContextHolder.clearContext();
	}
}
