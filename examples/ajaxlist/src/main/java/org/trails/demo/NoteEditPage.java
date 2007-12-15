package org.trails.demo;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.EventListener;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.services.ResponseBuilder;
import org.trails.component.IdentifierSelectionModel;
import org.trails.page.HibernateEditPage;
import org.trails.finder.BlockFinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public abstract class NoteEditPage extends HibernateEditPage
{

	@InjectObject("service:trails.core.EditorService")
	public abstract BlockFinder getBlockFinder();

	@Persist
	public abstract Object getPepe();
	public abstract void setPepe(Object o);

	@EventListener(targets = "categoryPropertySelection", events = "onchange", submitForm = "form")
	public void changePoses(IRequestCycle cycle)
	{
		ResponseBuilder responseBuilder = cycle.getResponseBuilder();
		responseBuilder.updateComponent("subcategoryPropertySelection");
		responseBuilder.updateComponent("subcategory");
	}

	public IPropertySelectionModel buildSubCategoryPropertySelectionModel()
	{
		if (getModel() != null && ((Note) getModel()).getCategory() != null)
		{
			Collection<SubCategory> subCategories = ((Note) getModel()).getCategory().getSubcategories();
			return new IdentifierSelectionModel(new ArrayList<SubCategory>(subCategories), "id", true);
		} else
		{
			return new IdentifierSelectionModel(Collections.EMPTY_LIST, "id", true);
		}
	}

}