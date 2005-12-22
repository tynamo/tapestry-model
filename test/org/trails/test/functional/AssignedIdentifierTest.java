/*
 * Created on Dec 13, 2004
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
package org.trails.test.functional;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AssignedIdentifierTest extends FunctionalTest
{
    public void testStringId() throws Exception
    {
        HtmlPage listThing2sPage = clickLinkOnPage(startPage, "List Thing2s");
        HtmlPage newThing2Page = clickLinkOnPage(listThing2sPage, "New Thing2");
        HtmlForm newThing2Form = getFirstForm(newThing2Page);
        newThing2Form.getInputByName("idStringField").setValueAttribute("blah");
        newThing2Page = clickButton(newThing2Form, "Apply");
        assertNull("no errors", getErrorDiv(newThing2Page));
        assertEquals("right id", "blah", getId("Identifier", newThing2Page));
    }
}
