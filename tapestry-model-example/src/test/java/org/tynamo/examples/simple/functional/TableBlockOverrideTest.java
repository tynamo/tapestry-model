package org.tynamo.examples.simple.functional;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.tynamo.test.AbstractContainerTest;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TableBlockOverrideTest extends AbstractContainerTest
{
	private HtmlPage startPage;

	@BeforeMethod
	public void setStartPage() throws Exception {
		startPage = webClient.getPage(BASEURI);
	}

	@Test
	public void overrideBlock() throws Exception
	{
		HtmlPage listThingsPage = clickLink(startPage, "List Things");
		HtmlPage newThingPage = clickLink(listThingsPage, "New Thing");
		HtmlForm form = newThingPage.getHtmlElementById("form");
		form.<HtmlInput>getInputByName("name").setValueAttribute("blah"); // it shouldn't be duplicated
		// FIXME currently Thing doesn't allow setting id, should we use Thing2?
		// Oh - the likely issue is that this customization isn't currently implemented
//		form.<HtmlInput>getInputByName("identifier").setValueAttribute("2");  // it shouldn't be duplicated either
//		listThingsPage = clickButton(newThingPage, "saveAndReturn");
//		assertXPathPresent(listThingsPage, "//p[text()='This is where the name should go.blah']");

	}
}
