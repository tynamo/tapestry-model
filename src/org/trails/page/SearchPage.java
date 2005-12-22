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
    
    @Persist
    public abstract Object getExampleModel();

    public abstract void setExampleModel(Object ExampleModel);

    public void search(IRequestCycle cycle)
    {
        ListPage listPage = (ListPage) Utils.findPage(cycle, 
                Utils.unqualify(getExampleModel().getClass().getName()) + "List", "List");
        listPage.setInstances(getPersistenceService().getInstances(getExampleModel()));
        pushCallback();
        cycle.activate(listPage);
    }

    @Override
    public void pushCallback()
    {
        getCallbackStack().push(new SearchCallback(getPageName(), getExampleModel()));
    }
    
    public IClassDescriptor getClassDescriptor()
    {
        return getDescriptorService().getClassDescriptor(getExampleModel().getClass());
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
