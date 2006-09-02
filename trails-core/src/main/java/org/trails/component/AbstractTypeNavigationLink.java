/*
 * Created on Jan 30, 2005
 *
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

import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IEngine;
import org.trails.TrailsRuntimeException;
import org.trails.i18n.ResourceBundleMessageSource;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;

import java.util.Locale;

/**
 * Common functionality for ListAllLink, NewLink, SearchLink
 * @author fus8882
 */
public abstract class AbstractTypeNavigationLink extends Link
{
    @InjectObject("spring:descriptorService")
    public abstract DescriptorService getDescriptorService();
    public abstract void setTypeName(String typeName);

    /**
     * @return Class object that this link targets.
     */
    public Class getType() {
        try {
            return Class.forName(getTypeName());
        } catch (ClassNotFoundException ce) {
            throw new TrailsRuntimeException(ce);
        }
    }

    /**
     * @return the class descriptor for the class that this link targets
     */
    public IClassDescriptor getClassDescriptor() {
        return getDescriptorService().getClassDescriptor(getType());
    }

    protected String generateLinkText(String displayName, String bundleKey, String defaultMessage) {
        Locale locale = null;
        IComponent container = getContainer();

        // attempt to find the locale or accept null
        if (container != null) {
            IPage page = container.getPage();
            if (page != null) {
                IEngine engine = page.getEngine();
                if (engine != null) {
                    locale = engine.getLocale();
                }
            }
        }

        Object[] params = new Object[]{displayName};
        ResourceBundleMessageSource messageSource = getResourceBundleMessageSource();
        return messageSource.getMessageWithDefaultValue(bundleKey, params, locale, defaultMessage);
    }    
}
