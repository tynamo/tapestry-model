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

import org.apache.tapestry.IRequestCycle;
import org.trails.TrailsRuntimeException;
import org.trails.page.EditPage;
import org.trails.page.TrailsPage;
import org.trails.page.PageResolver;

/**
 * Render a link allowing a new entity of the parameterized type to be created.
 * @author fus8882
 * @date Sep 29, 2004
 */
public abstract class NewLink extends AbstractTypeNavigationLink {
    public static String SUFFIX = "Edit";

    public abstract String getTypeName();

    public void click(IRequestCycle cycle) {
        ((TrailsPage)getPage()).pushCallback();
        PageResolver pageResolver = getPageResolver();
        EditPage page = (EditPage) pageResolver.resolvePage(cycle, getTypeName(), TrailsPage.PageType.EDIT);
        
        try {
            Class clazz = Class.forName(getTypeName());
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            page.setModel(constructor.newInstance());
            cycle.activate(page);
        } catch (Exception ex) {
            throw new TrailsRuntimeException(ex);
        }
    }
    
    /**
     * Get the text for the rendered link
     * @return Full i18n text in the form "List Foobars"
     */
    public String getLinkText() {
        String name = getClassDescriptor().getDisplayName();
        return generateLinkText(name, "org.trails.component.newlink", "[TRAILS][ORG.TRAILS.COMPONENT.NEWLINK]");
    }
}
