package org.tynamo.pages;

import org.apache.commons.collections.CollectionUtils;
import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.data.BlankOption;
import org.apache.tapestry5.internal.BeanValidationContext;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanEditContext;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.apache.tapestry5.util.EnumSelectModel;
import org.tynamo.components.EditComposition;
import org.tynamo.descriptor.CollectionDescriptor;
import org.tynamo.descriptor.IdentifierDescriptor;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PersistenceService;
import org.tynamo.util.GenericSelectionModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * A page that exists to contain blocks used to edit different types of properties. The blocks on this page are
 * contributed into the {@link org.apache.tapestry5.services.BeanBlockSource} service configuration.
 *
 * @see org.apache.tapestry5.services.BeanBlockContribution
 * @see org.apache.tapestry5.corelib.components.BeanEditForm
 */
public class PropertyEditBlocks
{

	@Environmental
	@Property(write = false)
	private BeanEditContext beanEditContext;

	@Environmental
	@Property(write = false)
	private PropertyEditContext propertyEditContext;

	@Environmental
	@Property(write = false)
	private BeanValidationContext beanValidationContext;

	@Inject
	private PersistenceService persistenceService;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private ValueEncoderSource valueEncoderSource;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private Locale locale;

	@Component(parameters = {"value=propertyEditContext.propertyValue", "label=prop:propertyEditContext.label",
			"translate=prop:textFieldTranslator", "validate=prop:textFieldValidator",
			"clientId=prop:propertyEditContext.propertyId", "annotationProvider=propertyEditContext"})
	private TextField textField;

	@Component(
			parameters = {"value=propertyEditContext.propertyValue", "label=prop:propertyEditContext.label",
					"translate=prop:textAreaTranslator", "validate=prop:textAreaValidator",
					"clientId=prop:propertyEditContext.propertyId",
					"annotationProvider=propertyEditContext"})
	private TextArea textArea;

	@Component(parameters = {"collection=propertyEditContext.propertyValue", "label=prop:propertyEditContext.label",
			"clientId=prop:propertyEditContext.propertyId", "collectionDescriptor=propertyDescriptor",
			"owner=beanValidationContext.beanInstance"})
	private EditComposition editComposition;

	@Component(parameters = {"model=beanValidationContext.beanInstance", "propertyDescriptor=propertyDescriptor"})
	private org.tynamo.components.Blob blob;

	@Component(
			parameters = {"value=propertyEditContext.propertyValue", "label=prop:propertyEditContext.label",
					"encoder=valueEncoderForProperty", "model=selectModelForProperty", "validate=prop:selectValidator",
					"clientId=prop:propertyEditContext.propertyId", "blankOption=prop:blankOption"})
	private Select select;

	public TynamoPropertyDescriptor getPropertyDescriptor()
	{
		return descriptorService.getClassDescriptor(beanEditContext.getBeanClass()).getPropertyDescriptor(propertyEditContext.getPropertyId());
	}

	@SuppressWarnings({"unchecked"})
	public SelectModel getSelectModelForProperty()
	{
		TynamoPropertyDescriptor propertyDescriptor = getPropertyDescriptor();

		Class type = propertyDescriptor != null && propertyDescriptor.isCollection() ?
				((CollectionDescriptor) propertyDescriptor).getElementType() : propertyEditContext.getPropertyType();

		if (type.isEnum()) return new EnumSelectModel(type, getMessages());

		if (propertyDescriptor != null && propertyDescriptor.isCollection() && ((CollectionDescriptor) propertyDescriptor).isOneToMany())
		{
			return new GenericSelectionModel(persistenceService.getOrphanInstances((CollectionDescriptor) propertyDescriptor, beanValidationContext.getBeanInstance()));
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
		if (propertyDescriptor != null && propertyDescriptor.isCollection())
		{
			CollectionDescriptor collectionDescriptor = (CollectionDescriptor) propertyDescriptor;
			return valueEncoderSource.getValueEncoder(collectionDescriptor.getElementType());
		}

		return valueEncoderSource.getValueEncoder(propertyEditContext.getPropertyType());
	}

	public FieldTranslator getTextFieldTranslator()
	{
		return propertyEditContext.getTranslator(textField);
	}

	public FieldValidator getTextFieldValidator()
	{
		return propertyEditContext.getValidator(textField);
	}

	public FieldTranslator getTextAreaTranslator()
	{
		return propertyEditContext.getTranslator(textArea);
	}

	public FieldValidator getTextAreaValidator()
	{
		return propertyEditContext.getValidator(textArea);
	}

	public FieldValidator getSelectValidator()
	{
		return propertyEditContext.getValidator(select);
	}

	public boolean isPropertyValueInstanceOfList()
	{
		CollectionDescriptor descriptor = (CollectionDescriptor) getPropertyDescriptor();
		return descriptor.getPropertyType().isAssignableFrom(List.class);
	}

	/**
	 * Palette's parameter "selected" only accepts java.util.List If the collection is a java.util.Set it needs to get
	 * converted
	 */
	public List getSelected()
	{
		Object value = propertyEditContext.getPropertyValue();
		if (isPropertyValueInstanceOfList())
		{
			return value != null ? (List) value : new ArrayList();
		} else
		{
			ArrayList selectedList = new ArrayList();
			if (value != null) selectedList.addAll((Collection) value);
			return selectedList;
		}
	}

	public void setSelected(List selected)
	{
		Collection collection = (Collection) propertyEditContext.getPropertyValue();
		if (collection != null) {
			CollectionDescriptor descriptor = (CollectionDescriptor) getPropertyDescriptor();

			for (Object o : CollectionUtils.subtract(selected, collection))
			{
				persistenceService.addToCollection(descriptor, o, beanValidationContext.getBeanInstance());
			}

			for (Object o : CollectionUtils.subtract(collection, selected))
			{
				persistenceService.removeFromCollection(descriptor, o, beanValidationContext.getBeanInstance());
			}
		} else {
			if (isPropertyValueInstanceOfList()) {
				propertyEditContext.setPropertyValue(selected);
			} else {
				Set set = new HashSet();
				set.addAll(selected);
				propertyEditContext.setPropertyValue(set);
			}
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
		return propertyEditContext.getContainerMessages();
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

	public BlankOption getBlankOption()
	{
		return getPropertyDescriptor().isRequired() ? BlankOption.NEVER : BlankOption.ALWAYS;
	}

}
