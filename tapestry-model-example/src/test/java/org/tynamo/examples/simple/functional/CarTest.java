package org.tynamo.examples.simple.functional;

import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSpanElement;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.tynamo.examples.simple.integration.BaseIntegrationTest;

public class CarTest extends BaseIntegrationTest
{

	@Test
	public void testObjectTableOnEditPage() throws Exception
	{
		HtmlPage listMakesPage = clickLink(startPage, "List Makes");
		HtmlPage newMakePage = clickLink(listMakesPage, "New Make");
		HtmlForm form = newMakePage.getHtmlElementById("form");
		form.getInputByName("name").setValueAttribute("Honda");
		
		listMakesPage = clickButton(newMakePage, "saveAndReturn");

		startPage = clickLink(listMakesPage, "Home");

		HtmlPage listModelsPage = clickLink(startPage, "List Car Models");
		HtmlPage newModelPage = clickLink(listModelsPage, "New Car Model");
		form = newModelPage.getHtmlElementById("form");
		form.getInputByName("name").setValueAttribute("Civic");
		listModelsPage = clickButton(newModelPage, "saveAndReturn");

		startPage = clickLink(listModelsPage, "Home");

		HtmlPage listCarsPage = clickLink(startPage, "List Cars");
		HtmlPage newCarPage = clickLink(listCarsPage, "New Car");
		form = newModelPage.getHtmlElementById("form");
		form.getInputByName("name").setValueAttribute("Accord");
		// kaosko -2009-11-02: not implemented yet
//		HtmlSelect makeSelect = form.getSelectByName("Make");
//		makeSelect.setSelectedAttribute("1", true);
//		listCarsPage = clickButton(newCarPage, "saveAndReturn");
//
//		startPage = clickLink(listCarsPage, "Home");
//
//		listModelsPage = clickLink(startPage, "List Models");
//		HtmlPage model1Page = clickLink(listModelsPage, "1");
//		assertXPathPresent(model1Page, "//td/a[contains(text(),'Accord')]");
	}

	@Test
	public void testCancel() throws Exception {

		HtmlPage listMakesPage = clickLink(startPage, "List Makes");
		HtmlPage newMakePage = clickLink(listMakesPage, "New Make");

		newMakePage = clickLink(newMakePage, "Cancel");

		assertXPathPresent(newMakePage, "//h1[contains(text(),'List')]");
	}

	@Test
	public void testListOnlyOrphanInstances() throws Exception {

		HtmlPage newMakePage = webClient.getPage(BASEURI +"add/make");
		HtmlForm form = newMakePage.getHtmlElementById("form");
		form.getInputByName("name").setValueAttribute("Honda");
		newMakePage = clickButton(newMakePage, "saveAndReturn");
		HtmlDefinitionDescription ddElement = (HtmlDefinitionDescription)newMakePage.getByXPath("//dt[contains(text(),'Id')]/following-sibling::dd").get(0);
		int hondaId = Integer.parseInt(ddElement.getTextContent());

		newMakePage = webClient.getPage(BASEURI +"add/make");
		form = newMakePage.getHtmlElementById("form");
		form.getInputByName("name").setValueAttribute("Toyota");
		newMakePage = clickButton(newMakePage, "saveAndReturn");
		ddElement = (HtmlDefinitionDescription)newMakePage.getByXPath("//dt[contains(text(),'Id')]/following-sibling::dd").get(0);
		int toyotaId = Integer.parseInt(ddElement.getTextContent());

		HtmlPage newModelPage = webClient.getPage(BASEURI +"add/carmodel");
		HtmlForm newModelForm = newModelPage.getHtmlElementById("form");
		newModelForm.getInputByName("name").setValueAttribute("Prius");
		newModelPage = clickButton(newModelPage, "saveAndReturn");
		ddElement = (HtmlDefinitionDescription)newModelPage.getByXPath("//dt[contains(text(),'Id')]/following-sibling::dd").get(0);
		int priusId = Integer.parseInt(ddElement.getTextContent());

		newModelPage = webClient.getPage(BASEURI +"add/carmodel");
		newModelForm = newModelPage.getHtmlElementById("form");
		newModelForm.getInputByName("name").setValueAttribute("Sedan");
		clickButton(newModelPage, "saveAndReturn");

		HtmlPage editMakePage = webClient.getPage(BASEURI +"edit/make/" + hondaId);

		assertXPathPresent(editMakePage, "//input[@value='Honda']");
		assertXPathPresent(editMakePage, "//select[@id='palette_set-avail']");
		assertXPathNotPresent(editMakePage, "//select[@id='palette_set-avail'][not(node())]");
		assertXPathPresent(editMakePage, "//select[@id='palette_set-avail']/option[text()='Prius']");
		assertXPathPresent(editMakePage, "//select[@id='palette_set-avail']/option[text()='Sedan']");

		HtmlPage editModelPage = webClient.getPage(BASEURI + "edit/carmodel/" + priusId);
		form = editModelPage.getHtmlElementById("form");
		form.getSelectByName("make").getOptionByText("Toyota").setSelected(true);
		clickButton(editModelPage, "saveAndReturn");

		editMakePage = webClient.getPage(BASEURI +"edit/make/" + toyotaId);
		assertXPathNotPresent(editMakePage, "//select[@id='palette_set-avail'][not(node())]");
		assertXPathNotPresent(editMakePage, "//select[@id='palette_set'][not(node())]");

		editMakePage = webClient.getPage(BASEURI +"edit/make/" + hondaId);

		assertXPathPresent(editMakePage, "//select[@id='palette_set-avail']");
		assertXPathNotPresent(editMakePage, "//select[@id='palette_set-avail'][not(node())]");
		assertXPathPresent(editMakePage, "//select[@id='palette_set'][not(node())]");
		assertXPathNotPresent(editMakePage, "//select[@id='palette_set-avail']/option[text()='Prius']");
		assertXPathPresent(editMakePage, "//select[@id='palette_set-avail']/option[text()='Sedan']");

	}
}
