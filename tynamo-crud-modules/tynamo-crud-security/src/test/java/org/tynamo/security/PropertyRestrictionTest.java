package org.tynamo.security;


import org.tynamo.descriptor.IClassDescriptor;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.test.Foo;

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
		restriction.setRequiredRole(new String[]{"admin"});
		restriction.setRestrictionType(RestrictionType.VIEW);
		restriction.setPropertyName("bar");

		restriction.restrict(authorities.adminAuthority, classDescriptor);
		assertFalse(propertyDescriptor.isHidden());
		restriction.restrict(authorities.noAdminAuthority, classDescriptor);
		assertTrue(propertyDescriptor.isHidden());

		restriction.setRestrictionType(RestrictionType.UPDATE);
		restriction.restrict(authorities.adminAuthority, classDescriptor);
		assertFalse(propertyDescriptor.isReadOnly());
		restriction.restrict(authorities.noAdminAuthority, classDescriptor);
		assertTrue(propertyDescriptor.isReadOnly());

	}

}
