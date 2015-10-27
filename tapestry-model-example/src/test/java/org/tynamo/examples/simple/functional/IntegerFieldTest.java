package org.tynamo.examples.simple.functional;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.testng.annotations.Test;
import org.tynamo.examples.simple.integration.BaseIntegrationTest;

import static com.gargoylesoftware.htmlunit.WebAssert.assertTextPresent;

public class IntegerFieldTest extends BaseIntegrationTest {

	@Test
	public void testIntegerFields() throws Exception {
		HtmlPage newThingPage = webClient.getPage(BASEURI + "add/thing");
		HtmlForm form = newThingPage.getHtmlElementById("form");
//		form.<HtmlInput>getInputByName("id").setValueAttribute("3678");
		form.<HtmlInput>getInputByName("number").setValueAttribute("3");

		// @todo: Tynamo does not supports assigned ids that are not String
/*
		newThingPage = clickButton(newThingPage, "save");
		assertErrorTextNotPresent(newThingPage);
		assertTextPresent(newThingPage, "3678");
*/

	}

}
