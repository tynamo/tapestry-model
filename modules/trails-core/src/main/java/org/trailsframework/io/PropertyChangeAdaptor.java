package org.trails.io;

import org.apache.tapestry.record.PropertyChange;
import org.apache.tapestry.record.PropertyChangeImpl;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.io.SqueezeAdaptor;


/**
 * Squeezes a {@link PropertyChangeAdaptor}
 */
public class PropertyChangeAdaptor implements SqueezeAdaptor
{
	public static final String DELIMITER = "trails:";
	public static final String PREFIX = "X";

	public String getPrefix()
	{
		return PREFIX;
	}

	public Class getDataClass()
	{
		return PropertyChange.class;
	}

	public String squeeze(DataSqueezer next, Object o)
	{
		PropertyChange propertyChange = (PropertyChange) o;
		return PREFIX + next.squeeze(propertyChange.getComponentPath()) + DELIMITER +
			next.squeeze(propertyChange.getPropertyName() + DELIMITER + next.squeeze(propertyChange.getNewValue()));

	}

	public Object unsqueeze(DataSqueezer next, String string)
	{
		String[] squeezeds = string.substring(PREFIX.length()).split(DELIMITER);
		return new PropertyChangeImpl((String) next.unsqueeze(squeezeds[0]), (String) next.unsqueeze(squeezeds[1]),
			next.unsqueeze(squeezeds[2]));
	}
}
