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
package org.tynamo.test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class FunctionalTest extends AbstractContainerTest
{

	protected HtmlPage startPage;

	@BeforeClass
	public void startContainer() throws Exception
	{
		//do nothing! we don't need to start the container here.
		//it will be started externally
	}


	@BeforeTest
	public void configureWebClient()
	{
		Properties testProperties = new Properties();
		try
		{
			testProperties.load(this.getClass().getResourceAsStream("/functionaltest.properties"));
			startPage = (HtmlPage) webClient.getPage(new URL(testProperties.getProperty("test.url")));
		} catch (IOException e)
		{
			Assert.fail(e.getMessage());
		}

		// disabling javascript for all the functional test cases
		webClient.setJavaScriptEnabled(false);
		super.configureWebClient();
	}
}
