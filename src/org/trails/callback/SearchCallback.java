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
public class SearchCallback extends TrailsCallback
{
    
    private Object exampleModel;
    
    public SearchCallback(String name, Object model)
    {
        super(name);
        exampleModel = model;
    }

    public SearchCallback(String name)
    {
        super(name);
    }

    public void performCallback(IRequestCycle cycle)
    {
        SearchPage searchPage = (SearchPage)cycle.getPage(getPageName());
        searchPage.setExampleModel(exampleModel);
        cycle.activate(searchPage);
    }

}
