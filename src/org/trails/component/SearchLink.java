package org.trails.component;

import java.util.Locale;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.springframework.context.NoSuchMessageException;
import org.trails.TrailsRuntimeException;
import org.trails.descriptor.DescriptorService;
import org.trails.page.SearchPage;

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
        SearchPage searchPage = (SearchPage)findPage(cycle, POSTFIX);
        try
        {
            searchPage.setExampleModel(getType().newInstance());
        }
        catch (Exception ex)
        {
            throw new TrailsRuntimeException(ex);
        }
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
