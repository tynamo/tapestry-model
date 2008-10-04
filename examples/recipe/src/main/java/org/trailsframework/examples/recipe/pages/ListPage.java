package org.trailsframework.examples.recipe.pages;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.trailsframework.examples.recipe.model.Category;
import org.trailsframework.examples.recipe.model.Recipe;
import org.trailsframework.services.PersistenceService;
import org.trailsframework.util.Identifiable;

import java.util.List;

public class ListPage
{

	@Inject
	private PersistenceService persitenceService;

	@Property
	private Identifiable model;

	@Property
	private Class clazz;

	void onActivate(Class clazz) throws Exception
	{
		this.clazz = clazz;

		if (persitenceService.getInstances(Category.class).isEmpty())
		{
			Category category1 = new Category();
			category1.setName("acategoria uno");
			persitenceService.save(category1);

			Recipe recipe1 = new Recipe();
			recipe1.setTitle("nos eque");
			recipe1.setCategory(category1);

			persitenceService.save(recipe1);
		}
	}

	public List getInstances()
	{
		return persitenceService.getInstances(clazz);
	}

	public Object[] getEditPageContext()
	{
		return new Object[]{clazz, model};
	}

}
