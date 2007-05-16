package org.trails.security;


import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.test.Foo;

public class PropertyRestrictionTest extends SecurityRestrictionTest
{
	public PropertyRestrictionTest()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public PropertyRestrictionTest(String arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public void testRestrict() throws Exception
	{
		IClassDescriptor classDescriptor = new TrailsClassDescriptor(Foo.class, "Foo");
		classDescriptor.getPropertyDescriptors().add(propertyDescriptor);
		PropertySecurityRestriction restriction = new PropertySecurityRestriction();
		restriction.setRequiredRole("admin");
		restriction.setRestrictionType(RestrictionType.VIEW);
		restriction.setPropertyName("bar");

		restriction.restrict(autorities.adminAuthority, classDescriptor);
		assertFalse(propertyDescriptor.isHidden());
		restriction.restrict(autorities.noAdminAuthority, classDescriptor);
		assertTrue(propertyDescriptor.isHidden());

		restriction.setRestrictionType(RestrictionType.UPDATE);
		restriction.restrict(autorities.adminAuthority, classDescriptor);
		assertFalse(propertyDescriptor.isReadOnly());
		restriction.restrict(autorities.noAdminAuthority, classDescriptor);
		assertTrue(propertyDescriptor.isReadOnly());

	}

}
