package org.trails.descriptor.annotation;

import junit.framework.TestCase;

import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;

public class ClassDescriptorDecoratorTest extends TestCase
{

    public ClassDescriptorDecoratorTest()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public ClassDescriptorDecoratorTest(String arg0)
    {
        super(arg0);
        // TODO Auto-generated constructor stub
    }
    
    public void testDecorate() throws Exception
    {
        ClassDescriptorAnnotationHandler decorator = new ClassDescriptorAnnotationHandler();
        TrailsClassDescriptor descriptor = new TrailsClassDescriptor(Annotated.class);
        ClassDescriptor classDescriptorAnno = Annotated.class.getAnnotation(ClassDescriptor.class);
        IClassDescriptor decoratedDescriptor =  decorator.decorateFromAnnotation(classDescriptorAnno, descriptor);
        assertEquals("This is annotated", decoratedDescriptor.getDisplayName());
        assertTrue(decoratedDescriptor.isHidden());
    }

}
