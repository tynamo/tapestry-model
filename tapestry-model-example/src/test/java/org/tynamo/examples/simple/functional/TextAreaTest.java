package org.tynamo.examples.simple.functional;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import org.testng.annotations.Test;
import org.tynamo.examples.simple.integration.BaseIntegrationTest;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import static org.testng.Assert.*;

public class TextAreaTest extends BaseIntegrationTest
{
	@Test
	public void testTextArea() throws Exception
	{
		HtmlPage newThingPage = webClient.getPage(BASEURI + "add/thing");
		assertNotNull(newThingPage.<HtmlForm>getHtmlElementById("form").getTextAreaByName("text") );
	}
}
