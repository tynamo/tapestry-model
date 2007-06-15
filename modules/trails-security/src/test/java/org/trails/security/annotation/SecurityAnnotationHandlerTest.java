package org.trails.security.annotation;

import java.lang.reflect.Method;
import java.util.List;

import junit.framework.TestCase;
import org.trails.security.ClassSecurityRestriction;
import org.trails.security.PropertySecurityRestriction;
import org.trails.security.RestrictionType;
import org.trails.security.SecurityAnnotated;

public class SecurityAnnotationHandlerTest extends TestCase
{

	public SecurityAnnotationHandlerTest()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public SecurityAnnotationHandlerTest(String arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public void testBuildClassRestrictions() throws Exception
	{
		SecurityAnnotationHandler handler = new SecurityAnnotationHandler();
		List classRestrictions = handler.buildClassRestrictions(SecurityAnnotated.class);
		ClassSecurityRestriction classRestriction = (ClassSecurityRestriction) classRestrictions.get(0);
		assertEquals("admin", classRestriction.getRequiredRole()[0]);
		assertEquals(RestrictionType.UPDATE, classRestriction.getRestrictionType());
	}

	public void testBuildPropertyRestrictions() throws Exception
	{
		SecurityAnnotationHandler handler = new SecurityAnnotationHandler();
		Method getter = SecurityAnnotated.class.getMethod("getRequiresAdmin", new Class[]{});
		List restrictions = handler.buildPropertyRestrictions(getter, "requiresAdmin");
		PropertySecurityRestriction restriction = (PropertySecurityRestriction) restrictions.get(0);
		assertEquals("admin", restriction.getRequiredRole()[0]);
		assertEquals("requiresAdmin", restriction.getPropertyName());
		assertEquals(RestrictionType.VIEW, restriction.getRestrictionType());

	}
}
