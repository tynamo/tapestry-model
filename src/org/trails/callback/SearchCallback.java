package org.trails.callback;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.callback.ICallback;
import org.trails.page.SearchPage;

/**
 * 
 * @author Chris Nelson
 * 
 * Return Control to a search page.
 *
 */
public class SearchCallback implements ICallback
{
    private String pageName;
    
    private Object exampleModel;
    
    public SearchCallback(String name, Object model)
    {
        super();
        // TODO Auto-generated constructor stub
        exampleModel = model;
        pageName = name;
    }

    public SearchCallback()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public void performCallback(IRequestCycle cycle)
    {
        SearchPage searchPage = (SearchPage)cycle.getPage(pageName);
        searchPage.setExampleModel(exampleModel);
        cycle.activate(searchPage);
    }

}
