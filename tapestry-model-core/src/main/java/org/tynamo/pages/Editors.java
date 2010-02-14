package org.tynamo.pages;

import org.apache.commons.collections.CollectionUtils;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanEditContext;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.apache.tapestry5.util.EnumSelectModel;
import org.chenillekit.tapestry.core.components.DateTimeField;
import org.chenillekit.tapestry.core.components.Editor;
import org.tynamo.components.*;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.IdentifierDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;
import org.tynamo.services.TynamoBeanContext;
import org.tynamo.util.GenericSelectionModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
	private PersistenceService persistenceService;

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

	@Component(parameters = {"model=tynamoBeanContext.bean", "propertyDescriptor=propertyDescriptor"})
	private org.tynamo.components.Blob blob;

	public TynamoPropertyDescriptor getPropertyDescriptor()
	{
		return descriptorService.getClassDescriptor(beanEditContext.getBeanClass()).getPropertyDescriptor(propertyEditContext.getPropertyId());
	}

	@SuppressWarnings({ "unchecked" })
	public SelectModel getSelectModel()
	{
		TynamoPropertyDescriptor propertyDescriptor = getPropertyDescriptor();

		Class type = propertyDescriptor.isCollection() ? ((CollectionDescriptor) propertyDescriptor).getElementType() : propertyDescriptor.getPropertyType();
		if (type.isEnum()) return new EnumSelectModel(type, resources.getMessages());

		if (propertyDescriptor.isCollection() && ((CollectionDescriptor) propertyDescriptor).isOneToMany()) {
			return new GenericSelectionModel(persistenceService.getOrphanInstances((CollectionDescriptor) propertyDescriptor, tynamoBeanContext.getBean()));
		}

		return new GenericSelectionModel(persistenceService.getInstances(type));
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
	 * Palette's parameter "selected" only accepts java.util.List
	 * If the collection is a java.util.Set it needs to get converted
	 */
	public List getSelected()
	{
		if (isPropertyValueInstanceOfList())
		{
			return (List) propertyEditContext.getPropertyValue();
		} else
		{
			ArrayList selectedList = new ArrayList();
			selectedList.addAll((Collection) propertyEditContext.getPropertyValue());
			return selectedList;
		}
	}

	public void setSelected(List selected)
	{
		Collection collection = (Collection) propertyEditContext.getPropertyValue();
		CollectionDescriptor descriptor = (CollectionDescriptor) getPropertyDescriptor();

		for (Object o : CollectionUtils.subtract(selected, collection))
		{
			persistenceService.addToCollection(descriptor, o, tynamoBeanContext.getBean());
		}

		for (Object o : CollectionUtils.subtract(collection, selected))
		{
			persistenceService.removeFromCollection(descriptor, o, tynamoBeanContext.getBean());
		}

	}

	public boolean isNotEditable()
	{
		IdentifierDescriptor descriptor = (IdentifierDescriptor) getPropertyDescriptor();
		return descriptor.isGenerated() || propertyEditContext.getPropertyValue() != null;
	}

	public BeanModel getEmbeddedModel()
	{
		return beanModelSource.createEditModel(propertyEditContext.getPropertyType(), getMessages());
	}

	private Messages getMessages()
	{
		return resources.getContainerMessages() != null ? resources.getContainerMessages() : resources.getMessages();
	}

	/**
	 * Looks for a help message within the messages based on the id.
	 */
	public String getHelpMessage()
	{
		Messages messages = getMessages();
		String key = propertyEditContext.getPropertyId() + "-help";
		if (messages.contains(key)) return messages.get(key);
		return null;
	}

}
