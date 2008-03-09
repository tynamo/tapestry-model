package org.trails.test.functional;

import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;

public class CarTest extends FunctionalTest
{

	public void testObjectTableOnEditPage() throws Exception
	{
		HtmlPage listMakesPage = clickLinkOnPage(startPage, "List Makes");
		HtmlPage newMakePage = clickLinkOnPage(listMakesPage, "New Make");

		getInputByName(newMakePage, "Name").setValueAttribute("Honda");

		listMakesPage = clickButton(newMakePage, "Ok");
		startPage = clickLinkOnPage(listMakesPage, "Home");
		HtmlPage listCarsPage = clickLinkOnPage(startPage, "List Cars");
		HtmlPage newCarPage = clickLinkOnPage(listCarsPage, "New Car");
		getInputByName(newCarPage, "Name").setValueAttribute("Accord");
		HtmlSelect makeSelect = getSelectByName(newCarPage, "Make");
		makeSelect.setSelectedAttribute("1", true);
		listCarsPage = clickButton(newCarPage, "Ok");
		startPage = clickLinkOnPage(listCarsPage, "Home");
		listMakesPage = clickLinkOnPage(startPage, "List Makes");
		HtmlPage make1Page = clickLinkOnPage(listMakesPage, "1");
		assertXPathPresent(make1Page, "//td[text() = 'Accord']");
	}

	public void testCancelAndDefaultCallback() throws Exception {

		HtmlPage listMakesPage = clickLinkOnPage(startPage, "List Makes");
		HtmlPage newMakePage = clickLinkOnPage(listMakesPage, "New Make");

		newMakePage = clickButton(newMakePage, "Cancel");
		assertXPathPresent(newMakePage, "//h1[text() = 'List Makes ']"); //note there an extra space in the h1 text
	}

	public void testCancelWithSessionCallbackStack() throws Exception
	{
		HtmlPage listMakesPage = clickLinkOnPage(startPage, "List Makes");
		HtmlPage listCarsPage = clickLinkOnPage(startPage, "List Cars");

		HtmlPage newMakePage = clickLinkOnPage(listMakesPage, "New Make"); //this is to add a callback pointing to the New Make page to the callbackStack
		HtmlPage newCarPage = clickLinkOnPage(listCarsPage, "New Car");

		newMakePage = clickButton(newCarPage, "Cancel");
		assertXPathPresent(newMakePage, "//h1[text() = 'Edit Make']");
	}
}
