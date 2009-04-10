package org.trailsframework.examples.recipe.tests.functional;

import org.trails.test.functional.FunctionalTest;

public class HomeTest extends FunctionalTest
{

	public void testStartPage() throws Exception
	{
		assertXPathPresent(startPage, "//h1[contains(text(),'Welcome to Trails')]");
	}

}
