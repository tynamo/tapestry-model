package org.trails.security;

import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.test.Foo;

public class ClassSecurityRestrictionTest extends SecurityRestrictionTest
{

    public ClassSecurityRestrictionTest()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public ClassSecurityRestrictionTest(String arg0)
    {
        super(arg0);
        // TODO Auto-generated constructor stub
    }
    
    public void testRestrict() throws Exception
    {
        ClassSecurityRestriction classRestriction = new ClassSecurityRestriction();
        IClassDescriptor classDescriptor = new TrailsClassDescriptor(Foo.class);
        classRestriction.setRequiredRole("admin");
        classRestriction.setRestrictionType(RestrictionType.VIEW);
        classRestriction.restrict(autorities.adminAuthority, classDescriptor);
        assertFalse(classDescriptor.isHidden());
        classDescriptor = new TrailsClassDescriptor(Foo.class);
        classRestriction.restrict(autorities.noAdminAuthority, classDescriptor);
        assertTrue(classDescriptor.isHidden());
        
        classRestriction.setRestrictionType(RestrictionType.UPDATE);
        classDescriptor = new TrailsClassDescriptor(Foo.class);
        classRestriction.restrict(autorities.noAdminAuthority, classDescriptor);
        assertFalse(classDescriptor.isAllowSave());     
        
        classRestriction.setRestrictionType(RestrictionType.REMOVE);
        classDescriptor = new TrailsClassDescriptor(Foo.class);
        classRestriction.restrict(autorities.noAdminAuthority, classDescriptor);
        assertFalse(classDescriptor.isAllowRemove());     
    }
}
