package org.trails.record;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.web.WebRequest;

public class ClientAsoPropertyPersistenceStrategy implements IClientAsoPropertyPersistenceStrategy
{

	private Set<String> propertiesNames = new HashSet<String>();
	private DataSqueezer dataSqueezer;
	protected WebRequest request;

	public void initializeService()
	{
	}

	public void store(String pageName, String idPath, String propertyName, Object newValue)
	{
	}

	public Collection getStoredChanges(String pageName)
	{
		return Collections.EMPTY_LIST;
	}

	public void discardStoredChanges(String pageName)
	{
	}

	public void addParametersForPersistentProperties(ServiceEncoding encoding, boolean post)
	{
		Defense.notNull(encoding, "encoding");
		for (String propertyName : propertiesNames)
		{
			encoding.setParameterValue(propertyName, dataSqueezer.squeeze(request.getAttribute(propertyName)));
		}
	}

	public void setRequest(WebRequest request)
	{
		this.request = request;
	}

	public void setDataSqueezer(DataSqueezer dataSqueezer)
	{
		this.dataSqueezer = dataSqueezer;
	}

	public void addPropertyName(String propertyName)
	{
		propertiesNames.add(propertyName);
	}
}
