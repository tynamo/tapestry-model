package org.trails.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.io.SqueezeAdaptor;
import org.trails.exception.TrailsRuntimeException;
import org.trails.util.Utils;

/**
 * Squeezes a {@link Class}
 */
public class ClassAdaptor implements SqueezeAdaptor
{

	private static final Log LOG = LogFactory.getLog(ClassAdaptor.class);

	public static final String PREFIX = "D";

	public Class getDataClass()
	{
		return Class.class;
	}

	public String getPrefix()
	{
		return PREFIX;
	}

	public String squeeze(DataSqueezer squeezer, Object object)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("squeezing: " + object.toString());
		}


		return PREFIX + ((Class) object).getName();
	}

	public Object unsqueeze(DataSqueezer squeezer, String string)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("unsqueezing: " + string);
		}

		final String className = string.substring(PREFIX.length());

		try
		{
			return Utils.classForName(className);

		} catch (TrailsRuntimeException cnfe)
		{
			throw new TrailsRuntimeException("decode-failure", cnfe);
		}
	}
}