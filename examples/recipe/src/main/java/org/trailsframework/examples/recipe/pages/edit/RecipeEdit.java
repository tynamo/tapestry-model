package org.trailsframework.examples.recipe.pages.edit;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.trailsframework.descriptor.CollectionDescriptor;
import org.trailsframework.examples.recipe.model.Ingredient;
import org.trailsframework.examples.recipe.model.Recipe;
import org.trailsframework.services.DescriptorService;
import org.trailsframework.services.PersistenceService;


public class RecipeEdit
{

	@Inject
	private PersistenceService persitenceService;

	@Inject
	private DescriptorService descriptorService;

	@Property
	@PageActivationContext
	private Recipe recipe;
	

	@Component(parameters = "object=recipe")
	private BeanEditForm form;

	@Inject
	private Messages messages;

// we shouldn't need this if the onActivate parameter is an Hibernate entity annotated with @PageActivationContext, but...
/*
	void onActivate(Recipe recipe) throws Exception
	{
		this.recipe = recipe;
	}

	Object[] onPassivate()
	{
		return new Object[]{recipe};
	}
*/

	void onValidateForm()
	{
		// manual validation example
		if (recipe.getIngredients() == null || recipe.getIngredients().size() < 1)
		{
			form.recordError(messages.get("not-enough-ingredients"));
		}
	}

	void onSuccess()
	{
		persitenceService.save(recipe);
	}

	void cleanupRender()
	{
		recipe = null;
	}

	public CollectionDescriptor getCollectionDescriptor()
	{
		return (CollectionDescriptor) descriptorService.getClassDescriptor(Recipe.class)
				.getPropertyDescriptor("ingredients");
	}

}
