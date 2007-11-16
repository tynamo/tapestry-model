package org.trails.page;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.tapestry.IRequestCycle;
import org.trails.TrailsRuntimeException;

import java.util.ArrayList;
import java.util.List;

public abstract class SearchPage extends TrailsPage
{

	public String[] getSearchableProperties()
	{
		try
		{
			//@note: Danger: gross code
			ArrayList<String> stringList = new ArrayList<String>();
			stringList.addAll(
					(List) Ognl.getValue("propertyDescriptors.{? searchable}.{name}", getClassDescriptor()));
			return stringList.toArray(new String[]{});
		}
		catch (OgnlException oe)
		{
			throw new TrailsRuntimeException(oe);
		}
	}

	public void activateTrailsPage(Object[] objects, IRequestCycle iRequestCycle)
	{
		if (getCallbackStack() != null)
		{
			getCallbackStack().clear();
		}
	}
}
