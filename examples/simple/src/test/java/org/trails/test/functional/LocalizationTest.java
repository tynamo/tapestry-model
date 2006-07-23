package org.trails.test.functional;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;
import com.gargoylesoftware.htmlunit.jelly.AssertContentTypeTag;

public class LocalizationTest extends FunctionalTest
{

    public void testLocale() throws Exception
    {
        HtmlAnchor link = (HtmlAnchor)new HtmlUnitXPath("//a[img/@alt='portuguese']").selectSingleNode(startPage);
        startPage = (HtmlPage)link.click();
        assertTrue(startPage.asText().contains("Listar"));
        HtmlPage listApplesPage = clickLinkOnPage(startPage, "Listar Apples");
        assertTrue(listApplesPage.asText().contains("Applar"));
    }
}
