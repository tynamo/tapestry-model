package org.trails.io;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.services.DataSqueezer;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.persistence.PersistenceService;

/**
 * This class is a Trails adaptation of Tapernate's EntitySqueezerFilter
 * Squeezes persistent entities 
 *
 */
public class EntitySqueezerStrategy implements SqueezeFilter
{
	private static final Log LOG = LogFactory.getLog(EntitySqueezerStrategy.class);


	public static final String DELIMITER = ":";
	public static final String PREFIX = "HIBRN8:";


	private DescriptorService descriptorService;

	private PersistenceService persistenceService;

	public DescriptorService getDescriptorService()
	{
		return descriptorService;
	}

	public void setDescriptorService(DescriptorService descriptorService)
	{
		this.descriptorService = descriptorService;
	}

	public PersistenceService getPersistenceService()
	{
		return persistenceService;
	}

	public void setPersistenceService(PersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}

	public String squeeze(Object data, DataSqueezer next)
	{

		IClassDescriptor classDescriptor = getDescriptorService().getClassDescriptor(data.getClass());
		if (classDescriptor != null)
		{
			Serializable id = (Serializable) persistenceService.getIdentifier(data, classDescriptor);

			if (LOG.isDebugEnabled())
			{
				LOG.debug("squeezing entity: " + data.toString());
			}

			if (id == null)
			{
				new ApplicationRuntimeException("encode-failure: unable to squeeze an unsaved entity");
			}

//			Serializable version = (Serializable) persistenceService.getVersion(data, classDescriptor);
			return PREFIX + next.squeeze(classDescriptor.getType()) + DELIMITER + next.squeeze(id);
		} else
		{
			return next.squeeze(data);
		}
	}

	public String[] squeeze(Object[] objects, DataSqueezer next)
	{
		final String[] squeezed = new String[objects.length];
		for (int i = 0; i < squeezed.length; i++)
		{
			squeezed[i] = squeeze(objects[i], next);
		}
		return squeezed;
	}

	public Object unsqueeze(String string, DataSqueezer next)
	{
		if (string.startsWith(PREFIX))
		{
			final String squeezed = string.substring(PREFIX.length());
			final int delimiterNdx = squeezed.indexOf(DELIMITER);
			final String entityName = squeezed.substring(0, delimiterNdx);
			final Class<?> clazz = (Class<?>) next.unsqueeze(entityName);
			final Serializable id =
				(Serializable) next.unsqueeze(squeezed.substring(delimiterNdx + DELIMITER.length()));

			if (LOG.isDebugEnabled())
			{
				LOG.debug("unsqueezing entity: " + clazz.getName() + " : " + id.toString());
			}

			return getPersistenceService().loadInstance(clazz, id);
		} else
		{
			return next.unsqueeze(string);
		}
	}

	public Object[] unsqueeze(String[] strings, DataSqueezer next)
	{
		final Object[] unsqueezed = new Object[strings.length];
		for (int i = 0; i < unsqueezed.length; i++)
		{
			unsqueezed[i] = unsqueeze(strings[i], next);
		}
		return unsqueezed;
	}
}
