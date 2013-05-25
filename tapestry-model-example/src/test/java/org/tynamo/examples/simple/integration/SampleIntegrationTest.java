package org.tynamo.examples.simple.integration;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SampleIntegrationTest extends BaseIntegrationTest
{
	@Test
	public void assertCorrectTitle() throws Exception
	{
		final HtmlPage homePage = webClient.getPage(BASEURI);
		Assert.assertEquals(homePage.getTitleText(), "Tynamo!");
	}
}