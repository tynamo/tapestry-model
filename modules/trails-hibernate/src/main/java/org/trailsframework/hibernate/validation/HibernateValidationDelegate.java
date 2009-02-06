package org.trailsframework.hibernate.validation;

import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.ioc.MessageFormatter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.ValidationMessagesSource;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.trailsframework.descriptor.TrailsClassDescriptor;
import org.trailsframework.descriptor.TrailsPropertyDescriptor;
import org.trailsframework.util.DisplayNameUtils;


public class HibernateValidationDelegate //extends TrailsValidationDelegate
{

	private ValidationMessagesSource messagesSource;
	private ThreadLocale threadLocale;

	public HibernateValidationDelegate(ValidationMessagesSource messagesSource, ThreadLocale threadLocale)
	{
		this.messagesSource = messagesSource;
		this.threadLocale = threadLocale;
	}

	/**
	 * Records error messages for all the class level or method level constraints violations.
	 */
	public void record(TrailsClassDescriptor descriptor, InvalidStateException invalidStateException, ValidationTracker validationTracker, Messages componentMessages)
	{
		for (InvalidValue invalidValue : invalidStateException.getInvalidValues())
		{
			String key = invalidValue.getMessage();
			TrailsPropertyDescriptor propertyDescriptor = descriptor.getPropertyDescriptor(invalidValue.getPropertyName());
			MessageFormatter messageFormatter = messagesSource.getValidationMessages(threadLocale.getLocale()).getFormatter(key);
			if (propertyDescriptor != null)
			{
				validationTracker.recordError(messageFormatter.format(DisplayNameUtils.getDisplayName(propertyDescriptor, componentMessages), invalidValue.getValue()));
			} else
			{
				// This error is an "unassociated (with any field) error".
				validationTracker.recordError(messageFormatter.format());
			}
		}

	}

}
