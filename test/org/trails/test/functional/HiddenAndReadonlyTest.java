/*
 * Created on Dec 22, 2004
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

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;

/**
 * @author fus8882
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HiddenAndReadonlyTest extends FunctionalTest
{
    public void testThing2() throws Exception
    {
        HtmlPage listThing2sPage = clickLinkOnPage(startPage, "List Thing2s");
        assertNull("no hidden column", new HtmlUnitXPath("//td/a[text() = 'Hidden'").selectSingleNode(listThing2sPage));
        HtmlPage newThing2Page = clickLinkOnPage(listThing2sPage, "New Thing2");
        assertNull("no hidden editor", new HtmlUnitXPath("//label[text() = 'Hidden']").selectSingleNode(newThing2Page));
        assertXPathPresent(newThing2Page,
                "//span[contains(preceding-sibling::label, 'Read Only') contains(text(), 'foo')]");
        
    }
}
