package org.tynamo.model.jpa.pages;


import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import org.tynamo.FlashMessage;
import org.tynamo.components.Flash;
import org.tynamo.descriptor.TynamoClassDescriptor;
import org.tynamo.util.Utils;

public abstract class JPAEditPage extends JPAModelPage
{

	@Inject
	private Logger logger;

	@Component
	private Form form;

	@Component
	private Flash flash;

	private boolean shouldStayHere;

	public Form getForm()
	{
		return form;
	}

	void pageLoaded()
	{
		// Make other changes to the bean here.
	}

	@Override
	protected void activate(Object bean, TynamoClassDescriptor classDescriptor, BeanModel beanModel)
	{
		shouldStayHere = false;
		super.activate(bean, classDescriptor, beanModel);
	}

	@Log
	protected void onValidateFormFromForm() throws ValidationException
	{

		//add more validation logic here
		/* TODO
		try
		{/* TODO */
			/**
			 * The jpa validate listener is enabled by default, so if nothing is wrong this entity will be
			 * validated twice, once here, and once in session.saveOrUpdate(instance);
			 */
			/*hibernateClassValidatorFactory.validateEntity(getBean());
		} catch (InvalidStateException ise)
		{
			hibernateValidationDelegate.record(getClassDescriptor(), ise, getForm().getDefaultTracker(), getMessages());
		}*/
	}

	void onApply()
	{
		shouldStayHere = true;
	}

	@Log
	protected Object onSuccess()
	{
		try
		{
			getPersitenceService().save(getBean());
			if (shouldStayHere)
			{
				flash.addFlashByKey(getSuccessMessageKey(), FlashMessage.MessageType.SUCCESS, getBean());
				return this;
			} else
			{
				return back();
			}


			//hibernateValidationDelegate.record(getClassDescriptor(), ise, getForm().getDefaultTracker(), getMessages());
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

	protected String getSuccessMessageKey()
	{
		return Utils.SAVED_MESSAGE;
	}
}
