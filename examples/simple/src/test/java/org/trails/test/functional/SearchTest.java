package org.trails.test.functional;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

public class SearchTest extends FunctionalTest
{
	public void testSearch() throws Exception
	{
		HtmlPage listApplesPage = clickLinkOnPage(startPage, "List Apples");
		HtmlPage newApplePage = clickLinkOnPage(listApplesPage, "New Apple");
		getInputByName(newApplePage, "Color").setValueAttribute("Blue");
		HtmlSelect makeSelect = getSelectByName(newApplePage, "Origin");
		makeSelect.setSelectedAttribute("AMERICA", true);

		listApplesPage = clickButton(newApplePage, "Ok");
		HtmlPage searchApplesPage = clickLinkOnPage(listApplesPage, "Search Apples");
		getInputByName(searchApplesPage, "Color").setValueAttribute("Blue");
		listApplesPage = clickButton(searchApplesPage, "Search");
		assertXPathPresent(listApplesPage, "//td[text() = 'Blue']");
		searchApplesPage = clickLinkOnPage(listApplesPage, "Search Apples");
		getInputByName(searchApplesPage, "Color").setValueAttribute("lu");
		listApplesPage = clickButton(searchApplesPage, "Search");
		assertXPathPresent(listApplesPage, "//td[text() = 'Blue']");
	}

	public void testSearchByEnum() throws Exception
	{
		HtmlPage listApplesPage = clickLinkOnPage(startPage, "List Apples");
		HtmlPage newApplePage = clickLinkOnPage(listApplesPage, "New Apple");
		getInputByName(newApplePage, "Color").setValueAttribute("Red");
		HtmlSelect makeSelect = getSelectByName(newApplePage, "Origin");
		makeSelect.setSelectedAttribute("OCEANIA", true);
		listApplesPage = clickButton(newApplePage, "Ok");

		HtmlPage searchApplesPage = clickLinkOnPage(listApplesPage, "Search Apples");
		makeSelect = getSelectByName(searchApplesPage, "Origin");
		makeSelect.setSelectedAttribute("OCEANIA", true);

		listApplesPage = clickButton(searchApplesPage, "Search");
		assertXPathPresent(listApplesPage, "//td[text() = 'Red']");
		assertXPathNotPresent(listApplesPage, "//td[text() = 'Blue']");

		searchApplesPage = clickLinkOnPage(listApplesPage, "Search Apples");
		makeSelect = getSelectByName(searchApplesPage, "Origin");
		makeSelect.setSelectedAttribute("EUROPE", true);

		listApplesPage = clickButton(searchApplesPage, "Search");
		assertXPathNotPresent(listApplesPage, "//td[text() = 'Blue']");
		assertXPathNotPresent(listApplesPage, "//td[text() = 'Red']");
	}
}
