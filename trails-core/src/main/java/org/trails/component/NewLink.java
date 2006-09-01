/*
 * Copyright 2004 Chris Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.trails.component;

import java.lang.reflect.Constructor;
import java.util.Locale;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.trails.TrailsRuntimeException;
import org.trails.page.EditPage;
import org.trails.page.TrailsPage;


/*
 * Created on Sep 29, 2004
 *
 */

/**
 * @author fus8882
 *
 */
public abstract class NewLink extends TypeNavigationLink
{
    public static String SUFFIX = "Edit";

    public abstract String getTypeName();

    public void click(IRequestCycle cycle)
    {
        ((TrailsPage)getPage()).pushCallback();
        EditPage page = (EditPage) getPageResolver().resolvePage(cycle, 
        		getTypeName(), 
        		TrailsPage.PageType.EDIT);
        
        try
        {
            Class clazz = Class.forName(getTypeName());
            Constructor constructor = clazz.getDeclaredConstructor(new Class[] {  });
            constructor.setAccessible(true);
            page.setModel(constructor.newInstance(new Object[] {  }));
            cycle.activate(page);
        }catch (Exception ex)
        {
            throw new TrailsRuntimeException(ex);
        }
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
       	return getResourceBundleMessageSource().getMessageWithDefaultValue("org.trails.component.newlink",
       											params,
       											locale,
       											"[TRAILS][ORG.TRAILS.COMPONENT.NEWLINK]");    		
    }
}
