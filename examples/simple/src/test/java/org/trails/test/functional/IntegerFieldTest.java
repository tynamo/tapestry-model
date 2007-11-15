package org.trails.test.functional;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class IntegerFieldTest extends FunctionalTest
{

	public void testIntegerFields() throws Exception
	{
		HtmlPage listThingsPage = clickLinkOnPage(startPage, "List Things");
		HtmlPage newThingPage = clickLinkOnPage(listThingsPage, "New Thing");
		getInputByName(newThingPage, "Id").setValueAttribute("1");
		getInputByName(newThingPage, "Number").setValueAttribute("3");
		newThingPage = clickButton(newThingPage, "Apply");
		assertNotNull(getId("Id", newThingPage));
	}
}
