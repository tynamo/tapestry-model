package org.trailsframework.validation;

import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.ioc.MessageFormatter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.ValidationMessagesSource;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.trailsframework.descriptor.IClassDescriptor;
import org.trailsframework.descriptor.IPropertyDescriptor;
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

	public void record(IClassDescriptor descriptor, InvalidStateException invalidStateException, ValidationTracker validationTracker)
	{
		for (InvalidValue invalidValue : invalidStateException.getInvalidValues())
		{
			String key = invalidValue.getMessage();
			IPropertyDescriptor propertyDescriptor = descriptor.getPropertyDescriptor(invalidValue.getPropertyName());
			Messages messages = messagesSource.getValidationMessages(threadLocale.getLocale());
			MessageFormatter messageFormatter = messages.getFormatter(key);
			if (propertyDescriptor != null)
			{
				validationTracker.recordError(messageFormatter.format(DisplayNameUtils.getDisplayName(propertyDescriptor, messages), invalidValue.getValue()));
			} else
			{
				// This error is an "unassociated (with any field) error".
				validationTracker.recordError(messageFormatter.format());
			}
		}
	}

}
