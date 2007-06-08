package org.trails.io;

import org.apache.tapestry.services.DataSqueezer;
import org.jmock.MockObjectTestCase;
import org.jmock.cglib.Mock;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.test.Foo;


public class ClassDescriptorSqueezerStrategyTest extends MockObjectTestCase
{

	ClassDescriptorSqueezerStrategy classDescriptorSqueezeStrategy = new ClassDescriptorSqueezerStrategy();
	TrailsClassDescriptor descriptor = new TrailsClassDescriptor(Foo.class);
	Mock descriptorServiceMock = new Mock(DescriptorService.class);
	Mock nextDataSqueezer;

	protected void setUp() throws Exception
	{
		nextDataSqueezer = new Mock(DataSqueezer.class);
		classDescriptorSqueezeStrategy.setDescriptorService((DescriptorService) descriptorServiceMock.proxy());
	}

	public void testSqueeze()
	{
		nextDataSqueezer.expects(once()).method("squeeze").with(eq(Foo.class))
			.will(returnValue("Dorg.trails.test.Foo"));
		assertEquals("YDorg.trails.test.Foo",
			classDescriptorSqueezeStrategy.squeeze((DataSqueezer) nextDataSqueezer.proxy(), descriptor));

	}

	public void testUnSqueeze()
	{

		descriptorServiceMock.expects(once()).method("getClassDescriptor").with(eq(Foo.class))
			.will(returnValue(descriptor));

		nextDataSqueezer.expects(once()).method("unsqueeze").with(eq("Dorg.trails.test.Foo"))
			.will(returnValue(Foo.class));

		assertSame(descriptor,
			classDescriptorSqueezeStrategy.unsqueeze((DataSqueezer) nextDataSqueezer.proxy(),
				"YDorg.trails.test.Foo"));
	}

}