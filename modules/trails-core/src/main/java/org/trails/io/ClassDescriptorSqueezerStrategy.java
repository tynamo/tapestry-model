package org.trails.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.io.SqueezeAdaptor;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;


/**
 * Squeezes a {@link IClassDescriptor}
 */
public class ClassDescriptorSqueezerStrategy implements SqueezeAdaptor
{

	private static final Log LOG = LogFactory.getLog(ClassDescriptorSqueezerStrategy.class);

	public static final String PREFIX = "Y";
	private DescriptorService descriptorService;

	public Class getDataClass()
	{
		return IClassDescriptor.class;
	}

	public String getPrefix()
	{
		return PREFIX;
	}

	public DescriptorService getDescriptorService()
	{
		return descriptorService;
	}

	public void setDescriptorService(DescriptorService descriptorService)
	{
		this.descriptorService = descriptorService;
	}

	public String squeeze(DataSqueezer squeezer, Object classDescriptor)
	{
		final String squeezed = squeezer.squeeze(((IClassDescriptor) classDescriptor).getType());

		if (LOG.isDebugEnabled())
		{
			LOG.debug("squeezing descriptor for: " + squeezed);
		}

		return PREFIX + squeezed;
	}

	public Object unsqueeze(DataSqueezer squeezer, String string)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("unsqueezing: " + string);
		}

		final String squeezed = string.substring(PREFIX.length());
		final Class clazz = (Class) squeezer.unsqueeze(squeezed);
		return getDescriptorService().getClassDescriptor(clazz);
	}

}
