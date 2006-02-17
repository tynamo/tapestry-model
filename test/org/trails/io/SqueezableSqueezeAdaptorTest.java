package org.trails.io;

import org.apache.tapestry.services.DataSqueezer;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Constraint;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.persistence.PersistenceService;
import org.trails.test.Foo;

public class SqueezableSqueezeAdaptorTest extends MockObjectTestCase
{
	Mock dataSqueezerMock = new Mock(DataSqueezer.class);
	Mock persistenceMock = new Mock(PersistenceService.class);
	Mock descriptorServiceMock = new Mock(DescriptorService.class);
	SqueezableSqueezeAdaptor adaptor = new SqueezableSqueezeAdaptor();
	Foo foo;
	
	public void setUp() throws Exception
	{
		foo = new Foo();
		foo.setId(new Integer(1));
		adaptor.setDescriptorService((DescriptorService)descriptorServiceMock.proxy());
		adaptor.setPersistenceService((PersistenceService)persistenceMock.proxy());
	}
	
	public void testSqueeze() throws Exception
	{
		TrailsClassDescriptor classDescriptor = new TrailsClassDescriptor(Foo.class);
		IdentifierDescriptor idDescriptor = new IdentifierDescriptor(Foo.class, "id", Integer.class);
		classDescriptor.getPropertyDescriptors().add(idDescriptor);
		descriptorServiceMock.expects(once()).method("getClassDescriptor")
			.with(eq(Foo.class)).will(returnValue(classDescriptor));
		
		
		
		
		// need to make a contraint so i can do tests on the arguments
		dataSqueezerMock.expects(once()).method("squeeze").with(new Constraint(){

			public StringBuffer describeTo(StringBuffer arg0)
			{
				arg0.append("whatever");
				return arg0;
			}

			public boolean eval(Object arg)
			{
				ObjectIdentity oid = (ObjectIdentity)arg;
				return (oid.getId().equals(new Integer(1)) && oid.getEntityName().equals(Foo.class.getName()));
			}
			
		}).will(returnValue("squeezed"));
		String squeezed = adaptor.squeeze((DataSqueezer)dataSqueezerMock.proxy(), foo);
		
	}
	
	public void testUnsqueeze() throws Exception
	{
		ObjectIdentity oid = new ObjectIdentity(Foo.class.getName(), new Integer(1));
		dataSqueezerMock.expects(once()).method("unsqueeze").with(eq("squeezedOid")).will(returnValue(oid));
		persistenceMock.expects(once()).method("getInstance").with(eq(Foo.class), eq(new Integer(1))).will(returnValue(foo));
		assertEquals(foo, 
				adaptor.unsqueeze((DataSqueezer)dataSqueezerMock.proxy(), 
						SqueezableSqueezeAdaptor.PREFIX +"squeezedOid"));
	}

}
