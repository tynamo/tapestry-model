/*
 * Created on Dec 12, 2004
 *
 * Copyright 2004 Chris Nelson
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.trails.test.functional;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;
import junit.framework.TestCase;
import org.jaxen.JaxenException;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class FunctionalTest extends TestCase
{

	WebClient webClient;
	protected HtmlPage startPage;

	public void setUp() throws Exception
	{
		Properties testProperties = new Properties();
		testProperties.load(this.getClass().getResourceAsStream("/functionaltest.properties"));
		webClient = new WebClient();
		setUpWebClient(webClient);
		startPage = (HtmlPage) webClient.getPage(new URL(testProperties.getProperty("test.url")));
	}

	protected HtmlForm getFirstForm(HtmlPage page)
	{
		return (HtmlForm) page.getForms().get(0);
	}

	protected HtmlPage clickButton(HtmlForm form, String buttonValue) throws IOException
	{
		ClickableElement button = null;
		try
		{
			button = ((HtmlSubmitInput) form.getInputByValue(buttonValue));
		} catch (ElementNotFoundException e)
		{
			button = (HtmlButton) form.getButtonByName(buttonValue);
		}
		return (HtmlPage) button.click();
	}

	protected HtmlPage clickButton(HtmlPage page, String buttonValue) throws IOException
	{
		return clickButton((HtmlForm) page.getForms().get(0), buttonValue);
	}

	protected HtmlPage clickLinkOnPage(HtmlPage page, String linkText) throws IOException
	{
		return (HtmlPage) page.getFirstAnchorByText(linkText).click();
	}

	protected HtmlDivision getErrorDiv(HtmlPage page) throws JaxenException
	{
		return (HtmlDivision) new HtmlUnitXPath("//div[@class='error']").selectSingleNode(page);
	}

	protected String getId(String idField, HtmlPage savedCategoryPage) throws JaxenException
	{
		HtmlListItem span = (HtmlListItem) new HtmlUnitXPath("//li[contains(., '" + idField + "')]").selectSingleNode(savedCategoryPage);
		return span.asText().replaceAll(idField, "").trim();
	}

	protected void assertXPathPresent(HtmlPage page, String xpath) throws Exception
	{
		assertNotNull(new HtmlUnitXPath(xpath).selectSingleNode(page));

	}

	protected void assertXPathNotPresent(HtmlPage page, String xpath) throws Exception
	{
		assertNull(new HtmlUnitXPath(xpath).selectSingleNode(page));
	}

	protected HtmlTextArea getTextAreaByName(HtmlPage page, String name) throws JaxenException
	{
		HtmlTextArea textArea = (HtmlTextArea)
				new HtmlUnitXPath("//textarea/preceding-sibling::label[contains(text(), '" + name + "')]/following-sibling::textarea").selectSingleNode(page);
		return textArea;
	}

	protected HtmlInput getInputByName(HtmlPage page, String name) throws JaxenException
	{
		return (HtmlInput)
				new HtmlUnitXPath("//input/preceding-sibling::label[contains(text(), '" + name + "')]/following-sibling::input").selectSingleNode(page);
	}

	protected HtmlSelect getSelectByName(HtmlPage page, String name) throws JaxenException
	{
		return (HtmlSelect)
				new HtmlUnitXPath("//select/preceding-sibling::label[contains(text(),  '" + name + "')]/following-sibling::select").selectSingleNode(page);
	}

	/**
	 * Hook method which is called during setup phase, before the first request.
	 * It allows subclasses to modify the webClient, such as for disabling javascript
	 *
	 * @param webClient
	 */
	protected void setUpWebClient(WebClient webClient)
	{
		/**
		 * momentarily disabling javascript for all the functional test cases
		 * until we add the dojo toolkit
		 */
		webClient.setJavaScriptEnabled(false);
	}
}
