package org.trailsframework.validation;


public class HibernateValidationDelegate //extends TrailsValidationDelegate
{

/*
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
*/

}
