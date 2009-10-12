package org.tynamo.test.functional;

import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;

public class TreeTest extends FunctionalTest
{
	public TreeTest()
	{
		super();
	}

	public void testTree() throws Exception
	{
		HtmlPage listTreeNodesPage = clickLinkOnPage(startPage, "List Tree Nodes");
		HtmlPage newTreeNodePage = clickLinkOnPage(listTreeNodesPage, "New Tree Node");
		HtmlTextInput input = (HtmlTextInput) getInputByName(newTreeNodePage, "Name");
		input.setValueAttribute("one");
		newTreeNodePage = clickButton(newTreeNodePage, "Apply");
		assertNotNull(getId("Id", newTreeNodePage));
		listTreeNodesPage = clickButton(newTreeNodePage, "Ok");
		newTreeNodePage = clickLinkOnPage(listTreeNodesPage, "New Tree Node");
		input = (HtmlTextInput) getInputByName(newTreeNodePage, "Name");
		input.setValueAttribute("two");
		listTreeNodesPage = clickButton(newTreeNodePage, "Ok");
		HtmlPage editTreeNodePage = clickLinkOnPage(listTreeNodesPage, "1");
		HtmlSelect select = getSelectByName(editTreeNodePage, "Parent");
		select.setSelectedAttribute("2", true);
		editTreeNodePage = clickButton(editTreeNodePage, "Apply");
		select = getSelectByName(editTreeNodePage, "Parent");
		HtmlOption option = select.getOptionByValue("2");
		assertTrue("2 is selected", option.isSelected());

		// now delete one
		listTreeNodesPage = clickButton(editTreeNodePage, "Delete");
		assertNull(new HtmlUnitXPath("//td[text() = 'one']").selectSingleNode(listTreeNodesPage));
	}
}
