package org.trailsframework.hibernate.pages;

import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.validator.InvalidStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trailsframework.pages.ModelPage;
import org.trailsframework.hibernate.validation.HibernateClassValidatorFactory;
import org.trailsframework.hibernate.validation.HibernateValidationDelegate;


public abstract class HibernateModelPage extends ModelPage
{

	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateModelPage.class);

	@Inject
	private HibernateClassValidatorFactory hibernateClassValidatorFactory;

	@Inject
	private HibernateValidationDelegate hibernateValidationDelegate;

	void onValidateFormFromForm() throws ValidationException
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

	Object onSuccess()
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

}
