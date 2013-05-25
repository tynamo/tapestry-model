package org.tynamo.examples.simple.functional;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.testng.annotations.Test;
import org.tynamo.examples.simple.integration.BaseIntegrationTest;

public class SearchTest extends BaseIntegrationTest
{

	@Test
	public void testSearch() throws Exception
	{
		// Only with this test, for some reason getting:
		// Caused by: net.sourceforge.htmlunit.corejs.javascript.WrappedException: Wrapped com.gargoylesoftware.htmlunit.ScriptException:
		// Exception invoking jsxSet_innerHTML (http://localhost:8180/assets/0.0.1-SNAPSHOT/stack/en/core.js#7672)
		// Caused by: com.gargoylesoftware.htmlunit.ScriptException: missing ; before statement (JavaScript URL#1)
		webClient.setJavaScriptEnabled(false);
		HtmlPage listApplesPage = clickLink(startPage, "List Apples");
		HtmlPage newApplePage = clickLink(listApplesPage, "New Apple");
		HtmlForm form = newApplePage.getHtmlElementById("form");
		form.getInputByName("color").setValueAttribute("Blue");
		form.getSelectByName("origin").setSelectedAttribute("AMERICA", true);

		listApplesPage = clickButton(newApplePage, "saveAndReturn");

		webClient.setJavaScriptEnabled(true);

		// FIXME Search page not implemented yet
//		HtmlPage searchApplesPage = clickLink(listApplesPage, "Search Apples");
//		newApplePage.getHtmlElementById("form").getInputByName("color").setValueAttribute("Blue");
//		listApplesPage = clickButton(searchApplesPage, "Search");
//		assertXPathPresent(listApplesPage, "//td[text() = 'Blue']");
//		searchApplesPage = clickLink(listApplesPage, "Search Apples");
//		searchApplesPage.getHtmlElementById("searchform").getInputByName("color").setValueAttribute("lu");
//		listApplesPage = clickButton(searchApplesPage, "Search");
//		assertXPathPresent(listApplesPage, "//td[text() = 'Blue']");
	}

	// @Test 
	// FIXME Search not implemented yet
	public void testSearchByEnum() throws Exception
	{
		HtmlPage listApplesPage = clickLink(startPage, "List Apples");
		HtmlPage newApplePage = clickLink(listApplesPage, "New Apple");
		HtmlForm form = newApplePage.getHtmlElementById("form");
		form.getInputByName("color").setValueAttribute("Red");
		form.getSelectByName("origin").setSelectedAttribute("OCEANIA", true);
		listApplesPage = clickButton(newApplePage, "Ok");

		HtmlPage searchApplesPage = clickLink(listApplesPage, "Search Apples");
		searchApplesPage.<HtmlForm>getHtmlElementById("searchform").getSelectByName("origin").setSelectedAttribute("OCEANIA", true);

		listApplesPage = clickButton(searchApplesPage, "Search");
		assertXPathPresent(listApplesPage, "//td[text() = 'Red']");
		assertXPathNotPresent(listApplesPage, "//td[text() = 'Blue']");

		searchApplesPage = clickLink(listApplesPage, "Search Apples");
		searchApplesPage.<HtmlForm>getHtmlElementById("searchform").getSelectByName("origin").setSelectedAttribute("EUROPE", true);

		listApplesPage = clickButton(searchApplesPage, "Search");
		assertXPathNotPresent(listApplesPage, "//td[text() = 'Blue']");
		assertXPathNotPresent(listApplesPage, "//td[text() = 'Red']");
	}
}
