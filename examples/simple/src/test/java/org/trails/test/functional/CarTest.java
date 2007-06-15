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
}
