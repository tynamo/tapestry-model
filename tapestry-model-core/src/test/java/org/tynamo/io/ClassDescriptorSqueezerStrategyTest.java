package org.tynamo.io;

import org.apache.tapestry.services.DataSqueezer;
import org.jmock.MockObjectTestCase;
import org.jmock.Mock;
import org.tynamo.descriptor.DescriptorService;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.test.Foo;


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
		assertEquals("Yorg.tynamo.test.Foo",
			classDescriptorSqueezeStrategy.squeeze((DataSqueezer) nextDataSqueezer.proxy(), descriptor));

	}

	public void testUnSqueeze()
	{

		descriptorServiceMock.expects(once()).method("getClassDescriptor").with(eq(Foo.class))
			.will(returnValue(descriptor));

		assertSame(descriptor,
			classDescriptorSqueezeStrategy.unsqueeze((DataSqueezer) nextDataSqueezer.proxy(),
				"Yorg.tynamo.test.Foo"));
	}

}