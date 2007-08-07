package org.trails.validation;

import org.apache.tapestry.valid.FieldTracking;
import org.apache.tapestry.valid.RenderString;
import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IPropertyDescriptor;

public class HibernateValidationDelegate extends TrailsValidationDelegate
{

	public void record(IClassDescriptor descriptor, InvalidStateException invalidStateException)
	{
		for (InvalidValue invalidValue : invalidStateException.getInvalidValues())
		{

			IPropertyDescriptor propertyDescriptor = descriptor.getPropertyDescriptor(invalidValue.getPropertyName());
			FieldTracking fieldTracking = null;
			String message = null;
			if (propertyDescriptor != null)
			{
				fieldTracking = (FieldTracking) getFieldTracking(propertyDescriptor.getDisplayName());
				message = propertyDescriptor.getDisplayName() + " " + invalidValue.getMessage();
			} else
			{
				// Making sure that this error is an "unassociated (with any field) error".
				setFormComponent(null);

				fieldTracking = findCurrentTracking();
				message = invalidValue.getMessage();
			}

			fieldTracking.setErrorRenderer(new RenderString(message));
		}
	}

}
