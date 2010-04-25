/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.tynamo.test;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.resource.ResourceCollection;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import java.io.IOException;
import java.util.List;

import static com.gargoylesoftware.htmlunit.WebAssert.assertTextNotPresent;
import static com.gargoylesoftware.htmlunit.WebAssert.assertTextPresent;
import static org.testng.Assert.*;

public abstract class AbstractContainerTest
{
	protected static PauseableServer server;

	protected static final int port = 8180;

	protected static final String BASEURI = "http://localhost:" + port + "/";

	protected final WebClient webClient = new WebClient();

	static String errorText = "You must correct the following errors before you may continue";

	@BeforeClass
	public void startContainer() throws Exception
	{
		if (server == null)
		{
			server = new PauseableServer();
			Connector connector = new SelectChannelConnector();
			connector.setPort(port);
			server.setConnectors(new Connector[]{connector});

			HandlerCollection handlers = new HandlerCollection();
			handlers.setHandlers(new Handler[]{buildContext(), new DefaultHandler()});
			server.setHandler(handlers);
			server.start();
			assertTrue(server.isStarted());
		}
	}

	/**
	 * Non-abstract hook method (with a default implementation) to allow subclasses to provide their own WebAppContext instance.
	 * @return a WebAppContext
	 */
	public WebAppContext buildContext()
	{
		WebAppContext context = new WebAppContext("src/main/webapp", "/");
		ResourceCollection resourceCollection = new ResourceCollection(new String[]{"src/main/webapp", "src/test/webapp"});
		context.setBaseResource(resourceCollection);

		/**
		 * like -Dorg.mortbay.jetty.webapp.parentLoaderPriority=true
		 * Sets the classloading model for the context to avoid an strange "ClassNotFoundException: org.slf4j.Logger"
		 */
		context.setParentLoaderPriority(true);
		return context;
	}

	/**
	 * Hook method which is called during setup phase, before the first request.
	 * It allows subclasses to modify the  webClient, such as for disabling javascript
	 */
	@BeforeTest
	public void configureWebClient()
	{
		webClient.setThrowExceptionOnFailingStatusCode(true);
	}

	public void pauseServer(boolean paused)
	{
		if (server != null) server.pause(paused);
	}

	public static class PauseableServer extends Server
	{
		public synchronized void pause(boolean paused)
		{
			try
			{
				if (paused) for (Connector connector : getConnectors())
					connector.stop();
				else for (Connector connector : getConnectors())
					connector.start();
			} catch (Exception e)
			{
			}
		}
	}

	/**
	 * Verifies that the specified xpath is somewhere on the page.
	 *
	 * @param page
	 * @param xpath
	 */
	protected void assertXPathPresent(HtmlPage page, String xpath)
	{
		String message = "XPath not present: " + xpath;
		List list = page.getByXPath(xpath);
		if (list.isEmpty()) fail(message);
		assertNotNull(list.get(0), message);
	}

	/**
	 * Verifies that the specified xpath does NOT appear anywhere on the page.
	 *
	 * @param page
	 * @param xpath
	 */
	protected void assertXPathNotPresent(HtmlPage page, String xpath)
	{
		if (!page.getByXPath(xpath).isEmpty()) fail("XPath IS present: " + xpath);
	}


	protected HtmlPage clickLink(HtmlPage page, String linkText)
	{
		try
		{
			return (HtmlPage) page.getFirstAnchorByText(linkText).click();
		} catch (ElementNotFoundException e)
		{
			fail("Couldn't find a link with text '" + linkText + "' on page " + page);
		} catch (IOException e)
		{
			fail("Clicking on link '" + linkText + "' on page " + page + " failed because of: ", e);
		}
		return null;
	}

	protected HtmlPage clickButton(HtmlPage page, String buttonId) throws IOException
	{
		return page.getElementById(buttonId).click();
	}

	protected HtmlPage clickButton(HtmlForm form, String buttonValue) throws IOException
	{
		try
		{
			return form.<HtmlInput>getInputByValue(buttonValue).click();
		} catch (ElementNotFoundException e)
		{
			try
			{
				return form.getButtonByName(buttonValue).click();
			} catch (ElementNotFoundException e1)
			{
				fail("Couldn't find a button with text/name '" + buttonValue + "' on form '" + form.getNameAttribute() +
						"'");
			}
		}
		return null;
	}

	protected void assertErrorTextPresent(HtmlPage page)
	{
		assertTextPresent(page, errorText);
	}

	protected void assertErrorTextNotPresent(HtmlPage page)
	{
		assertTextNotPresent(page, errorText);
	}

}
