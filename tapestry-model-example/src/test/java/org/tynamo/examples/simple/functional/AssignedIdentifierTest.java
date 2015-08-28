package org.tynamo.examples.simple.functional;

import static com.gargoylesoftware.htmlunit.WebAssert.assertTextPresent;

import org.testng.annotations.Test;
import org.tynamo.examples.simple.integration.BaseIntegrationTest;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class AssignedIdentifierTest extends BaseIntegrationTest
{
	@Test
	public void assigningStringId() throws Exception
	{
		HtmlPage newThing2Page = webClient.getPage(BASEURI + "add/thing2");
		HtmlForm newThing2Form = (HtmlForm) newThing2Page.getElementById("form");
		newThing2Form.<HtmlInput>getInputByName("textField").setValueAttribute("blah");
		newThing2Page = clickButton(newThing2Page, "saveAndReturn");
		assertErrorTextNotPresent(newThing2Page);
		assertTextPresent(newThing2Page, "blah");
	}
}
