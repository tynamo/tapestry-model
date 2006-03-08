package org.trails.page;

import java.util.ArrayList;
import java.util.List;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Persist;
import org.trails.TrailsRuntimeException;
import org.trails.callback.SearchCallback;
import org.trails.component.Utils;
import org.trails.descriptor.IClassDescriptor;

public abstract class SearchPage extends TrailsPage
{

    public SearchPage()
    {
        super();
    }
    
    public abstract String getTypeName();

	public abstract void setTypeName(String TypeName);

    @Override
    public void pushCallback()
    {
        getCallbackStack().push(new SearchCallback(getPageName(), getTypeName()));
    }
    
    public IClassDescriptor getClassDescriptor()
    {
    	try
    	{
    		return getDescriptorService().getClassDescriptor(Class.forName(getTypeName()));
    	}
    	catch (ClassNotFoundException e) 
    	{
			throw new TrailsRuntimeException(e);
		}
    }

    public String[] getSearchableProperties()
    {
        try
        {
            // Danger: gross code
            ArrayList<String> stringList = new ArrayList<String>();
            stringList.addAll(
                (List)Ognl.getValue("propertyDescriptors.{? searchable}.{name}", getClassDescriptor()));
            return stringList.toArray(new String[] {});
        }
        catch (OgnlException oe)
        {
            throw new TrailsRuntimeException(oe);
        }
    }
    
}
