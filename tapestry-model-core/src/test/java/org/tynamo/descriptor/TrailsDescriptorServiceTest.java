package org.tynamo.descriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class TrailsDescriptorServiceTest extends MockObjectTestCase
{
	TrailsDescriptorService descriptorService = new TrailsDescriptorService();
	TrailsClassDescriptor descriptor;

	public void setUp() throws Exception
	{
		ArrayList<Class> types = new ArrayList<Class>();
		types.add(TestBean.class);
		types.add(ABean.class);
		types.add(A.class);
		types.add(B.class);
		types.add(C.class);
		types.add(D.class);
		types.add(E.class);

		descriptorService.setTypes(types);
		descriptorService.setDescriptorFactory(new ReflectionDescriptorFactory());
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

		descriptorService.getDescriptorFactory().setPropertyExcludes(Arrays.asList(new String[]{"bork", "class"}));
		descriptorService.init();
		descriptor = descriptorService.getClassDescriptor(TestBean.class);

		assertEquals("property excluded", 1,
			descriptor.getPropertyDescriptors().size());
	}

	public void testDecorating() throws Exception
	{
		TrailsDescriptorService descriptorService = new TrailsDescriptorService();
		descriptorService.setDescriptorFactory(new ReflectionDescriptorFactory());
		ArrayList types = new ArrayList();
		types.add(TestBean.class);
		descriptorService.setTypes(types);
		Mock decoratorMock = new Mock(DescriptorDecorator.class);
		DescriptorDecorator decorator = (DescriptorDecorator) decoratorMock.proxy();

		TrailsClassDescriptor decoratedDescriptor = new TrailsClassDescriptor(TestBean.class);
		decoratedDescriptor.setDisplayName("Decorated");
		decoratorMock.expects(atLeastOnce()).method("decorate").with(isA(TrailsClassDescriptor.class)).will(returnValue(decoratedDescriptor));
		descriptorService.getDecorators().add(decorator);
		descriptorService.init();
		assertEquals("was decorated", "Decorated",
			descriptorService.getClassDescriptor(TestBean.class).getDisplayName());
		decoratorMock.verify();
	}

	public void testGetAllDescriptors() throws Exception
	{
		List descriptors = descriptorService.getAllDescriptors();
		TrailsClassDescriptor aDescriptor = (TrailsClassDescriptor) descriptors.get(0);
		assertEquals("A is first", ABean.class, aDescriptor.getType());
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

	/**
	 * @(#) B.java
	 */

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

	/**
	 * @(#) C.java
	 */

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

	/**
	 * @(#) D.java
	 */

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

	/**
	 * @(#) E.java
	 */

	public class E
	{

	}

}
