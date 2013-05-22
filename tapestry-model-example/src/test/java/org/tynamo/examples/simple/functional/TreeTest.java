package org.tynamo.examples.simple.functional;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.tynamo.test.AbstractContainerTest;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import static org.testng.Assert.*;

public class TreeTest extends AbstractContainerTest
{
	private HtmlPage startPage;

	@BeforeMethod
	public void setStartPage() throws Exception {
		startPage = webClient.getPage(BASEURI);
	}

	@Test
	public void testTree() throws Exception
	{
		HtmlPage newTreeNodePage = webClient.getPage(BASEURI + "add/treenode"); 
		HtmlForm form = newTreeNodePage.getHtmlElementById("form");
		form.<HtmlInput>getInputByName("name").setValueAttribute("one");
		// FIXME apply button doesn't work
//		newTreeNodePage = clickButton(newTreeNodePage, "save");
//		assertNotNull(newTreeNodePage.getElementById("Identifier") );
		newTreeNodePage = webClient.getPage(BASEURI + "add/treenode");
		form = newTreeNodePage.getHtmlElementById("form");
		form.<HtmlInput>getInputByName("name").setValueAttribute("two");
		clickButton(newTreeNodePage, "saveAndReturn");
		HtmlPage editTreeNodePage = webClient.getPage(BASEURI + "edit/treenode/1"); 
		form = editTreeNodePage.getHtmlElementById("form");
		form.getSelectByName("parent").setSelectedAttribute("1", true);
		// FIXME Save button doesn't work yet
//		editTreeNodePage = clickButton(editTreeNodePage, "save");
//		HtmlOption option = newTreeNodePage.getHtmlElementById("form").getSelectByName("Parent").getOptionByValue("2");
//		assertTrue(option.isSelected(), "2 is selected");
//
//		// now delete one
//		listTreeNodesPage = clickButton(editTreeNodePage, "Delete");
//		assertNull(new HtmlUnitXPath("//td[text() = 'one']").selectSingleNode(listTreeNodesPage));
	}
}
