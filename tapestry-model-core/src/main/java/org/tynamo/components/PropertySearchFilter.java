package org.tynamo.components;

import java.lang.annotation.Annotation;
import java.util.Locale;
import java.util.Map.Entry;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentAction;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.FieldTranslator;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.internal.BeanValidationContext;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Primary;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.DataTypeAnalyzer;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.FieldTranslatorSource;
import org.apache.tapestry5.services.FieldValidatorDefaultSource;
import org.apache.tapestry5.services.FormSupport;
import org.tynamo.descriptor.TynamoPropertyDescriptor;
import org.tynamo.search.SearchFilterOperator;
import org.tynamo.search.SearchFilterPredicate;
import org.tynamo.services.PropertySearchFilterContext;
import org.tynamo.services.SearchFilterBlockSource;

public class PropertySearchFilter {
	/**
	 * Configures and stores a {@link PropertySearchFilterContext} into the {@link Environment}.
	 */
	static class SetupEnvironment implements ComponentAction<PropertySearchFilter> {
		private static final long serialVersionUID = 1L;

		private final String property;

		public SetupEnvironment(String property) {
			this.property = property;
		}

		public void execute(PropertySearchFilter component) {
			component.setupEnvironment(property);
		}

		@Override
		public String toString() {
			return String.format("PropertySearchFilter.SetupEnvironment[%s]", property);
		}
	}

	static class CleanupEnvironment implements ComponentAction<PropertySearchFilter> {
		private static final long serialVersionUID = 1L;

		public void execute(PropertySearchFilter component) {
			component.cleanupEnvironment();
		}

		@Override
		public String toString() {
			return "PropertySearchFilter.CleanupEnvironment";
		}
	}

	@Parameter(required = true, allowNull = false)
	private Entry<TynamoPropertyDescriptor, SearchFilterPredicate> entry;

	@Inject
	private SearchFilterBlockSource searchFilterBlockSource;

	@Inject
	private Messages messages;

	@Inject
	private Locale locale;

	@Inject
	private ComponentResources resources;

	@Inject
	private FieldTranslatorSource fieldTranslatorSource;

	@Inject
	private FieldValidatorDefaultSource fieldValidatorDefaultSource;

	@Inject
	private Environment environment;

	@Inject
	private PropertyAccess propertyAccess;

	@Inject
	@Primary
	DataTypeAnalyzer dataTypeAnalyzer;

	@Inject
	private BeanModelSource beanModelSource;

	@Environmental
	private FormSupport formSupport;

	private static final ComponentAction CLEANUP_ENVIRONMENT = new CleanupEnvironment();

	private BeanModel model;

	private PropertyModel propertyModel;

	private String resolveDataType(TynamoPropertyDescriptor descriptor) {

		// we are going backwards - we have the propertydescriptor but DatatypeAnalyzer interface uses property adapters
		ClassPropertyAdapter classAdapter = propertyAccess.getAdapter(descriptor.getBeanType());
		PropertyAdapter adapter = classAdapter.getPropertyAdapter(descriptor.getName());
		return dataTypeAnalyzer.identifyDataType(adapter);
	}

	/**
	 * Returns a Block for rendering the property. The Block will be able to access the {@link PropertySearchFilterContext} via the
	 * {@link Environmental} annotation.
	 */
	Block beginRender() {

		// TODO See PropertyEditor.beginRender() if we need instance specific overrides. We'd have to change from using
		// just property descriptors to using bean model. Not sure if it makes sense for search
		String dataType = resolveDataType(entry.getKey());

		if (dataType == null)
			throw new RuntimeException(String.format("The data type for property '%s' of type %s is null.", entry.getKey()
				.getName(), entry.getKey().getBeanType()));

		try {
			return searchFilterBlockSource.toBlock(dataType);
		} catch (RuntimeException ex) {
			// TODO check if it's ok to pass null object
			String message = messages.format("core-block-error", entry.getKey().getName(), dataType, null, ex);
			throw new TapestryException(message, resources.getLocation(), ex);
		}

	}

	/**
	 * Returns false, to prevent the rendering of the body of the component. PropertySeachFilter should not have a body.
	 */
	boolean beforeRenderBody() {
		return false;
	}

	/**
	 * Record into the Form what's needed to process the property.
	 */
	void setupRender() {
		// Sets up the PropertyEditContext for the duration of the render of this component
		// (which will include the duration of the editor block).

		formSupport.storeAndExecute(this, new SetupEnvironment(entry.getKey().getName()));
	}

	/**
	 * Records into the Form the cleanup logic for the property.
	 */
	void cleanupRender() {
		// Removes the PropertyEditContext after this component (including the editor block)
		// has rendered.

		formSupport.storeAndExecute(this, CLEANUP_ENVIRONMENT);
	}

	/**
	 * Creates a {@link org.apache.tapestry5.services.PropertySearchFilterContext} and pushes it onto the
	 * {@link org.apache.tapestry5.services.Environment} stack.
	 */
	void setupEnvironment(final String propertyName) {
		model = beanModelSource.createEditModel(entry.getKey().getBeanType(), resources.getContainerMessages());
		propertyModel = model.get(propertyName);

		PropertySearchFilterContext context = new PropertySearchFilterContext() {
			public Messages getContainerMessages() {
				return messages;
			}

			public String getLabel() {
				return propertyModel.getLabel();
			}

			public boolean isEnabled() {
				return entry.getValue().isEnabled();
			}

			public void setEnabled(boolean value) {
				entry.getValue().setEnabled(value);
			}

			public String getPropertyId() {
				return propertyModel.getId();
			}

			public Class getPropertyType() {
				return propertyModel.getPropertyType();
			}

			public Object getLowValue() {
				// return propertyModel.getConduit().get(object);
				return entry.getValue().getLowValue();
			}

			public Object getHighValue() {
				return entry.getValue().getHighValue();
			}


			public FieldTranslator getTranslator(Field field) {
				// return fieldTranslatorSource.createDefaultTranslator(field, propertyName, overrides.getOverrideMessages(),
				// locale, propertyModel.getPropertyType(), propertyModel.getConduit());
				return fieldTranslatorSource.createDefaultTranslator(field, propertyName, messages, locale,
					propertyModel.getPropertyType(), propertyModel.getConduit());
			}

			public FieldValidator getValidator(Field field) {
				// return fieldValidatorDefaultSource.createDefaultValidator(field, propertyName, overrides.getOverrideMessages(),
				// locale, propertyModel.getPropertyType(), propertyModel.getConduit());
				return fieldValidatorDefaultSource.createDefaultValidator(field, propertyName, messages,
					locale, propertyModel.getPropertyType(), propertyModel.getConduit());
			}

			public void setLowValue(Object value) {
				entry.getValue().setLowValue(value);
			}

			public void setHighValue(Object value) {
				entry.getValue().setHighValue(value);
			}

			public String getOperatorId() {
				return propertyModel.getId() + "_searchoperator";
			}

			public SearchFilterOperator getOperatorValue() {
				return entry.getValue().getOperator();
			}

			public void setOperatorValue(SearchFilterOperator value) {
				entry.getValue().setOperator(value);
			}

			public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
				return propertyModel.getAnnotation(annotationClass);
			}
		};

		environment.push(PropertySearchFilterContext.class, context);

		BeanValidationContext beanValidationContext = environment.peek(BeanValidationContext.class);

		if (beanValidationContext != null) {
			beanValidationContext.setCurrentProperty(propertyName);
		}
	}

	/**
	 * Called at the end of the form render (or at the end of the form submission) to clean up the {@link Environment} stack.
	 */
	void cleanupEnvironment() {
		environment.pop(PropertySearchFilterContext.class);
	}

}
