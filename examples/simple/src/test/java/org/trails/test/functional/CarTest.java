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

		HtmlUnitXPath xpath = new HtmlUnitXPath("//span/preceding-sibling::label[contains(text(), 'Name')]/following-sibling::span/input");
		List nodes = xpath.selectNodes(newMakePage);
		getInputByName(newMakePage, "Name").setValueAttribute("Honda");

		listMakesPage = clickButton(newMakePage, "Ok");
		startPage = clickLinkOnPage(listMakesPage, "Home");
		HtmlPage listCarsPage = clickLinkOnPage(startPage, "List Cars");
		HtmlPage newCarPage = clickLinkOnPage(listCarsPage, "New Car");
		getInputByName(newCarPage, "Name").setValueAttribute("Accord");
		HtmlSelect makeSelect = (HtmlSelect) new HtmlUnitXPath("//span/preceding-sibling::label[contains(text(),  'Make')]/following-sibling::span/select")
			.selectSingleNode(newCarPage);
		makeSelect.setSelectedAttribute("1", true);
		listCarsPage = clickButton(newCarPage, "Ok");
		startPage = clickLinkOnPage(listCarsPage, "Home");
		listMakesPage = clickLinkOnPage(startPage, "List Makes");
		HtmlPage make1Page = clickLinkOnPage(listMakesPage, "1");
		assertXPathPresent(make1Page, "//td[text() = 'Accord']");
	}
}
