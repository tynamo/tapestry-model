package org.tynamo.examples.simple.functional;

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;
import org.tynamo.examples.simple.integration.BaseIntegrationTest;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TextAreaTest extends BaseIntegrationTest
{
	@Test
	public void testTextArea() throws Exception
	{
		HtmlPage newThingPage = webClient.getPage(BASEURI + "add/thing");
		assertNotNull(newThingPage.<HtmlForm>getHtmlElementById("form").getTextAreaByName("textarea") );
	}
}
