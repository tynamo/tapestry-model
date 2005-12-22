package org.trails.descriptor.annotation;


import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Foo;

import junit.framework.TestCase;

public class AnnotationDecoratorTest extends TestCase
{
    public void testDecorate()
    {
        AnnotationDecorator decorator = new AnnotationDecorator();
        
        IClassDescriptor descriptor = new TrailsClassDescriptor(Annotated.class, "Annotated");
        IPropertyDescriptor fieldPropDescriptor = new TrailsPropertyDescriptor(Foo.class, "notBloppity", String.class);
        
        descriptor.getPropertyDescriptors().add(fieldPropDescriptor);
        IPropertyDescriptor hiddenDescriptor = new TrailsPropertyDescriptor(Foo.class, "hidden", String.class);
        hiddenDescriptor.setIndex(1);
        descriptor.getPropertyDescriptors().add(hiddenDescriptor); 
        IPropertyDescriptor validatedStringDescriptor = new TrailsPropertyDescriptor(Foo.class, "validatedString", String.class);
        validatedStringDescriptor.setIndex(3);
        
        IPropertyDescriptor booleanDescriptor = new TrailsPropertyDescriptor(Foo.class, "booleanProperty", boolean.class);
        descriptor.getPropertyDescriptors().add(validatedStringDescriptor);   
        descriptor.getPropertyDescriptors().add(new IdentifierDescriptor(Foo.class, "id", Integer.class));
        descriptor.getPropertyDescriptors().add(booleanDescriptor);
        
        descriptor = decorator.decorate(descriptor);
        assertEquals(Annotated.CLASS_LABEL, descriptor.getDisplayName());
        assertEquals("right index", "notBloppity", 
                ((IPropertyDescriptor)descriptor.getPropertyDescriptors().get(2)).getName());
        assertTrue(descriptor.getPropertyDescriptor("hidden").isHidden());
        assertEquals(Annotated.NOT_BLOPPITY_LABEL,
                descriptor.getPropertyDescriptor("notBloppity").getDisplayName());
        assertTrue("still an id descriptor", 
                descriptor.getPropertyDescriptor("id") instanceof IdentifierDescriptor);
        validatedStringDescriptor = descriptor.getPropertyDescriptor("validatedString");
        
        assertEquals(Annotated.BOOLEAN_LABEL, 
                descriptor.getPropertyDescriptor("booleanProperty").getDisplayName());
        
    }
}
