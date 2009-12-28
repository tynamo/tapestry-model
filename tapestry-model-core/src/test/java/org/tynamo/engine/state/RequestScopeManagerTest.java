package org.tynamo.engine.state;

import org.apache.tapestry.engine.state.StateObjectFactory;
import org.apache.tapestry.web.WebRequest;
import org.jmock.Expectations;
import org.jmock.integration.junit3.MockObjectTestCase;
import org.tynamo.record.IClientAsoPropertyPersistenceStrategy;


public class RequestScopeManagerTest extends MockObjectTestCase
{
	private StateObjectFactory newFactory(final Object stateObject)
	{
		final StateObjectFactory factory = mock(StateObjectFactory.class);

		checking(new Expectations()
		{
			{
				atLeast(1).of(factory).createStateObject();
				will(returnValue(stateObject));
			}
		});

		return factory;
	}

	private WebRequest newRequest(final String key, final Object value)
	{
		final WebRequest request = mock(WebRequest.class);

		checking(new Expectations()
		{
			{
				atLeast(1).of(request).getAttribute("aso:" + key);
				will(returnValue(value));
			}
		});
		return request;
	}

	public void testMissing()
	{
		final WebRequest request = newRequest("myAso", null);

		checking(new Expectations()
		{
			{
				atLeast(1).of(request).getParameterValue("aso:myAso");
				will(returnValue(null));
			}
		});

		RequestScopeManager m = new RequestScopeManager();
		m.setRequest(request);

		assertEquals(false, m.exists("myAso"));

		verify();
	}

	public void testExists()
	{
		final WebRequest request = newRequest("myAso", "someObject");

		RequestScopeManager m = new RequestScopeManager();
		m.setRequest(request);

		assertEquals(true, m.exists("myAso"));

		verify();
	}

	public void testGetExists()
	{
		Object stateObject = new Object();
		final WebRequest request = newRequest("myAso", stateObject);

		RequestScopeManager m = new RequestScopeManager();
		m.setRequest(request);

		assertSame(stateObject, m.get("myAso", null));

		verify();
	}

	public void testGetAndCreate()
	{
		final Object stateObject = new Object();
		final WebRequest request = newRequest("myAso", null);

		checking(new Expectations()
		{
			{
				exactly(1).of(request).getParameterValue("aso:myAso");
				will(returnValue(null));
				exactly(1).of(request).setAttribute("aso:myAso", stateObject);
			}
		});

		StateObjectFactory factory = newFactory(stateObject);

		RequestScopeManager m = new RequestScopeManager();
		m.setRequest(request);

		assertSame(stateObject, m.get("myAso", factory));

		verify();
	}

	public void testStoreStateless()
	{
		final Object stateObject = new Object();
		final WebRequest request = mock(WebRequest.class);
		final IClientAsoPropertyPersistenceStrategy persistenceStrategy = mock(IClientAsoPropertyPersistenceStrategy.class);

		checking(new Expectations()
		{
			{
				exactly(1).of(request).setAttribute("aso:myAso", stateObject);
				never(persistenceStrategy);
			}
		});

		RequestScopeManager m = new RequestScopeManager();
		m.setRequest(request);
		m.setStateful(false);

		m.store("myAso", stateObject);

		verify();
	}


	public void testStoreStateful()
	{
	}

}