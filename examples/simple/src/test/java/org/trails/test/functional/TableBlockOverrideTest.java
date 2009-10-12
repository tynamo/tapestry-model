package org.tynamo.test.functional;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TableBlockOverrideTest extends FunctionalTest
{
	public void testThatBlockOverrideAppears() throws Exception
	{
		HtmlPage listThingsPage = clickLinkOnPage(startPage, "List Things");
		HtmlPage newThingPage = clickLinkOnPage(listThingsPage, "New Thing");
		HtmlForm newThingForm = getFirstForm(newThingPage);
		getInputByName(newThingPage, "Name").setValueAttribute("blah"); // it shouldn't be duplicated
		getInputByName(newThingPage, "Id").setValueAttribute("2");  // it shouldn't be duplicated either
		listThingsPage = clickButton(newThingForm, "Ok");
		assertXPathPresent(listThingsPage, "//p[text()='This is where the name should go.blah']");

	}
}
