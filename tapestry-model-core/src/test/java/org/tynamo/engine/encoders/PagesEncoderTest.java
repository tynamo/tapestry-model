package org.tynamo.engine.encoders;

import org.apache.tapestry.engine.ServiceEncoding;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.jmock.Expectations;
import org.tynamo.services.ServiceConstants;


public class PagesEncoderTest extends MockObjectTestCase
{

	protected ServiceEncoding newEncoding()
	{
		return mock(ServiceEncoding.class);
	}

	protected void trainGetParameterValue(final ServiceEncoding encoding, final String name, final String value)
	{
		checking(new Expectations()
		{
			{
				atLeast(1).of(encoding).getParameterValue(name);
				will(returnValue(value));
			}
		});
	}

	public void testWrongService()
	{
		ServiceEncoding encoding = newEncoding();
		trainGetParameterValue(encoding, ServiceConstants.SERVICE, "foo");
		new PagesEncoder().encode(encoding);
		verify();
	}
}