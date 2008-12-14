package org.trailsframework.examples.recipe.pages.edit;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.trailsframework.examples.recipe.model.Recipe;
import org.trailsframework.services.PersistenceService;


public class RecipeEdit
{

	@Inject
	private PersistenceService persitenceService;

	@Property
	private Recipe bean;

	void onActivate(Recipe recipe) throws Exception
	{
		this.bean = recipe;
	}

	Object[] onPassivate()
	{
		return new Object[]{bean};
	}

	void onSuccess()
	{
		persitenceService.save(bean);
	}

	void cleanupRender()
	{
		bean = null;
	}

}
