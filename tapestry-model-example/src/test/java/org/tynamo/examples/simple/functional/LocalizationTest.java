package org.tynamo.examples.simple.functional;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import org.testng.annotations.Test;
import org.tynamo.examples.simple.integration.BaseIntegrationTest;

public class LocalizationTest extends BaseIntegrationTest
{
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
