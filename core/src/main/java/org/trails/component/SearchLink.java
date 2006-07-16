package org.trails.component;

import java.util.Locale;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.trails.TrailsRuntimeException;
import org.trails.page.SearchPage;
import org.trails.page.TrailsPage;

public abstract class SearchLink extends TypeNavigationLink
{
    public static final String POSTFIX = "Search";
    public SearchLink()
    {
        super();
    }

    /**
     * Finds the search page for the type specified by the typeName
     * component parameter and forwards to it, setting the example model 
     * to a new instance.  This instance is use to build a query by
     * example.
     * @param cycle
     */
    public void click(IRequestCycle cycle)
    {
        SearchPage searchPage = (SearchPage)getPageResolver().resolvePage(
        		cycle, getTypeName(), TrailsPage.PageType.SEARCH);
        searchPage.setTypeName(getTypeName());
        cycle.activate(searchPage);
    }
    
    public String getLinkText() {
    	Locale locale = null;
    	IComponent container = getContainer();
    	if (container != null) {
    		IPage page = container.getPage();
    		if (page != null) {
    			IEngine engine = page.getEngine();
    			if (engine != null) {
    				locale = engine.getLocale();
    			}
    		}
    	}
    	Object[] params = new Object[]{getClassDescriptor().getDisplayName()};
       	return getResourceBundleMessageSource().getMessageWithDefaultValue("org.trails.component.searchlink",
       													params,
       													locale,
       													"[TRAILS][ORG.TRAILS.COMPONENT.SEARCHLINK]");
    }
}
