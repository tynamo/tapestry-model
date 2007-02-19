/*
 * Copyright 2004 Chris Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.trails.hibernate;

import java.util.Date;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import ognl.Ognl;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.DescriptorDecorator;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.descriptor.TrailsPropertyDescriptor;
import org.trails.test.Bar;
import org.trails.test.Baz;
import org.trails.test.Descendant;
import org.trails.test.Embeddee;
import org.trails.test.Embeddor;
import org.trails.test.Foo;
import org.trails.test.IBar;


/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HibernateDescriptorDecoratorTest extends TestCase
{
    ApplicationContext appContext;
    DescriptorDecorator hibernateDescriptorDecorator;
    IClassDescriptor classDescriptor;
    
    public void setUp()
    {
        appContext = new ClassPathXmlApplicationContext(
                "applicationContext-test.xml");
        hibernateDescriptorDecorator = (DescriptorDecorator) appContext.getBean(
                "hibernateDescriptorDecorator");
        TrailsClassDescriptor fooDescriptor = new TrailsClassDescriptor(Foo.class);
        fooDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Foo.class, "bazzes", Set.class));
        fooDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Foo.class, "bings", Set.class));
        fooDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Foo.class, "id", Integer.class));
        fooDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Foo.class, "name", String.class));
        fooDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Foo.class, "hidden", String.class));
        fooDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Foo.class, "date", Date.class));
        fooDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Foo.class, "readOnly", String.class));
        fooDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Foo.class, "multiWordProperty", String.class));
        fooDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Foo.class, "primitive", boolean.class));
        fooDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Foo.class, "bar", IBar.class));
        fooDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Foo.class, "fromFormula", String.class));
       
        classDescriptor = hibernateDescriptorDecorator.decorate(fooDescriptor);
        
    }
    
    public void testNameDescriptor() throws Exception
    {
        IPropertyDescriptor nameDescriptor = (IPropertyDescriptor)classDescriptor.getPropertyDescriptor("name");
        
        assertEquals("is name", "name", nameDescriptor.getName());
       
    }
    
    public void testIdDescriptor() throws Exception
    {
        IdentifierDescriptor idDescriptor = (IdentifierDescriptor) classDescriptor.getIdentifierDescriptor();
        assertTrue("is id", idDescriptor.isIdentifier());
        assertFalse("not generated", idDescriptor.isGenerated());
    }

    public void testFormulaDescriptor() throws Exception
    {
        IPropertyDescriptor formulaDescriptor =  classDescriptor.getPropertyDescriptor("fromFormula");
        assertTrue(formulaDescriptor.isReadOnly());
    }
    
    public void testCollectionDescriptor() throws Exception
    {

        CollectionDescriptor bazzesDescriptor = (CollectionDescriptor) classDescriptor.getPropertyDescriptor("bazzes");
        assertTrue("bazzes is a collection", bazzesDescriptor.isCollection());
        assertEquals("right element type", Baz.class,
            bazzesDescriptor.getElementType());
        //TODO Fix when hibernate annotations add support for this..
        //assertTrue("bazzes are children", bazzesDescriptor.isChildRelationship());
        assertTrue(bazzesDescriptor.isOneToMany());
        assertEquals("bazzes are mapped by 'foo' property in Baz", "foo", bazzesDescriptor.getInverseProperty());
        assertTrue("Foo has a cyclic relationship", classDescriptor.getHasCyclicRelationships());
    }
    
    public void testGetClassDescriptors() throws Exception
    {

        assertFalse("not a child", classDescriptor.isChild());
        List propertyDescriptors = classDescriptor.getPropertyDescriptors();
        assertEquals("got 11", 11, propertyDescriptors.size());
        
//        TrailsPropertyDescriptor barDescriptor = (TrailsPropertyDescriptor) Ognl.getValue("#root.{? #this.name == 'bar'}[0]",
//            propertyDescriptors);
//        assertEquals("name", "bar", barDescriptor.getName());
//        assertTrue("is not an id", !barDescriptor.isIdentifier());
        
        IPropertyDescriptor hiddenDescriptor = (IPropertyDescriptor) 
        	Ognl.getValue("#root.{? #this.name == 'hidden'}[0]", propertyDescriptors);
        assertNotNull("didn't blow up", hiddenDescriptor);
        
		// This is not working yet for Hibernate3 Xdoclet
//        IPropertyDescriptor readOnlyDescriptor = (IPropertyDescriptor)
//        	Ognl.getValue("#root.{? #this.name == 'readOnly'}[0]", propertyDescriptors);
//        assertTrue("is read only", readOnlyDescriptor.isReadOnly());
        
        IPropertyDescriptor primitiveDescriptor = (IPropertyDescriptor)
        	Ognl.getValue("#root.{? #this.name == 'primitive'}[0]", propertyDescriptors);
        assertTrue("is boolean", primitiveDescriptor.isBoolean());

       
    }

//    public void testischild() throws exception
//    {
//        descriptorservice descriptorservice = (descriptorservice)appcontext.getbean("descriptorservice");
//        iclassdescriptor bazdescriptor = descriptorservice.getclassdescriptor(baz.class);
//        asserttrue("is a child", bazdescriptor.ischild());
//    }
    
    public void testIsObjectReference() throws Exception
    {
        IPropertyDescriptor propertyDescriptor = classDescriptor.getPropertyDescriptor(
                "bar");
        assertTrue(propertyDescriptor.isObjectReference());
        assertEquals("got right class", Bar.class,
            propertyDescriptor.getPropertyType());

        IPropertyDescriptor primitiveDescriptor = classDescriptor.getPropertyDescriptor(
                "primitive");
        assertFalse(primitiveDescriptor.isObjectReference());
    }

    public void testGetMappings() throws Exception
    {
        LocalSessionFactoryBean lsfb = (LocalSessionFactoryBean) appContext.getBean(
                "&sessionFactory");
        Configuration cfg = lsfb.getConfiguration();
        PersistentClass fooMapping = cfg.getClassMapping(Foo.class.getName());
        Property idProp = fooMapping.getIdentifierProperty();
        //System.out.println(idProp.getMetaAttributes());
    }
    
    public void testLengthLarge() throws Exception
    {
        IPropertyDescriptor multiWordDescriptor = classDescriptor.getPropertyDescriptor("multiWordProperty");
        assertTrue(multiWordDescriptor.isLarge());
        assertEquals("right length", 101, multiWordDescriptor.getLength());
        IPropertyDescriptor nameDescriptor = classDescriptor.getPropertyDescriptor("name");
        assertFalse("not large", nameDescriptor.isLarge());
    }
    
    public void testInheritance() throws Exception
    {
        TrailsClassDescriptor descendantDescriptor = new TrailsClassDescriptor(Descendant.class);
        descendantDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Foo.class, "bazzes", Set.class));
        descendantDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Foo.class, "extra", String.class));
        descendantDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Foo.class, "id", Integer.class));
        descendantDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Foo.class, "name", String.class));
        IClassDescriptor decorated = (IClassDescriptor)hibernateDescriptorDecorator.decorate(descendantDescriptor);
        assertEquals(4, decorated.getPropertyDescriptors().size());
    }
    
    public void testEmbedded() throws Exception
    {
    	TrailsClassDescriptor embeddorDescriptor = new TrailsClassDescriptor(Embeddor.class);
    	embeddorDescriptor.getPropertyDescriptors().add(new TrailsPropertyDescriptor(Embeddor.class, "embeddee", Embeddee.class));
    	IClassDescriptor decorated = (IClassDescriptor)hibernateDescriptorDecorator.decorate(embeddorDescriptor);
    	IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor)decorated.getPropertyDescriptors().get(0);
    	assertTrue(propertyDescriptor.isEmbedded());
    	EmbeddedDescriptor embeddedDescriptor = (EmbeddedDescriptor)propertyDescriptor;
    	assertEquals("embeddee", embeddedDescriptor.getName());
    	assertEquals("right bean type", Embeddor.class, embeddedDescriptor.getBeanType());
    	assertEquals("3 prop descriptors", 3, embeddedDescriptor.getPropertyDescriptors().size());
    }
}
