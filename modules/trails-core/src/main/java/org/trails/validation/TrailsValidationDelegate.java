package org.trails.validation;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.valid.FieldTracking;
import org.apache.tapestry.valid.IFieldTracking;
import org.apache.tapestry.valid.RenderString;
import org.apache.tapestry.valid.ValidationDelegate;

public class TrailsValidationDelegate extends ValidationDelegate
{

	public void record(Exception ex)
	{
		FieldTracking tracking = findCurrentTracking();
		tracking.setErrorRenderer(new RenderString(ex.getMessage()));
	}

	public IFieldTracking getFieldTracking(String displayName)
	{
		if (_trackingsByDisplayName.containsKey(displayName))
		{
			return _trackingsByDisplayName.get(displayName);
		} else
		{
			return findCurrentTracking();
		}
	}

	protected Map<String, IFieldTracking> _trackingsByDisplayName = new HashMap<String, IFieldTracking>();

	@Override
	public void recordFieldInputValue(String input)
	{
		super.recordFieldInputValue(input);
		FieldTracking tracking = findCurrentTracking();
		tracking.setInput(input);
		_trackingsByDisplayName.put(tracking.getComponent().getDisplayName(), tracking);
	}
}
