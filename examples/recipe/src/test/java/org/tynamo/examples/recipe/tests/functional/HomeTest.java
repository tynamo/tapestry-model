package org.tynamo.examples.recipe.tests.functional;

import org.testng.annotations.Test;
import org.tynamo.test.AbstractContainerTest;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HomeTest extends AbstractContainerTest
{

	@Test
	public void testStartPage() throws Exception
	{
        final HtmlPage startPage = webClient.getPage(BASEURI);
		assertXPathPresent(startPage, "//h1[contains(text(),'Welcome to Tynamo')]");
	}

}
