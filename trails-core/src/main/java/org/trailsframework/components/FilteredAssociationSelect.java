package org.trails.component;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Parameter;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.trails.descriptor.annotation.InitialValueDescriptorExtension;
import org.trails.descriptor.annotation.PossibleValuesDescriptorExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** imports for javadocs **/
import org.trails.descriptor.annotation.InitialValue;
import org.trails.descriptor.annotation.PossibleValues;
import ognl.OgnlException;

/**
 * Component that renders a select element based on the annotations
 * {@link InitialValue} and {@link PossibleValues}.
 * <p/>
 * If the property has a {@link PossibleValues} annotation, the select will
 * render its options evaluating the expression passed as the value of the
 * annotation.<br>
 * <p/>
 * If the property has a {@link InitialValue} it will render all
 * instances of that property type and resubmit the form every time the value
 * changes, so the related select values can be filtered.<br>
 *
 * @author pruggia
 */
public abstract class FilteredAssociationSelect extends AssociationSelect
{

	@Parameter(required = true)
	public abstract Object getPageModel();

	/**
	 * Sets the initial value for the property based on the expression declared
	 * in the {@link InitialValue} annotation.
	 *
	 * @param cycle the request cycle, it's never null.
	 */
	@Override
	protected void prepareForRender(final IRequestCycle cycle)
	{
		InitialValueDescriptorExtension extension = (InitialValueDescriptorExtension) getPropertyDescriptor()
				.getExtension(InitialValueDescriptorExtension.class.getName());
		if (extension != null)
		{
			if (getValue() == null)
			{
				try
				{
					setValue(extension.evaluateExpresion(getPageModel()));
				} catch (OgnlException e)
				{
					// do nothing, don't worry about it
				}
			}
		}
		super.prepareForRender(cycle);
	}

	/**
	 * Creates an {@link IPropertySelectionModel} based on properties annotated
	 * with {@link PossibleValues} and {@link InitialValue}.
	 *
	 * @return Returns the selection model, it never returns null.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public IPropertySelectionModel buildSelectionModel()
	{
		PossibleValuesDescriptorExtension extension = (PossibleValuesDescriptorExtension) getPropertyDescriptor()
				.getExtension(PossibleValuesDescriptorExtension.class.getName());

		/* instances passed directly from a collection to the component, no need to build a list. */
		if (extension != null && getInstances() == null)
		{
			// If it's filtered by a "PossibleValues" annotation, we need to
			// retrieve the list evaluating the ognl expression of the annotation.
			Collection<Object> col = null;
			List allObjects = null;
			try
			{
				col = (Collection) extension.evaluateExpresion(getPageModel());
			} catch (OgnlException e)
			{
				col = new ArrayList();
			}
			if (col instanceof List)
			{
				allObjects = (List) col;
			} else
			{
				allObjects = new ArrayList();
				allObjects.addAll(col);
			}

			if (!allObjects.isEmpty())
			{

				IdentifierSelectionModel selectionModel = new IdentifierSelectionModel(allObjects,
						getClassDescriptor().getIdentifierDescriptor().getName(), isAllowNone());
				selectionModel.setNoneLabel(getNoneLabel());
				return selectionModel;
			}
		}

		return super.buildSelectionModel();
	}

	/**
	 * If the property is annotated with {@link InitialValue}, this
	 * method generates javascript code to submit the form and update the options
	 * for the filtered property.
	 *
	 * @return a fragment of javascript. It never returns null.
	 */
	public String getOnChangeJavascript()
	{
		InitialValueDescriptorExtension extension = (InitialValueDescriptorExtension) getPropertyDescriptor()
				.getExtension(InitialValueDescriptorExtension.class.getName());
		if (extension != null)
		{
			return "tapestry.form.refresh('form')";
			/*
				  return "tapestry.form.registerForm('form');tapestry.form.refresh('form')";
				  */
		} else
		{
			return "";
		}
	}
}
