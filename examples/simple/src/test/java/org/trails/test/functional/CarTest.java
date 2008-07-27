package org.trails.test.functional;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

public class CarTest extends FunctionalTest
{

	public void testObjectTableOnEditPage() throws Exception
	{
		HtmlPage listMakesPage = clickLinkOnPage(startPage, "List Makes");
		HtmlPage newMakePage = clickLinkOnPage(listMakesPage, "New Make");
		getInputByName(newMakePage, "Name").setValueAttribute("Honda");
		listMakesPage = clickButton(newMakePage, "Ok");

		startPage = clickLinkOnPage(listMakesPage, "Home");

		HtmlPage listModelsPage = clickLinkOnPage(startPage, "List Models");
		HtmlPage newModelPage = clickLinkOnPage(listModelsPage, "New Model");
		getInputByName(newModelPage, "Name").setValueAttribute("Civic");
		listModelsPage = clickButton(newModelPage, "Ok");

		startPage = clickLinkOnPage(listModelsPage, "Home");

		HtmlPage listCarsPage = clickLinkOnPage(startPage, "List Cars");
		HtmlPage newCarPage = clickLinkOnPage(listCarsPage, "New Car");
		getInputByName(newCarPage, "Name").setValueAttribute("Accord");
		HtmlSelect makeSelect = getSelectByName(newCarPage, "Make");
		makeSelect.setSelectedAttribute("1", true);
		listCarsPage = clickButton(newCarPage, "Ok");

		startPage = clickLinkOnPage(listCarsPage, "Home");

		listModelsPage = clickLinkOnPage(startPage, "List Models");
		HtmlPage model1Page = clickLinkOnPage(listModelsPage, "1");
		assertXPathPresent(model1Page, "//td/a[contains(text(),'Accord')]");
	}

	public void testCancelAndDefaultCallback() throws Exception {

		HtmlPage listMakesPage = clickLinkOnPage(startPage, "List Makes");
		HtmlPage newMakePage = clickLinkOnPage(listMakesPage, "New Make");

		newMakePage = clickButton(newMakePage, "Cancel");

		assertXPathPresent(newMakePage, "//h1[contains(text(),'List Makes')]");
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
