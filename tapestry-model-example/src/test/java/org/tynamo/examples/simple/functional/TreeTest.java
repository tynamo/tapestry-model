package org.tynamo.examples.simple.functional;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.testng.annotations.Test;
import org.tynamo.examples.simple.integration.BaseIntegrationTest;

public class TreeTest extends BaseIntegrationTest
{

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
