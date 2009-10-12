package org.tynamo.examples.recipe;

import org.tynamo.builder.Builder;
import org.tynamo.examples.recipe.model.Recipe;


/**
 * "ConcreteBuilder" (see GOF's <a href="http://en.wikipedia.org/wiki/Builder_pattern">Builder pattern</a>) implements
 * operations of "Builder" to create "Products" in this case Recipes.
 */
public class RecipeBuilder implements Builder<Recipe>
{

	public Recipe build()
	{
		Recipe recipe = new Recipe();
		// modify you recipe object here....
		return recipe;
	}
}
