package org.tynamo.hibernate.pages;


import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.validator.InvalidStateException;
import org.slf4j.Logger;
import org.tynamo.hibernate.validation.HibernateClassValidatorFactory;
import org.tynamo.hibernate.validation.HibernateValidationDelegate;

public abstract class HibernateEditPage extends HibernateModelPage
{

	@Inject
	private Logger logger;

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

	@Log
	protected void onValidateFormFromForm() throws ValidationException
	{
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

	@Log
	protected Object onSuccess()
	{
		try
		{
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
			logger.error(e.getMessage());
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
