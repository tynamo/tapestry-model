package org.trails.descriptor.annotation;

import junit.framework.TestCase;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Foo;

import java.util.Date;

public class PropertyDescriptorDecoratorTest extends TestCase {
    public void testPropertyDescriptorDecorator() throws Exception {
        PropertyDescriptorAnnotationHandler decorator = new PropertyDescriptorAnnotationHandler();
        TrailsClassDescriptor descriptor = new TrailsClassDescriptor(Annotated.class);
        IPropertyDescriptor fieldPropDescriptor = new TrailsPropertyDescriptor(Foo.class, "notBloppity", String.class);
        IPropertyDescriptor hiddenDescriptor = new TrailsPropertyDescriptor(Foo.class, "hidden", String.class);
        IPropertyDescriptor dateDescriptor = new TrailsPropertyDescriptor(Foo.class, "date", Date.class);
        descriptor.getPropertyDescriptors().add(fieldPropDescriptor);
        descriptor.getPropertyDescriptors().add(hiddenDescriptor);

        PropertyDescriptor fieldAppearance = Annotated.class.getDeclaredField("notBloppity").getAnnotation(PropertyDescriptor.class);

        fieldPropDescriptor = decorator.decorateFromAnnotation(fieldAppearance, fieldPropDescriptor);
        assertEquals("Bloppity", fieldPropDescriptor.getDisplayName());
        assertEquals("right index", 2, (fieldPropDescriptor).getIndex());

        PropertyDescriptor propDescriptorAnno = Annotated.class.getMethod("getHidden").getAnnotation(PropertyDescriptor.class);
        IPropertyDescriptor hiddenPropDescriptor = decorator.decorateFromAnnotation(propDescriptorAnno, hiddenDescriptor);
        assertTrue(hiddenPropDescriptor.isHidden());
        assertTrue(hiddenPropDescriptor.getIndex() != 2);
        assertEquals("Hidden", hiddenPropDescriptor.getDisplayName());

        PropertyDescriptor dateDescriptorAnno = Annotated.class.getMethod("getDate").getAnnotation(PropertyDescriptor.class);
        IPropertyDescriptor datePropDescriptor = decorator.decorateFromAnnotation(dateDescriptorAnno, dateDescriptor);
        assertEquals("MM/dd/yyyy", datePropDescriptor.getFormat());
    }
}
