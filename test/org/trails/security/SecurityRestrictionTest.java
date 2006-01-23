package org.trails.security;

import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Foo;
import org.trails.test.IBar;

import junit.framework.TestCase;

public class SecurityRestrictionTest extends TestCase
{
    protected SecurityAuthorities autorities;	
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

    public void setUp()
    {
        propertyDescriptor = new TrailsPropertyDescriptor(Foo.class, "bar", IBar.class);
        autorities = new SecurityAuthorities();
    }
    
    /**
     * Some test so All test cases can be executed.
     *
     */
    public void testFoo() 
    {
    	
    }

}
