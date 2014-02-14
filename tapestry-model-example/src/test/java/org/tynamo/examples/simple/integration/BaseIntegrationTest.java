package org.tynamo.examples.simple.integration;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.internal.InternalConstants;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.tynamo.test.AbstractContainerTest;

public abstract class BaseIntegrationTest extends AbstractContainerTest
{
	protected HtmlPage startPage;

	@BeforeClass
	@Override
	public void startContainer() throws Exception {
		System.setProperty(InternalConstants.DISABLE_DEFAULT_MODULES_PARAM, "true");
		System.setProperty(SymbolConstants.EXECUTION_MODE, "hibernate");
		super.startContainer();
	}

	@BeforeMethod
	public void setStartPage() throws Exception {
		startPage = webClient.getPage(BASEURI + "home");
	}

}