package org.tynamo.test.functional;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class EmbeddedTest extends FunctionalTest
{

	public void testPerson() throws Exception
	{
		HtmlPage listPersonsPage = clickLinkOnPage(startPage, "List Persons");
		HtmlPage newPersonPage = clickLinkOnPage(listPersonsPage, "New Person");
		getInputByName(newPersonPage, "Name").setValueAttribute("John Doe");
		getInputByName(newPersonPage, "City").setValueAttribute("Sunnyville");
		newPersonPage = clickButton(newPersonPage, "Apply");
		assertEquals("Sunnyville", getInputByName(newPersonPage, "City").getAttributeValue("value"));
		listPersonsPage = clickButton(newPersonPage, "Ok");
		assertXPathPresent(listPersonsPage, "//td['John doe']");
	}
}
