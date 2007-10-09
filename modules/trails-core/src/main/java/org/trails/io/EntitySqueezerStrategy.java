package org.trails.io;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.io.SqueezeAdaptor;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.persistence.PersistenceService;

/**
 * This class is a Trails adaptation of Tapernate's EntitySqueezerFilter
 * Squeezes persistent entities
 */
public class EntitySqueezerStrategy implements SqueezeFilter, SqueezeAdaptor
{
	private static final Log LOG = LogFactory.getLog(EntitySqueezerStrategy.class);


	String _delimiter;
	String _prefix;


	private DescriptorService descriptorService;

	private PersistenceService persistenceService;

	public String getPrefix()
	{
		return _prefix;
	}

	public void setDelimiter(String delimiter)
	{
		this._delimiter = delimiter;
	}

	public void setPrefix(String prefix)
	{
		this._prefix = prefix;
	}

	public Class getDataClass()
	{
		return Squeezable.class;
	}


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


	public String squeeze(DataSqueezer dataSqueezer, Object o)
	{
		return squeeze(o, dataSqueezer);
	}

	public Object unsqueeze(DataSqueezer dataSqueezer, String s)
	{
		return unsqueeze(s, dataSqueezer);
	}

	public String squeeze(Object data, DataSqueezer next)
	{

		if (data != null)
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
					return _prefix + next.squeeze(classDescriptor.getType()) + _delimiter + next.squeeze(-1);
				}

//			Serializable version = (Serializable) persistenceService.getVersion(data, classDescriptor);
				return _prefix + next.squeeze(classDescriptor.getType()) + _delimiter + next.squeeze(id);
			}
		}
		return next.squeeze(data);
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
		if (string.startsWith(_prefix))
		{
			final String squeezed = string.substring(_prefix.length());
			final int delimiterNdx = squeezed.indexOf(_delimiter);

			final String entityName;
			final Serializable id;

			if (delimiterNdx > 0)
			{
				entityName = squeezed.substring(0, delimiterNdx);
				id = (Serializable) next.unsqueeze(squeezed.substring(delimiterNdx + _delimiter.length()));
			} else
			{
				entityName = squeezed;
				id = null;
			}

			final Class<?> clazz = (Class<?>) next.unsqueeze(entityName);

			if (LOG.isDebugEnabled())
			{
				LOG.debug("unsqueezing entity: " + clazz.getName() + " : " + id);
			}

			if (id != null)
			{
				return getPersistenceService().loadInstance(clazz, id);
			} else
			{
				try
				{
					return clazz.newInstance();
				} catch (InstantiationException e)
				{
					throw new ApplicationRuntimeException("decode-failure: unable to unsqueeze entity", e);
				} catch (IllegalAccessException e)
				{
					throw new ApplicationRuntimeException("decode-failure: unable to unsqueeze entity", e);
				}
			}
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
