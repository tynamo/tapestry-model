package org.trails.descriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.trails.persistence.PersistenceService;
import org.trails.test.Bing;
import org.trails.test.Foo;

import junit.framework.TestCase;

public class TrailsDescriptorServiceTest extends MockObjectTestCase
{
    TrailsDescriptorService descriptorService = new TrailsDescriptorService();
    IClassDescriptor descriptor;
    
    public void setUp() throws Exception
    {
        ArrayList types = new ArrayList();
        types.add(TestBean.class);
        types.add(ABean.class);
        descriptorService.setTypes(types);
        descriptorService.init();
        descriptor = descriptorService.getClassDescriptor(TestBean.class);
    }
    
    public void testGetDescriptor()
    {
        assertNotNull("got descripor", descriptor);
        assertEquals("3 prop descriptors", 3, descriptor.getPropertyDescriptors().size());
    }
    
    public void testExcluding() throws Exception
    {
        
        descriptorService.setPropertyExcludes(Arrays.asList(new String[] {"bork", "class"}));
        descriptorService.init();
        descriptor = descriptorService.getClassDescriptor(TestBean.class);

        assertEquals("property excluded", 1,
            descriptor.getPropertyDescriptors().size());
    }
    
    public void testDecorating() throws Exception
    {
        TrailsDescriptorService descriptorService = new TrailsDescriptorService();
        ArrayList types = new ArrayList();
        types.add(TestBean.class);
        descriptorService.setTypes(types);
        Mock decoratorMock = new Mock(DescriptorDecorator.class);
        DescriptorDecorator decorator = (DescriptorDecorator)decoratorMock.proxy();

        TrailsClassDescriptor decoratedDescriptor = new TrailsClassDescriptor(TestBean.class);
        decoratedDescriptor.setDisplayName("Decorated");
        decoratorMock.expects(atLeastOnce()).method("decorate").with(isA(IClassDescriptor.class)).will(returnValue(decoratedDescriptor));
        descriptorService.getDecorators().add(decorator);
        descriptorService.init();
        assertEquals("was decorated", "Decorated", 
                descriptorService.getClassDescriptor(TestBean.class).getDisplayName());
        decoratorMock.verify();
    }
    
    public void testGetAllDescriptors() throws Exception
    {
        List descriptors = descriptorService.getAllDescriptors();
        IClassDescriptor aDescriptor = (IClassDescriptor)descriptors.get(0);
        assertEquals("A is first", ABean.class, aDescriptor.getType());
    }
    
    public void testIgnoreCGLIBEnhancements()
    {
        ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext-test.xml");
        PersistenceService psvc = (PersistenceService)appContext.getBean("persistenceService");
        DescriptorService descSvc = (DescriptorService)appContext.getBean("descriptorService");
        Foo foo = new Foo();
        foo.setId(new Integer(1));
        foo.setName("boo");        
        psvc.save(foo);
        foo = (Foo)psvc.getInstance(Foo.class, new Integer(1));
        assertNotNull(descSvc.getClassDescriptor(foo.getClass()));
    }
    
//    public void testMethodDescriptors() throws Exception
//    {
//        factory.setMethodExcludes(Arrays.asList(new String[] {"equals", "toString", "hashCode"}));
//        factory.init();
//        descriptor = factory.getClassDescriptor(TestBean.class);
//        assertEquals("1 method", 1, descriptor.getMethodDescriptors().size());
//        IMethodDescriptor methodDescriptor = (IMethodDescriptor)descriptor.getMethodDescriptors().get(0);
//        assertEquals("right method", "doSomething", methodDescriptor.getName());
//    }
    
    public class ABean
    {
        private String name;

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }
    }
    
    public class TestBean
    {
        private String bar;
        
        private String bork;

        public String getBar()
        {
            return bar;
        }
        

        public void setBar(String bar)
        {
            this.bar = bar;
        }
        

        public String getBork()
        {
            return bork;
        }
        

        public void setBork(String bork)
        {
            this.bork = bork;
        }
        
        public void doSomething() { System.out.println("foo"); }
    }
}
