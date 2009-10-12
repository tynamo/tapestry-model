package org.tynamo.hibernate.pages;


import org.apache.tapestry5.Link;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tynamo.hibernate.validation.HibernateClassValidatorFactory;
import org.tynamo.hibernate.validation.HibernateValidationDelegate;
import org.hibernate.validator.InvalidStateException;

public abstract class HibernateEditPage extends HibernateModelPage
{

	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateEditPage.class);

	@Component
	private Form form;

	public Form getForm()
	{
		return form;
	}

	void pageLoaded()
	{
		// Make other changes to the bean here.
	}


	@Inject
	private HibernateClassValidatorFactory hibernateClassValidatorFactory;

	@Inject
	private HibernateValidationDelegate hibernateValidationDelegate;

	protected void onValidateFormFromForm() throws ValidationException
	{
		LOGGER.debug("validating");
		//add more validation logic here
		try
		{
			/**
			 * The hibernate validate listener is enabled by default, so if nothing is wrong this entity will be
			 * validated twice, once here, and once in session.saveOrUpdate(instance);
			 */
			hibernateClassValidatorFactory.validateEntity(getBean());
		} catch (InvalidStateException ise)
		{
			hibernateValidationDelegate.record(getClassDescriptor(), ise, getForm().getDefaultTracker(), getMessages());
		}
	}

	protected Object onSuccess()
	{
		try
		{

			LOGGER.debug("saving....");
			getPersitenceService().save(getBean());
			return back();

		} catch (InvalidStateException ise)
		{
			hibernateValidationDelegate.record(getClassDescriptor(), ise, getForm().getDefaultTracker(), getMessages());
		} catch (Exception e)
		{
//			missing ExceptionUtils (Lang 2.3 API)
//			form.recordError(ExceptionUtil.getRootCause(e));
			getForm().recordError(e.getMessage());
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected BeanModel createBeanModel(Class clazz)
	{
		return getBeanModelSource().createEditModel(clazz, getMessages());
	}
}
