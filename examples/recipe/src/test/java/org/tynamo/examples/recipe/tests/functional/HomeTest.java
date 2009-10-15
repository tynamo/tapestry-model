package org.tynamo.examples.recipe.tests.functional;

import org.tynamo.test.functional.FunctionalTest;

public class HomeTest extends FunctionalTest
{

	public void testStartPage() throws Exception
	{
		assertXPathPresent(startPage, "//h1[contains(text(),'Welcome to Tynamo')]");
	}

}
