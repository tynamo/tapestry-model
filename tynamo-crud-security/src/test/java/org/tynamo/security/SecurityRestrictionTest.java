package org.tynamo.security;

import junit.framework.TestCase;
import org.tynamo.descriptor.IPropertyDescriptor;
import org.tynamo.descriptor.TrailsPropertyDescriptor;
import org.tynamo.test.Foo;
import org.tynamo.test.IBar;

public class SecurityRestrictionTest extends TestCase
{
	protected SecurityAuthorities authorities;
	protected IPropertyDescriptor propertyDescriptor;

	public SecurityRestrictionTest()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public SecurityRestrictionTest(String arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	protected void setUp() throws Exception
	{
		propertyDescriptor = new TrailsPropertyDescriptor(Foo.class, "bar", IBar.class);
		authorities = new SecurityAuthorities();
	}

	/**
	 * Some test so All test cases can be executed.
	 */
	public void testFoo()
	{

	}

}
