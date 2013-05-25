/*
 * Created on Dec 9, 2004
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
package org.tynamo.examples.simple.functional;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.testng.annotations.Test;
import org.tynamo.examples.simple.integration.BaseIntegrationTest;

import java.io.IOException;

import static com.gargoylesoftware.htmlunit.WebAssert.assertElementPresent;
import static com.gargoylesoftware.htmlunit.WebAssert.assertTextPresent;
import static org.testng.Assert.assertNotNull;

public class EditCategoryTest extends BaseIntegrationTest
{

//	@Test I don't understand what this test is trying to test
	public void testRequiredValidation() throws Exception
	{
		HtmlPage newCategoryPage;
		HtmlForm newCategoryForm = goToNewCategoryForm();
		HtmlSubmitInput saveButton = (HtmlSubmitInput) newCategoryForm.getInputByValue("saveAndReturn");
		newCategoryPage = (HtmlPage) saveButton.click();
		assertErrorTextPresent(newCategoryPage);
		newCategoryForm = newCategoryPage.getHtmlElementById("form");
		HtmlTextArea textArea = newCategoryForm.getTextAreaByName("Description");
		textArea.setText("a description");
		newCategoryPage = clickButton(newCategoryForm, "saveAndStay");
		assertErrorTextNotPresent(newCategoryPage);
		assertElementPresent(newCategoryPage, "Id");
	}

	@Test
	public void testRegexValidation() throws Exception
	{
		HtmlPage catalogListPage = (HtmlPage) startPage.getAnchorByText("List Catalogs").click();
		HtmlPage newCatalogPage = (HtmlPage) catalogListPage.getAnchorByText("New Catalog").click();

		HtmlForm form = newCatalogPage.getHtmlElementById("form");
		HtmlInput nameInput = form.<HtmlInput>getInputByName("name");
		nameInput.setValueAttribute("new catalog");

		newCatalogPage = clickButton(newCatalogPage, "saveAndStay");
		assertErrorTextPresent(newCatalogPage);
		assertTextPresent(newCatalogPage, "NameLabel must match \"[a-z]*\"");

		form = newCatalogPage.getHtmlElementById("form");
		nameInput = form.<HtmlInput>getInputByName("name");
		nameInput.setValueAttribute("newspacecatalog");

		newCatalogPage = clickButton(newCatalogPage, "saveAndStay");
		assertErrorTextNotPresent(newCatalogPage);
	}

	private HtmlForm goToNewCategoryForm() throws Exception
	{
		HtmlPage newCategoryPage = goToNewCategoryPage();
		return newCategoryPage.getForms().get(0);
	}

	private HtmlPage goToNewCategoryPage() throws IOException
	{
		HtmlPage catalogListPage = (HtmlPage) startPage.getAnchorByText("List Catalogs").click();
		HtmlPage newCatalogPage = (HtmlPage) catalogListPage.getAnchorByText("New Catalog").click();
		HtmlForm form = newCatalogPage.getHtmlElementById("form");
		form.<HtmlInput>getInputByName("name").setValueAttribute("newcatalog");
		newCatalogPage = clickButton(newCatalogPage, "saveAndReturn");

		return clickLink(newCatalogPage,"Add Category");
	}

	@Test
	public void testOverrideOnAddToCollectionPage() throws Exception
	{
		assertXPathPresent(goToNewCategoryPage(), "//label[text() = 'The Description']");
	}

//	@Test //not supported, the palette component doesn't have a "Add New..." link anymore.
	public void testAddNewDisabled() throws Exception
	{
		HtmlPage listCatalogsPage = clickLink(startPage, "List Catalogs");
		HtmlPage newCatalogPage = clickLink(listCatalogsPage, "New Catalog");
//		HtmlSubmitInput addButton = (HtmlSubmitInput) new HtmlUnitXPath("//input[@type='submit' and @value='Add New...']").selectSingleNode(newCatalogPage);
		HtmlAnchor addLink = null;
		try
		{
			addLink = newCatalogPage.getAnchorByText("Add New...");
		} catch (ElementNotFoundException e)
		{
			assertNotNull(e);  // assertTrue(addButton.isDisabled());
		}
		newCatalogPage.<HtmlForm>getHtmlElementById("form").getInputByName("name").setValueAttribute("newercatalog");
		newCatalogPage = clickButton(newCatalogPage, "saveAndStay");
//		addButton = (HtmlSubmitInput) new HtmlUnitXPath("//input[@type='submit' and @value='Add New...']").selectSingleNode(newCatalogPage);
		addLink = newCatalogPage.getAnchorByText("Add New...");
		assertNotNull(addLink); // assertFalse(addButton.isDisabled());
	}

//	@Test @ascandroli: this would be super nice, but right now is too much ot handle
	public void testAddProductToCategory() throws Exception
	{
		webClient.setJavaScriptEnabled(false);
		HtmlForm newCategoryForm = goToNewCategoryForm();
        HtmlTextArea textArea = newCategoryForm.getTextAreaByName("Description");
		textArea.setText("howdya doo");
		HtmlPage categoryPage = clickButton(newCategoryForm, "Apply");
		HtmlPage newProductPage = clickLink(categoryPage, "Add New...");
		HtmlTextInput input = newProductPage.<HtmlForm>getHtmlElementById("form").getInputByName("name");
		input.setValueAttribute("a new product");

		categoryPage = clickButton(newProductPage, "Ok");
		assertXPathPresent(categoryPage, "//td[@class='selected-cell']/select/option['a new product']");
		HtmlPage catalogPage = clickButton(categoryPage, "Ok");
		assertXPathPresent(catalogPage, "//td/a['howdya doo']");
		HtmlPage listPage = clickButton(catalogPage, "Ok");
		assertXPathPresent(listPage, "//td/a['newercatalog']");
	}
}
