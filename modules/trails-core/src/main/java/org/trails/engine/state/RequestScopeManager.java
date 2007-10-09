package org.trails.engine.state;

import org.apache.tapestry.engine.state.StateObjectFactory;
import org.apache.tapestry.engine.state.StateObjectPersistenceManager;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.web.WebRequest;
import org.trails.record.IClientAsoPropertyPersistenceStrategy;


/**
 * Manager for the 'request' scope; state objects are stored as HttpRequest
 * attributes. It allows to ASOs to be persisted in the client.
 * 
 */
public class RequestScopeManager implements StateObjectPersistenceManager
{

	private WebRequest request;
	private DataSqueezer dataSqueezer;
	private IClientAsoPropertyPersistenceStrategy clientAsoPropertyPersistenceStrategy;
	public boolean stateful = true;

	private static final String PREFIX = "aso:";

	private String buildKey(String objectName)
	{
		return PREFIX + objectName;
	}

	public boolean exists(String objectName)
	{
		String key = buildKey(objectName);
		return request.getAttribute(key) != null || request.getParameterValue(key) != null;
	}

	public Object get(String objectName, StateObjectFactory factory)
	{
		String key = buildKey(objectName);

		if (request.getAttribute(key) != null)
		{
			return request.getAttribute(key);
		}

		Object result = unsqueeze(key);

		if (result == null)
		{
			result = factory.createStateObject();
			storeWithKey(key, result);
		}

		return result;
	}

	private synchronized Object unsqueeze(String key)
	{
		if (request.getAttribute(key) != null)
		{
			return request.getAttribute(key);
		}

		if (request.getParameterValue(key) != null)
		{
			storeWithKey(key, dataSqueezer.unsqueeze(request.getParameterValue(key)));
		}

		return request.getAttribute(key);
	}

	public void store(String objectName, Object stateObject)
	{
		String key = buildKey(objectName);
		storeWithKey(key, stateObject);
	}

	private void storeWithKey(String key, Object stateObject)
	{
		request.setAttribute(key, stateObject);
		if (isStateful())
		{
			clientAsoPropertyPersistenceStrategy.addPropertyName(key);
		}
	}

	public boolean isStateful()
	{
		return stateful && clientAsoPropertyPersistenceStrategy != null;
	}

	public void setRequest(WebRequest request)
	{
		this.request = request;
	}

	public void setDataSqueezer(DataSqueezer dataSqueezer)
	{
		this.dataSqueezer = dataSqueezer;
	}

	public void setClientAsoPropertyPersistenceStrategy(IClientAsoPropertyPersistenceStrategy clientAsoPropertyPersistenceStrategy)
	{
		this.clientAsoPropertyPersistenceStrategy = clientAsoPropertyPersistenceStrategy;
	}

	public void setStateful(boolean stateful)
	{
		this.stateful = stateful;
	}
}
