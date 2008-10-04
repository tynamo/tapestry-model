package org.trails.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.io.SqueezeAdaptor;
import org.trails.util.Utils;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.engine.encoders.abbreviator.EntityNameAbbreviator;


/**
 * Squeezes a {@link IClassDescriptor}
 */
public class ClassDescriptorSqueezerStrategy implements SqueezeAdaptor
{

	private static final Log LOG = LogFactory.getLog(ClassDescriptorSqueezerStrategy.class);

	public static final String PREFIX = "Y";
	private DescriptorService descriptorService;
	private EntityNameAbbreviator entityNameAbbreviator;
	private boolean shouldAbbreviate = false;

	public Class getDataClass()
	{
		return IClassDescriptor.class;
	}

	public String getPrefix()
	{
		return PREFIX;
	}

	public void setDescriptorService(DescriptorService descriptorService)
	{
		this.descriptorService = descriptorService;
	}

	public String squeeze(DataSqueezer squeezer, Object object)
	{
		IClassDescriptor classDescriptor = (IClassDescriptor) object;
		final String squeezed = abbreviate(classDescriptor.getType());

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

		return descriptorService.getClassDescriptor(unabbreviate(squeezed));
	}

	public void setEntityNameAbbreviator(EntityNameAbbreviator entityNameAbbreviator)
	{
		this.entityNameAbbreviator = entityNameAbbreviator;
		shouldAbbreviate = entityNameAbbreviator != null;
	}

	private String abbreviate(Class clazz)
	{
		return shouldAbbreviate ? entityNameAbbreviator.abbreviate(clazz) : clazz.getName();
	}

	private Class unabbreviate(String abbreviation)
	{
		return shouldAbbreviate ? entityNameAbbreviator.unabbreviate(abbreviation) : Utils.classForName(abbreviation);
	}
}
