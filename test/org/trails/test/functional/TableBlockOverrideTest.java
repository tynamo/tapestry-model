package org.trails.test.functional;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TableBlockOverrideTest extends FunctionalTest
{
	public void testThatBlockOverrideAppears() throws Exception
	{
        HtmlPage listThingsPage = clickLinkOnPage(startPage, "List Things");
        HtmlPage newThingPage = clickLinkOnPage(listThingsPage, "New Thing");
        System.out.println(newThingPage.asXml());
        HtmlForm newThingForm = getFirstForm(newThingPage);
        getInputByName(newThingPage, "Name").setValueAttribute("blah");
        getInputByName(newThingPage, "Id").setValueAttribute("1");
        listThingsPage = clickButton(newThingForm, "Save");
        assertXPathPresent(listThingsPage, "//td[text()='This is where the name should go.blah']");
		
	}
}
