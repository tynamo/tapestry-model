/* vim: set ts=2 et sw=2 cindent fo=qroca: */

package org.trails.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.InjectObject;
import org.apache.tapestry.form.IPropertySelectionModel;

import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.persistence.PersistenceService;

import org.trails.descriptor.annotation.PossibleValuesDescriptorExtension;
import org.trails.descriptor.annotation.InitialValueDescriptorExtension;

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
public abstract class FilteredAssociationSelect extends AbstractPropertySelection
{

	/**
	 * The persistence service.
	 *
	 * @return Returns the persistence service.
	 */
	@InjectObject("spring:persistenceService")
	public abstract PersistenceService getPersistenceService();

	/**
	 * The descriptor service.
	 *
	 * @return Returs the descriptor service.
	 */
	@InjectObject("spring:descriptorService")
	public abstract DescriptorService getDescriptorService();

	/**
	 * Convenience method to retrieve the class descriptor for the current
	 * property.
	 *
	 * @return a {@link IClassDescriptor} object, never null.
	 */
	public IClassDescriptor getClassDescriptor()
	{
		return getDescriptorService().getClassDescriptor(getPropertyDescriptor().getPropertyType());
	}

	/**
	 * Sets the initial value for the property based on the expression declared
	 * in the {@link InitialValue} annotation.
	 *
	 * @param cycle the request cycle, it's never null.
	 */
	protected void prepareForRender(final IRequestCycle cycle)
	{
		InitialValueDescriptorExtension extension = (InitialValueDescriptorExtension) getPropertyDescriptor()
				.getExtension(InitialValueDescriptorExtension.class.getName());
		if (extension != null)
		{
			Object value = null;
			try
			{
				value = Ognl.getValue(getPropertyDescriptor().getName(), getPageModel());
			} catch (OgnlException e)
			{
				throw new RuntimeException("Error obtaining object property: " + getPropertyDescriptor().getName(), e);
			}
			if (value == null)
			{
				IPropertySelectionModel propertySelectionModel = buildSelectionModel();
				extension.initValue(getPageModel(), getPropertyDescriptor().getName(), propertySelectionModel);
			}
		}
	}

	/**
	 * Creates an {@link IPropertySelectionModel} based on properties annotated
	 * with {@link PossibleValues} and {@link InitialValue}.
	 *
	 * @return Returns the selection model, it never returns null.
	 */
	@SuppressWarnings("unchecked")
	public IPropertySelectionModel buildSelectionModel()
	{
		IdentifierSelectionModel selectionModel;
		if (getInstances() != null)
		{
			// instances passed directly from a collection to the component, no need
			// to build a list.
			selectionModel = new IdentifierSelectionModel(getInstances(),
					getClassDescriptor().getIdentifierDescriptor().getName(), isAllowNone());
		} else
		{
			String idProperty;
			idProperty = getClassDescriptor().getIdentifierDescriptor().getName();
			PossibleValuesDescriptorExtension extension = (PossibleValuesDescriptorExtension) getPropertyDescriptor()
					.getExtension(PossibleValuesDescriptorExtension.class.getName());

			List allObjects = null;

			if (extension != null)
			{
				// If it's filtered by a "PossibleValues" annotation, we need to
				// retrieve the list evaluating the ognl expression of the annotation.
				Collection<Object> col = null;
				try
				{
					col = (Collection) Ognl.getValue(extension.getExpression(), getPageModel());
				} catch (Exception e)
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
			} else
			{
				allObjects = getPersistenceService().getAllInstances(getClassDescriptor().getType());
			}

			if (allObjects.isEmpty())
			{
				selectionModel = new IdentifierSelectionModel(allObjects, idProperty, true);
				selectionModel.setNoneLabel(getNoneLabel());
			} else
			{
				selectionModel = new IdentifierSelectionModel(allObjects, idProperty, false);
			}
		}
		selectionModel.setNoneLabel(getNoneLabel());
		return selectionModel;
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

	/**
	 * Convenience method to retrieve the page model.
	 *
	 * @return an Object, never null.
	 */
	private Object getPageModel()
	{
		Object model;
		try
		{
			model = Ognl.getValue("model", getPage());
		} catch (OgnlException e)
		{
			throw new RuntimeException("Error obtaining model from page.", e);
		}
		return model;
	}
}
