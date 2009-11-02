package org.tynamo.security;

import java.util.List;

import junit.framework.TestCase;
import org.tynamo.descriptor.IClassDescriptor;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.descriptor.TrailsPropertyDescriptor;

public class SecurityServiceTest extends TestCase
{

	public SecurityServiceTest()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public SecurityServiceTest(String arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public void testBuildRestrictions() throws Exception
	{
		TrailsSecurityService securityService = new TrailsSecurityService();
		IClassDescriptor classDescriptor = new TrailsClassDescriptor(SecurityAnnotated.class);
		classDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(SecurityAnnotated.class, "requiresAdmin", String.class));
		classDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(SecurityAnnotated.class, "wideOpen", String.class));
		List restrictions = securityService.buildRestrictions(classDescriptor);
		assertEquals(2, restrictions.size());

	}

	public void testRestrict() throws Exception
	{
		//TrailsSecurityService securityService = new TrailsSecurityService();

	}
}
