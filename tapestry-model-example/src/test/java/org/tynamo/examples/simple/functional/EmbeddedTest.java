package org.tynamo.examples.simple.functional;

import org.testng.annotations.Test;
import org.tynamo.examples.simple.integration.BaseIntegrationTest;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class EmbeddedTest extends BaseIntegrationTest
{
	@Test
	public void addPerson() throws Exception
	{
		HtmlPage newPersonPage = webClient.getPage(BASEURI + "add/person");
		HtmlForm form = newPersonPage.getHtmlElementById("form");
		form.getInputByName("textField_1").setValueAttribute("John");
		form.getInputByName("textField_2").setValueAttribute("Doe");
		form.getInputByName("textField").setValueAttribute("Sunnyville");

		HtmlPage showPersonPage = clickButton(newPersonPage, "saveAndReturn");

		assertXPathPresent(showPersonPage, "//dd[text()='John']");
		assertXPathPresent(showPersonPage, "//dd[text()='Doe']");
		assertXPathPresent(showPersonPage, "//dd[contains(text(),'Sunnyville')]");
	}
}
