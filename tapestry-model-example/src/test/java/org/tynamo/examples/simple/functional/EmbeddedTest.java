package org.tynamo.examples.simple.functional;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.testng.annotations.Test;
import org.tynamo.examples.simple.integration.BaseIntegrationTest;


public class EmbeddedTest extends BaseIntegrationTest
{
	@Test
	public void addPerson() throws Exception
	{
		HtmlPage newPersonPage = webClient.getPage(BASEURI + "add/person");
		HtmlForm form = newPersonPage.getHtmlElementById("form");
		form.getInputByName("firstName").setValueAttribute("John");
		form.getInputByName("lastName").setValueAttribute("Doe");
		form.getInputByName("city").setValueAttribute("Sunnyville");

		HtmlPage showPersonPage = clickButton(newPersonPage, "saveAndReturn");

		assertXPathPresent(showPersonPage, "//dd[text()='John']");
		assertXPathPresent(showPersonPage, "//dd[text()='Doe']");
		assertXPathPresent(showPersonPage, "//dd[contains(text(),'Sunnyville')]");
	}
}
