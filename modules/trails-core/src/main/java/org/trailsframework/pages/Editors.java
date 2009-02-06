package org.trailsframework.pages;

import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanEditContext;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.chenillekit.tapestry.core.components.DateTimeField;
import org.chenillekit.tapestry.core.components.Editor;
import org.trailsframework.components.EditComposition;
import org.trailsframework.descriptor.CollectionDescriptor;
import org.trailsframework.descriptor.TrailsPropertyDescriptor;
import org.trailsframework.services.DescriptorService;
import org.trailsframework.services.PersistenceService;
import org.trailsframework.util.GenericSelectionModel;


public class Editors
{

	@Environmental
	@Property(write = false)
	private BeanEditContext beanEditContext;

	@Environmental
	@Property(write = false)
	private PropertyEditContext propertyEditContext;

	@Inject
	private PersistenceService persitenceService;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private ValueEncoderSource valueEncoderSource;

	@Component(parameters = {"value=propertyEditContext.propertyValue", "label=prop:propertyEditContext.label",
			"clientId=propertyEditContext.propertyid", "validate=prop:dateFieldValidator"})
	private DateTimeField dateField;

	@Component(parameters = {"value=propertyEditContext.propertyValue", "label=prop:propertyEditContext.label",
			"translate=prop:fckTranslator", "validate=prop:fckValidator",
			"clientId=prop:propertyEditContext.propertyId", "annotationProvider=propertyEditContext"})
	private Editor fckEditor;

	@Component(parameters = {"collection=propertyEditContext.propertyValue", "label=prop:propertyEditContext.label",
			"clientId=prop:propertyEditContext.propertyId", "collectionDescriptor=propertyDescriptor"})//, "owner=beanEditContext.object"})
	private EditComposition editComposition;

	public TrailsPropertyDescriptor getPropertyDescriptor()
	{
		return descriptorService.getClassDescriptor(beanEditContext.getBeanClass()).getPropertyDescriptor(propertyEditContext.getPropertyId());
	}

	public SelectModel getSelectModel()
	{
		TrailsPropertyDescriptor propertyDescriptor = getPropertyDescriptor();

		if (propertyDescriptor.isCollection())
		{
			CollectionDescriptor collectionDescriptor = (CollectionDescriptor) propertyDescriptor;
			return new GenericSelectionModel(persitenceService.getInstances(collectionDescriptor.getElementType()));
		} else
		{
			return new GenericSelectionModel(persitenceService.getInstances(propertyEditContext.getPropertyType()));
		}
	}

	/**
	 * Provide a value encoder for a type.
	 *
	 * @return
	 */
	public ValueEncoder getValueEncoderForProperty()
	{
		TrailsPropertyDescriptor propertyDescriptor = getPropertyDescriptor();
		if (propertyDescriptor.isCollection())
		{
			CollectionDescriptor collectionDescriptor = (CollectionDescriptor) propertyDescriptor;
			return valueEncoderSource.getValueEncoder(collectionDescriptor.getElementType());
		} else
		{
			return valueEncoderSource.getValueEncoder(propertyEditContext.getPropertyType());
		}
	}

	public FieldValidator getDateFieldValidator()
	{
		return propertyEditContext.getValidator(dateField);
	}

	public FieldValidator getFckValidator()
	{
		return propertyEditContext.getValidator(fckEditor);
	}

	public FieldTranslator getFckTranslator()
	{
		return propertyEditContext.getTranslator(fckEditor);
	}


/*	public List<Boolean> buildSelectedList()
	{
		ArrayList<Boolean> selected = new ArrayList<Boolean>();
		if (collection != null)
		{
			selected = new ArrayList<Boolean>(collection.size());
			for (Object o : getCollection())
			{
				selected.add(false);
			}
		}
		return selected;
	}

	public List getSelectedList()
	{
		ArrayList selectedList = new ArrayList();
		selectedList.addAll(getCollection());
		return selectedList;
	}

	public void setSelectedList(List selected)
	{
		if (selected != null)
		{
			getCollection().clear();
			getCollection().addAll(selected);
		}
	}
*/

}
