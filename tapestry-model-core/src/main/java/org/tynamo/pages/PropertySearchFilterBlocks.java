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
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
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

	@Component(parameters = { "value=context.lowValue", "label=prop:context.label", "encoder=valueEncoderForProperty",
			"model=selectModelForProperty", "validate=prop:enumSearchFilterValidator",
			"clientId=prop:context.propertyId" })
	private Select enumSearchFilter;

	@Component(parameters = { "value=context.lowValue", "label=prop:context.label", "clientId=prop:context.propertyId",
			"encoder=booleanValueEncoder" })
	private RadioGroup booleanSearchFilter;

	@Component(parameters = { "value=context.operatorValue", "label=prop:context.label",
			"clientId=prop:context.operatorId", "encoder=searchFilterOperatorEncoder" })
	private RadioGroup numberSearchFilterOperator;

	// @Component(parameters = { "value=context.operatorValue", "label=prop:context.label",
	// "clientId=prop:context.operatorId" })
	// private RadioGroup searchFilterOperator;

	public boolean getFilterEnabled() {
		return context.isEnabled();
	}

	public void setFilterEnabled(boolean value) {
		context.setEnabled(value);
	}

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

	@InjectComponent
	TextField numberFieldEq;

	public FieldTranslator getNumberFieldTranslator() {
		return context.getTranslator(numberFieldEq);
	}

	public FieldValidator getNumberFieldValidator() {
		return context.getValidator(numberFieldEq);
	}

	public FieldValidator getEnumSearchFilterValidator() {
		return context.getValidator(enumSearchFilter);
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

	public ValueEncoder getBooleanValueEncoder() {
		return new ValueEncoder<Boolean>() {
			public String toClient(Boolean value) {
				return value == null ? "" : value.toString();
			}
			public Boolean toValue(String clientValue) {
				return clientValue.equals("") ? null : Boolean.valueOf(clientValue);
			}
		};
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
