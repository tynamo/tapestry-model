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
package org.tynamo.examples.simple.functional;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.testng.annotations.Test;
import org.tynamo.examples.simple.integration.BaseIntegrationTest;

import static com.gargoylesoftware.htmlunit.WebAssert.assertTextPresent;

public class AssignedIdentifierTest extends BaseIntegrationTest
{
	@Test
	public void assigningStringId() throws Exception
	{
		HtmlPage newThing2Page = webClient.getPage(BASEURI + "add/thing2");
		HtmlForm newThing2Form = (HtmlForm) newThing2Page.getElementById("form");
		newThing2Form.<HtmlInput>getInputByName("identifier").setValueAttribute("blah");
		newThing2Page = clickButton(newThing2Page, "saveAndReturn");
		assertErrorTextNotPresent(newThing2Page);
		assertTextPresent(newThing2Page, "blah");
	}
}
