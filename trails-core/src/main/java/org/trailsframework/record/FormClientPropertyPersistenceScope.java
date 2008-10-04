package org.trails.record;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.record.AbstractPrefixedClientPropertyPersistenceScope;
import org.apache.tapestry.record.PersistentPropertyData;

/**
 * Based on com.zillow.web.infrastructure.FormClientPropertyPersistenceStrategy
 * described in http://wiki.apache.org/jakarta-tapestry/FormClientPersistence
 */
public class FormClientPropertyPersistenceScope extends AbstractPrefixedClientPropertyPersistenceScope
{
	private IRequestCycle _requestCycle;

	public FormClientPropertyPersistenceScope()
	{
		super("form:");
	}

	public boolean shouldEncodeState(ServiceEncoding encoding, String pageName, PersistentPropertyData data)
	{
		return pageName.equals(_requestCycle.getPage().getPageName());
	}

	public void setRequestCycle(IRequestCycle requestCycle)
	{
		_requestCycle = requestCycle;
	}

}