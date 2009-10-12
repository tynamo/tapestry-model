package org.tynamo.io;

import java.util.Date;

import org.apache.tapestry.services.DataSqueezer;
import org.jmock.MockObjectTestCase;
import org.jmock.Mock;
import org.tynamo.descriptor.DescriptorService;
import org.tynamo.descriptor.IPropertyDescriptor;
import org.tynamo.descriptor.TrailsClassDescriptor;
import org.tynamo.descriptor.TrailsPropertyDescriptor;
import org.tynamo.test.Foo;


public class PropertyDescriptorSqueezerStrategyTest extends MockObjectTestCase
{

	PropertyDescriptorSqueezerStrategy propertyDescriptorSqueezeStrategy = new PropertyDescriptorSqueezerStrategy();
	IPropertyDescriptor fooDateDescriptor = new TrailsPropertyDescriptor(Foo.class, "date", Date.class);
	Mock descriptorServiceMock = new Mock(DescriptorService.class);
	Mock nextDataSqueezer;

	protected void setUp() throws Exception
	{
		nextDataSqueezer = new Mock(DataSqueezer.class);
		propertyDescriptorSqueezeStrategy.setDescriptorService((DescriptorService) descriptorServiceMock.proxy());
	}

	public void testSqueeze()
	{
		nextDataSqueezer.expects(once()).method("squeeze").with(eq(Foo.class))
			.will(returnValue("Dorg.tynamo.test.Foo"));
		assertEquals("WDorg.tynamo.test.Foo.date",
			propertyDescriptorSqueezeStrategy.squeeze((DataSqueezer) nextDataSqueezer.proxy(), fooDateDescriptor));

	}

	public void testUnSqueeze()
	{
		TrailsClassDescriptor descriptor = new TrailsClassDescriptor(Foo.class);
		descriptor.getPropertyDescriptors().add(fooDateDescriptor);

		descriptorServiceMock.expects(once()).method("getClassDescriptor").with(eq(Foo.class))
			.will(returnValue(descriptor));

		nextDataSqueezer.expects(once()).method("unsqueeze").with(eq("Dorg.tynamo.test.Foo"))
			.will(returnValue(Foo.class));
		assertSame(fooDateDescriptor,
			propertyDescriptorSqueezeStrategy.unsqueeze((DataSqueezer) nextDataSqueezer.proxy(),
				"WDorg.tynamo.test.Foo.date"));
	}

}