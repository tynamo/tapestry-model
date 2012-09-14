package org.tynamo.pages;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.corelib.components.RadioGroup;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.search.SearchFilterOperator;
import org.tynamo.services.DescriptorService;
import org.tynamo.services.PropertySearchFilterContext;
import org.tynamo.services.TynamoBeanContext;

public class PropertySearchFilterBlocks
{
	@Environmental
	@Property(write = false)
	private PropertySearchFilterContext context;

	@Component(parameters = { "value=context.lowValue", "label=prop:context.label",
			"translate=prop:textFieldTranslator", "validate=prop:textFieldValidator", "clientId=prop:context.propertyId",
			"annotationProvider=context" })
	private TextField textField;

	@Component(parameters = { "value=context.lowValue", "label=prop:context.label",
			"translate=prop:numberFieldTranslator", "validate=prop:numberFieldValidator", "clientId=prop:context.propertyId",
			"annotationProvider=context" })
	private TextField numberField;

	@Component(parameters = { "value=context.lowValue", "label=prop:context.label",
			"encoder=valueEncoderForProperty", "model=selectModelForProperty", "validate=prop:selectValidator",
			"clientId=prop:context.propertyId" })
	private Select select;

	@SuppressWarnings("unused")
	@Component(parameters = { "value=context.lowValue", "label=prop:context.label",
			"clientId=prop:context.propertyId" })
	private Checkbox checkboxField;

	@SuppressWarnings("unused")
	@Component(parameters = { "value=context.operatorValue", "label=prop:context.label",
			"clientId=prop:context.operatorId" })
	private RadioGroup searchFilterOperator;

	@Inject
	private DescriptorService descriptorService;

	@Inject
	private Locale locale;

	@Inject
	private TypeCoercer typeCoercer;

	@Environmental(false)
	@Property(write = false)
	private TynamoBeanContext tynamoBeanContext;

	@Inject
	@Property
	private Block missingAdvisor;

	public TynamoPropertyDescriptor getPropertyDescriptor()
	{
		return descriptorService.getClassDescriptor(tynamoBeanContext.getBeanType())
				.getPropertyDescriptor(context.getPropertyId());
	}

	public DateFormat getDateFormat()
	{
		String format = getPropertyDescriptor().getFormat();
		return format != null ? new SimpleDateFormat(format) : DateFormat.getDateInstance(java.text.DateFormat.MEDIUM, locale);
	}

	public NumberFormat getNumberFormat()
	{
		String format = getPropertyDescriptor().getFormat();
		return format != null ? new DecimalFormat(format) : NumberFormat.getInstance(locale);
	}

	public FieldTranslator getTextFieldTranslator() {
		return context.getTranslator(textField);
	}

	public FieldValidator getTextFieldValidator() {
		return context.getValidator(textField);
	}

	public FieldTranslator getNumberFieldTranslator() {
		return context.getTranslator(numberField);
	}

	public FieldValidator getNumberFieldValidator() {
		return context.getValidator(numberField);
	}

	public FieldValidator getSelectValidator() {
		return context.getValidator(select);
	}

	/**
	 * Provide a value encoder for an enum type.
	 */
	@SuppressWarnings("unchecked")
	public ValueEncoder getValueEncoderForProperty() {
		return new EnumValueEncoder(typeCoercer, context.getPropertyType());
	}

	/**
	 * Provide a value encoder for an enum type.
	 */
	@SuppressWarnings("unchecked")
	public ValueEncoder getSearchFilterOperatorEncoder() {
		return new EnumValueEncoder(typeCoercer, SearchFilterOperator.class);
	}

	@SuppressWarnings("unchecked")
	public SearchFilterOperator toOperator(String value) {
		return SearchFilterOperator.valueOf(value);
	}

	/**
	 * Provide a select mode for an enum type.
	 */
	@SuppressWarnings("unchecked")
	public SelectModel getSelectModelForProperty() {
		return new EnumSelectModel(context.getPropertyType(), context.getContainerMessages());
	}

}
