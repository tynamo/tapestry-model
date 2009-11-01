package org.tynamo.pages;

import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanEditContext;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.chenillekit.tapestry.core.components.DateTimeField;
import org.chenillekit.tapestry.core.components.Editor;
import org.tynamo.components.EditComposition;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.IdentifierDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;
import org.tynamo.services.TynamoBeanContext;
import org.tynamo.util.GenericSelectionModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Editors
{

	@Environmental
	@Property(write = false)
	private BeanEditContext beanEditContext;

	@Environmental
	@Property(write = false)
	private PropertyEditContext propertyEditContext;

	@Environmental
	@Property(write = false)
	private TynamoBeanContext tynamoBeanContext;

	@Inject
	private PersistenceService persitenceService;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private ValueEncoderSource valueEncoderSource;

	@Inject
	private ComponentResources resources;

	@Inject
	private BeanModelSource beanModelSource;

	@Component(parameters = {"value=propertyEditContext.propertyValue", "label=prop:propertyEditContext.label",
			"translate=prop:textFieldTranslator", "validate=prop:textFieldValidator",
			"clientId=prop:propertyEditContext.propertyId", "annotationProvider=propertyEditContext"})
	private TextField textField;

	@Component(parameters = {"value=propertyEditContext.propertyValue", "label=prop:propertyEditContext.label",
			"clientId=propertyEditContext.propertyid", "validate=prop:dateFieldValidator"})
	private DateTimeField dateField;

	@Component(parameters = {"value=propertyEditContext.propertyValue", "label=prop:propertyEditContext.label",
			"translate=prop:fckTranslator", "validate=prop:fckValidator",
			"clientId=prop:propertyEditContext.propertyId", "annotationProvider=propertyEditContext"})
	private Editor fckEditor;

	@Component(parameters = {"collection=propertyEditContext.propertyValue", "label=prop:propertyEditContext.label",
			"clientId=prop:propertyEditContext.propertyId", "collectionDescriptor=propertyDescriptor",
			"owner=tynamoBeanContext.bean"})
	private EditComposition editComposition;

	public TynamoPropertyDescriptor getPropertyDescriptor()
	{
		return descriptorService.getClassDescriptor(beanEditContext.getBeanClass()).getPropertyDescriptor(propertyEditContext.getPropertyId());
	}

	public SelectModel getSelectModel()
	{
		TynamoPropertyDescriptor propertyDescriptor = getPropertyDescriptor();

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
		TynamoPropertyDescriptor propertyDescriptor = getPropertyDescriptor();
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

	public FieldTranslator getTextFieldTranslator()
	{
		return propertyEditContext.getTranslator(textField);
	}

	public FieldValidator getTextFieldValidator()
	{
		return propertyEditContext.getValidator(textField);
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
*/

	public boolean isPropertyValueInstanceOfList()
	{
		return propertyEditContext.getPropertyValue() instanceof List;
	}

	/**
	 *
	 * Palette's parameter "selected" only accepts java.util.List
	 * If the collection is a java.util.Set it needs to get converted
	 *
	 */
	public List getSelected()
	{
		ArrayList selectedList = new ArrayList();
		selectedList.addAll((Collection) propertyEditContext.getPropertyValue());
		return selectedList;
	}

	public void setSelected(List selected)
	{
		Collection collection = (Collection) propertyEditContext.getPropertyValue();
		if (selected != null)
		{
			collection.clear();
			collection.addAll(selected);
		}
	}

	public boolean isNotEditable()
	{
		IdentifierDescriptor descriptor = (IdentifierDescriptor) getPropertyDescriptor();
		return descriptor.isGenerated() || propertyEditContext.getPropertyValue() != null;
	}

	public BeanModel getEmbeddedModel()
	{
		return beanModelSource.createEditModel(propertyEditContext.getPropertyType(),
				resources.getContainerMessages() != null ? resources.getContainerMessages() : resources.getMessages());
	}

}
