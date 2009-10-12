package org.tynamo.test.functional;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TextAreaTest extends FunctionalTest
{

	public void testTextArea() throws Exception
	{
		HtmlPage listThingsPage = clickLinkOnPage(startPage, "List Things");
		HtmlPage newThingPage = clickLinkOnPage(listThingsPage, "New Thing");
		HtmlForm newThingForm = getFirstForm(newThingPage);
		assertNotNull(newThingForm.getTextAreasByName("textArea").get(0));
	}
}
