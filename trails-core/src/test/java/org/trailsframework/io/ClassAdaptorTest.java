package org.trails.io;

import org.apache.tapestry.services.DataSqueezer;
import org.jmock.MockObjectTestCase;
import org.jmock.Mock;


public class ClassAdaptorTest extends MockObjectTestCase
{

	ClassAdaptor classAdaptor = new ClassAdaptor();
	Mock nextDataSqueezer;

	protected void setUp() throws Exception
	{
		nextDataSqueezer = new Mock(DataSqueezer.class);
	}

	public void testSqueeze()
	{
		nextDataSqueezer.expects(never());
		assertEquals("Dorg.trails.io.ClassAdaptor",
			classAdaptor.squeeze((DataSqueezer) nextDataSqueezer.proxy(), ClassAdaptor.class));

	}

	public void testUnSqueeze()
	{
		nextDataSqueezer.expects(never());
		assertEquals(ClassAdaptor.class,
			classAdaptor.unsqueeze((DataSqueezer) nextDataSqueezer.proxy(), "Dorg.trails.io.ClassAdaptor"));
	}

}