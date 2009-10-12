/*
 * Created on 20/12/2005 by Eduardo Piva <eduardo@gwe.com.br>
 *
 */
package org.tynamo.page;

import junit.framework.TestCase;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContextImpl;
import org.apache.tapestry.test.Creator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tynamo.descriptor.DescriptorService;
import org.tynamo.descriptor.IClassDescriptor;
import org.tynamo.security.SecurityAuthorities;
import org.tynamo.security.test.FooSecured;

import java.util.List;

/**
 * @author Eduardo Fernandes Piva (eduardo@gwe.com.br)
 */
public class HomePageTest extends TestCase
{

	private DescriptorService descriptorService;
	private SecurityAuthorities autorities;
	private HomePage home;
	private Creator creator = new Creator();

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		// appContext will initialize the aspect
		autorities = new SecurityAuthorities();
		ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext-test.xml");
		descriptorService = (DescriptorService) appContext.getBean("descriptorService");
		home = buildHomePage();
	}

	public void testGetAllDescriptions()
	{
		List<IClassDescriptor> homeDescriptors = home.getAllDescriptors();
		List<IClassDescriptor> descriptors = descriptorService.getAllDescriptors();
		assertNotNull(homeDescriptors);
		assertNotNull(descriptors);

		for (IClassDescriptor descriptor : descriptors)
		{
			if (!descriptor.isHidden())
			{
				assertTrue(descriptorContains(homeDescriptors, descriptor));
			}
		}
	}

	public void testGetAllDescriptionsWithoutRole()
	{
		SecurityContextImpl context = new SecurityContextImpl();
		context.setAuthentication(autorities.noAdminAuthentication);
		SecurityContextHolder.setContext(context);
		List<IClassDescriptor> homeDescriptors = home.getAllDescriptors();
		List<IClassDescriptor> descriptors = descriptorService.getAllDescriptors();
		boolean hasSecurityAnnotated = false; /* we have to ensure we are testing some class that has security on it */

		for (IClassDescriptor descriptor : descriptors)
		{
			if (descriptor.getType().equals(FooSecured.class))
			{
				assertTrue(!descriptorContains(homeDescriptors, descriptor));
				assertTrue(descriptor.isHidden());
				hasSecurityAnnotated = true;
			} else
			{
				if (!descriptor.isHidden())
				{
					assertTrue(descriptorContains(homeDescriptors, descriptor));
				}
			}
		}

		assertTrue(hasSecurityAnnotated);
	}

	public void testGetAllDescriptionsWithRole()
	{
		SecurityContextImpl context = new SecurityContextImpl();
		context.setAuthentication(autorities.rootAuthentication);
		SecurityContextHolder.setContext(context);
		List<IClassDescriptor> homeDescriptors = home.getAllDescriptors();
		List<IClassDescriptor> descriptors = descriptorService.getAllDescriptors();
		boolean hasSecurityAnnotated = false; /* we have to ensure we are testing some class that has security on it */

		/* admin can view everything. It is the same test as testGetAllDescriptions */
		for (IClassDescriptor descriptor : descriptors)
		{
			if (descriptor.getType().equals(FooSecured.class))
			{
				hasSecurityAnnotated = true;
				assertFalse(descriptor.isHidden());
			}
			if (!descriptor.isHidden())
			{
				assertTrue(descriptorContains(homeDescriptors, descriptor));
			}
		}

		assertTrue(hasSecurityAnnotated);
	}

	private HomePage buildHomePage()
	{
		return (HomePage) creator.newInstance(HomePage.class,
				new Object[]{
						"descriptorService", descriptorService,
				});
	}

	private boolean descriptorContains(List<IClassDescriptor> homeDescriptors, IClassDescriptor tmp)
	{
		for (IClassDescriptor descriptor : homeDescriptors)
		{
			if (descriptor.getType().equals(tmp.getType()))
			{
				return true;
			}
		}
		return false;
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
		// Not to interfere with other tests
		SecurityContextHolder.clearContext();
	}
}