package org.tynamo.examples.simple.integration;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.testng.annotations.BeforeMethod;
import org.tynamo.test.AbstractContainerTest;

public abstract class BaseIntegrationTest extends AbstractContainerTest
{
	protected HtmlPage startPage;

	@BeforeMethod
	public void setStartPage() throws Exception {
		startPage = webClient.getPage(BASEURI + "home");
	}

}