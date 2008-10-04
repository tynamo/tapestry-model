package org.trails.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.io.SqueezeAdaptor;
import org.trails.util.Utils;
import org.trails.descriptor.DescriptorService;
import org.trails.descriptor.IClassDescriptor;
import org.trails.engine.encoders.abbreviator.EntityNameAbbreviator;
import org.trails.persistence.PersistenceService;
import org.trails.builder.BuilderDirector;
import org.trails.exception.TrailsRuntimeException;

import java.io.Serializable;

/**
 * This class is a Trails adaptation of Tapernate's EntitySqueezerFilter Squeezes persistent entities
 */
public class EntitySqueezerStrategy implements SqueezeFilter, SqueezeAdaptor
{
	private static final Log LOG = LogFactory.getLog(EntitySqueezerStrategy.class);

	String delimiter;
	String prefix;

	private DescriptorService descriptorService;

	private PersistenceService persistenceService;

	private EntityNameAbbreviator entityNameAbbreviator;
	private boolean shouldAbbreviate = false;

	private BuilderDirector builderDirector;

	public String getPrefix()
	{
		return prefix;
	}

	public Class getDataClass()
	{
		return Squeezable.class;
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
			IClassDescriptor classDescriptor = descriptorService.getClassDescriptor(data.getClass());
			if (classDescriptor != null)
			{
				Serializable id = persistenceService.getIdentifier(data, classDescriptor);

				if (LOG.isDebugEnabled())
				{
					LOG.debug("squeezing entity: " + data.toString());
				}

				if (id == null)
				{
					return prefix + entityNameAbbreviator.abbreviate(classDescriptor.getType());
				}

//			Serializable version = (Serializable) persistenceService.getVersion(data, classDescriptor);
				return prefix + abbreviate(classDescriptor.getType()) + delimiter + next.squeeze(id);
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
		if (string.startsWith(prefix))
		{
			final String squeezed = string.substring(prefix.length());
			final int delimiterNdx = squeezed.indexOf(delimiter);

			final String entityName;
			final Serializable id;

			if (delimiterNdx > 0)
			{
				entityName = squeezed.substring(0, delimiterNdx);
				id = (Serializable) next.unsqueeze(squeezed.substring(delimiterNdx + delimiter.length()));
			} else
			{
				entityName = squeezed;
				id = null;
			}

			final Class<?> clazz = (Class<?>) unabbreviate(entityName);

			if (LOG.isDebugEnabled())
			{
				LOG.debug("unsqueezing entity: " + clazz.getName() + " : " + id);
			}

			if (id != null)
			{
				return persistenceService.loadInstance(clazz, id);
			} else
			{
				try
				{
					return builderDirector.createNewInstance(clazz);
				} catch (TrailsRuntimeException e)
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

	public void setDelimiter(String delimiter)
	{
		this.delimiter = delimiter;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	public void setDescriptorService(DescriptorService descriptorService)
	{
		this.descriptorService = descriptorService;
	}

	public void setPersistenceService(PersistenceService persistenceService)
	{
		this.persistenceService = persistenceService;
	}

	public void setEntityNameAbbreviator(EntityNameAbbreviator entityNameAbbreviator)
	{
		this.entityNameAbbreviator = entityNameAbbreviator;
		shouldAbbreviate = entityNameAbbreviator != null;
	}

	public void setBuilderDirector(BuilderDirector builderDirector)
	{
		this.builderDirector = builderDirector;
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
