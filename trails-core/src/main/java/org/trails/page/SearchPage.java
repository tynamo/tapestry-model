package org.trails.page;

import java.util.ArrayList;
import java.util.List;

import ognl.Ognl;
import ognl.OgnlException;
import org.apache.tapestry.annotations.Persist;
import org.trails.TrailsRuntimeException;
import org.trails.callback.SearchCallback;
import org.trails.descriptor.IClassDescriptor;

public abstract class SearchPage extends TrailsPage
{

	@Persist
	public abstract Class getType();

	public abstract void setType(Class type);


	@Override
	public void pushCallback()
	{
		getCallbackStack().push(new SearchCallback(getPageName(), getType()));
	}

	public IClassDescriptor getClassDescriptor()
	{
		return getDescriptorService().getClassDescriptor(getType());
	}

	public String[] getSearchableProperties()
	{
		try
		{
			// Danger: gross code
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

}
