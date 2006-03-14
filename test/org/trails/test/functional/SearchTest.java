package org.trails.test.functional;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class SearchTest extends FunctionalTest
{
	public void testSearch() throws Exception
	{
        HtmlPage listApplesPage = clickLinkOnPage(startPage, "List Apples");
		HtmlPage newApplePage = clickLinkOnPage(listApplesPage, "New Apple");
		getInputByName(newApplePage, "Color").setValueAttribute("Blue");
		listApplesPage = clickButton(newApplePage, "Ok");
		HtmlPage searchApplesPage = clickLinkOnPage(listApplesPage, "Search Apple");
		getInputByName(searchApplesPage, "Color").setValueAttribute("Blue");
		listApplesPage = clickButton(searchApplesPage, "Search");
		assertXPathPresent(listApplesPage, "//td[text() = 'Blue']");
	}
}
