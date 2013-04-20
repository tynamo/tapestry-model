package org.tynamo.descriptor;

import org.apache.tapestry5.ioc.test.TestBase;
import org.easymock.EasyMock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.tynamo.descriptor.decorators.DescriptorDecorator;
import org.tynamo.descriptor.factories.*;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.DescriptorServiceImpl;

import java.util.*;

public class DescriptorServiceTest extends TestBase
{

	PropertyDescriptorFactory propertyDescriptorFactory;
	MethodDescriptorFactory methodDescriptorFactory;
	ArrayList<Class> types;
	List<DescriptorDecorator> decorators;

	@BeforeClass
	public void setUp() throws Exception
	{
		types = new ArrayList<Class>();
		types.add(TestBean.class);
		types.add(ABean.class);
		types.add(A.class);
		types.add(B.class);
		types.add(C.class);
		types.add(D.class);
		types.add(E.class);

		propertyDescriptorFactory = new PropertyDescriptorFactoryImpl(Collections.<String>emptyList());
		methodDescriptorFactory = new MethodDescriptorFactoryImpl(Collections.<String>emptyList());
		decorators = Collections.emptyList();
	}

	@Test
	public void testGetDescriptor()
	{
		DescriptorService descriptorService = new DescriptorServiceImpl(types,
				new ReflectionDescriptorFactory(decorators, methodDescriptorFactory, propertyDescriptorFactory));

		TynamoClassDescriptor descriptor = descriptorService.getClassDescriptor(TestBean.class);
		assertNotNull(descriptor, "got descripor");
		assertEquals(descriptor.getPropertyDescriptors().size(), 3, "3 prop descriptors");
	}

	@Test
	public void testExcluding() throws Exception
	{
		PropertyDescriptorFactory propertyDescriptorFactory = new PropertyDescriptorFactoryImpl(Arrays.asList("bork", "class"));
		DescriptorService descriptorService = new DescriptorServiceImpl(types,
				new ReflectionDescriptorFactory(decorators, methodDescriptorFactory, propertyDescriptorFactory));

		TynamoClassDescriptor descriptor = descriptorService.getClassDescriptor(TestBean.class);
		assertEquals(descriptor.getPropertyDescriptors().size(), 1, "property excluded");
	}

	@Test
	public void testDecorating() throws Exception
	{
		ArrayList<Class> types = new ArrayList<Class>();
		types.add(TestBean.class);

		TynamoClassDescriptor decoratedDescriptor = new TynamoClassDescriptorImpl(TestBean.class);
		decoratedDescriptor.setHasCyclicRelationships(true);
		decoratedDescriptor.setNonVisual(true);

		DescriptorDecorator decorator = newMock(DescriptorDecorator.class);
		expect(decorator.decorate(EasyMock.<TynamoClassDescriptor>anyObject())).andReturn(decoratedDescriptor).atLeastOnce();
		replay();

		DescriptorService descriptorService = new DescriptorServiceImpl(types,
				new ReflectionDescriptorFactory(Arrays.asList(decorator), methodDescriptorFactory, propertyDescriptorFactory));

		TynamoClassDescriptor classDescriptor = descriptorService.getClassDescriptor(TestBean.class);

		assertNotNull(classDescriptor);
		assertTrue(classDescriptor.isNonVisual(), "was decorated");
		assertTrue(classDescriptor.getHasCyclicRelationships(), "was decorated");

		verify();
	}

	@Test
	public void testGetAllDescriptors() throws Exception
	{
		DescriptorService descriptorService = new DescriptorServiceImpl(types,
				new ReflectionDescriptorFactory(decorators, methodDescriptorFactory, propertyDescriptorFactory));

		assertEquals(descriptorService.getAllDescriptors().size(), 7, "There is 7 descriptors");
	}

/*
	@Test
	public void testMethodDescriptors() throws Exception
	{
		factory.setMethodExcludes(Arrays.asList(new String[]{"equals", "toString", "hashCode"}));
		factory.init();
		descriptor = factory.getClassDescriptor(TestBean.class);
		assertEquals("1 method", 1, descriptor.getMethodDescriptors().size());
		IMethodDescriptor methodDescriptor = (IMethodDescriptor) descriptor.getMethodDescriptors().get(0);
		assertEquals("right method", "doSomething", methodDescriptor.getName());
	}
*/

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

		public void doSomething()
		{
			//System.out.println("foo");
		}
	}

	public class A
	{
		private B b;
		private C c;


		public B getB()
		{
			return b;
		}

		public void setB(B b)
		{
			this.b = b;
		}

		public C getC()
		{
			return c;
		}

		public void setC(C c)
		{
			this.c = c;
		}
	}

	public class B
	{
		private C c;
		private E e;


		public C getC()
		{
			return c;
		}

		public void setC(C c)
		{
			this.c = c;
		}

		public E getE()
		{
			return e;
		}

		public void setE(E e)
		{
			this.e = e;
		}
	}

	public class C
	{
		private D d;


		public D getD()
		{
			return d;
		}

		public void setD(D d)
		{
			this.d = d;
		}
	}

	public class D
	{
		private E e;


		public E getE()
		{
			return e;
		}

		public void setE(E e)
		{
			this.e = e;
		}
	}

	public class E
	{

	}

}
