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

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.annotations.InjectObject;
import org.trails.i18n.ResourceBundleMessageSource;


/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class TrailsComponent extends BaseComponent
{
    public static String DEFAULT = "Default";
    
    /**
     * Return the Spring ResourceBundleMessageSource. This is used to implement
     * i18n in all Trails components, accessing a i18n properties file in the
     * application instead of accessing the property file located in org.trais.component package.
     * By doing this, someone who would need i18n wouldn't need to change the property
     * located in the org.trails.component package and rebuild the trails.jar
     * @return
     */
    @InjectObject("spring:trailsMessageSource")
    public abstract ResourceBundleMessageSource getResourceBundleMessageSource();
    
    /**
     * This method will lookup for a message in Trails resource bundle
     * and return a message, if found, in the default Locale. This is normally called from
     * Component templates, so messages will be looked up outside the component,
     * making it easier for an user to localize Trails.
     */
    public String getMessage(String key) {
    	return getResourceBundleMessageSource().getMessageWithDefaultValue(key,
    								getContainer().getPage().getLocale(),
    								"[TRAILS][" + key.toUpperCase() + "]");
    }
        
}
