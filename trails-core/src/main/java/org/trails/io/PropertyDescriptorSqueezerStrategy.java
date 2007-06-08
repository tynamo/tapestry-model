package org.trails.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.io.SqueezeAdaptor;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IPropertyDescriptor;

/**
 * Squeezes a {@link IPropertyDescriptor}
 */
public class PropertyDescriptorSqueezerStrategy implements SqueezeAdaptor
{

	private static final Log LOG = LogFactory.getLog(PropertyDescriptorSqueezerStrategy.class);

	public static final String PREFIX = "W";
	private DescriptorService descriptorService;

	public Class getDataClass()
	{
		return IPropertyDescriptor.class;
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

	public String squeeze(DataSqueezer squeezer, Object object)
	{
		final IPropertyDescriptor propertyDescriptor = (IPropertyDescriptor) object;

		if (LOG.isDebugEnabled())
		{
			LOG.debug("squeezing: " + propertyDescriptor.getBeanType().getName() + "." + propertyDescriptor.getName());
		}

		final String squeezed = squeezer.squeeze(propertyDescriptor.getBeanType());
		return PREFIX + squeezed + "." + propertyDescriptor.getName();
	}

	public Object unsqueeze(DataSqueezer squeezer, String string)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("unsqueezing: " + string);
		}

		final int dotIndex = string.lastIndexOf(".");
		final String squeezed = string.substring(PREFIX.length(), dotIndex);
		final String propertyName = string.substring(dotIndex + 1);
		final Class clazz = (Class) squeezer.unsqueeze(squeezed);
		return getDescriptorService().getClassDescriptor(clazz).getPropertyDescriptor(propertyName);
	}

}