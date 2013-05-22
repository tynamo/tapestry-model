package org.tynamo.examples.simple.functional;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.tynamo.test.AbstractContainerTest;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import static com.gargoylesoftware.htmlunit.WebAssert.*;

public class LocalizationTest extends AbstractContainerTest
{
	private HtmlPage startPage;

	@BeforeMethod
	public void setStartPage() throws Exception {
		startPage = webClient.getPage(BASEURI);
	}

	@Test
	public void testLocale() throws Exception
	{
		HtmlAnchor link = null;//(HtmlAnchor) new HtmlUnitXPath("//a[img/@alt='portuguese']").selectSingleNode(startPage);
		// FIXME choosing different locale not implemented yet
//		startPage = (HtmlPage) link.click();
//		assertTextPresent(startPage, "Listar");
//		HtmlPage listApplesPage = clickLink(startPage, "Listar Apples");
//		assertTextPresent(startPage, "Applar");
	}
}
