package org.trails.test.functional;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class AssociationTest extends FunctionalTest
{

    public AssociationTest()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public void testAssociationSelect() throws Exception
    {
        HtmlPage listMakesPage = clickLinkOnPage(startPage, "List Makes");
        HtmlPage newMakePage = clickLinkOnPage(listMakesPage, "New Make");     
        getInputByName(newMakePage, "Name").setValueAttribute("Honda");
        listMakesPage = clickButton(newMakePage, "Ok");
        startPage = clickLinkOnPage(listMakesPage, "Home");
        
        HtmlPage listModelsPage = clickLinkOnPage(startPage, "List Models");
        HtmlPage newModelPage = clickLinkOnPage(listModelsPage, "New Model");
        getInputByName(newModelPage, "Name").setValueAttribute("Civic");
        listModelsPage = clickButton(newModelPage, "Ok");
        startPage = clickLinkOnPage(listModelsPage, "Home");
        
        HtmlPage listCarsPage = clickLinkOnPage(startPage, "List Cars");
        HtmlPage newCarPage = clickLinkOnPage(listCarsPage, "New Car");
        assertXPathPresent(newCarPage, 
            "//span[contains(preceding-sibling::label, 'Make')]/select/option[text() = 'Honda']");
        assertXPathPresent(newCarPage, 
            "//span[contains(preceding-sibling::label, 'Model')]/select/option[text() = 'Civic']");
       
    }
}
