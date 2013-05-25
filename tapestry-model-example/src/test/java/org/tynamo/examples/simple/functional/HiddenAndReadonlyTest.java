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
package org.tynamo.examples.simple.functional;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.tynamo.examples.simple.integration.BaseIntegrationTest;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HiddenAndReadonlyTest extends BaseIntegrationTest
{
	@Test
	public void testThing2() throws Exception
	{
		//assertNull("no hidden column", new HtmlUnitXPath("//td/a[contains(text(), 'Hidden')]").selectSingleNode(listThing2sPage));
		HtmlPage newThing2Page = webClient.getPage(BASEURI + "add/thing2");
		assertXPathPresent(newThing2Page, "//div[@class='t-beaneditor-row']//label[contains(text(), 'Read Only')]/following-sibling::p[contains(text(), 'foo')]");

	}
}
