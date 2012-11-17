package org.tynamo.descriptor;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tynamo.descriptor.extension.DescriptorExtension;

public class TynamoDescriptor implements Descriptor, Serializable
{
	protected static final Logger LOGGER = LoggerFactory.getLogger(TynamoDescriptor.class);

	protected Class beanType;

	private boolean nonVisual;

	Map<String, DescriptorExtension> extensions = new Hashtable<String, DescriptorExtension>();

	/**
	 * @param dto
	 */
	public TynamoDescriptor(TynamoDescriptor dto)
	{
		try
		{
			BeanUtils.copyProperties(this, dto);
		} catch (IllegalAccessException e)
		{
			LOGGER.error(e.getMessage(), e);
		} catch (InvocationTargetException e)
		{
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e)
		{
			LOGGER.error(e.toString(), e);
		}
	}

	public TynamoDescriptor(Descriptor descriptor)
	{
		try
		{
			BeanUtils.copyProperties(this, descriptor);
			copyExtensionsFrom(descriptor);
		} catch (IllegalAccessException e)
		{
			LOGGER.error(e.getMessage(), e);
		} catch (InvocationTargetException e)
		{
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e)
		{
			LOGGER.error(e.toString(), e);
		}
	}

	public TynamoDescriptor(Class beanType)
	{
		this.beanType = beanType;
	}

	@Override
	public Object clone()
	{
		return new TynamoDescriptor(this);
	}

	public void copyFrom(Descriptor descriptor)
	{
		try
		{
			BeanUtils.copyProperties(this, descriptor);
			copyExtensionsFrom(descriptor);
		} catch (IllegalAccessException e)
		{
			LOGGER.error(e.getMessage(), e);
		} catch (InvocationTargetException e)
		{
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e)
		{
			LOGGER.error(e.toString(), e);
		}
	}

	public void copyExtensionsFrom(Descriptor descriptor)
	{
		Map<String, DescriptorExtension> exts = descriptor.getExtensions();

		for (Map.Entry<String, DescriptorExtension> entry : exts.entrySet())
		{
			String key = entry.getKey();
			DescriptorExtension value = entry.getValue();
			try
			{
				this.addExtension(key, (DescriptorExtension) BeanUtils.cloneBean(value));
			} catch (Exception e)
			{
				//@todo fix clone methods.
			}
		}
	}

	public boolean isNonVisual()
	{
		return nonVisual;
	}

	public void setNonVisual(boolean nonVisual)
	{
		this.nonVisual = nonVisual;
	}

	/**
	 * @deprecated: Use getBeanType instead
	 */
	@Deprecated
	public Class getType()
	{
		return getBeanType();
	}


	/**
	 * Returns the type of bean this descriptor provides metadata for.
	 */
	public Class getBeanType()
	{
		return beanType;
	}

	public void setBeanType(Class beanType)
	{
		this.beanType = beanType;
	}

	/**
	 * Keye is property name preceded by package name
	 */
	public boolean supportsExtension(String keye)
	{
		return getExtension(keye) != null;
	}

	public boolean supportsExtension(Class extensionType)
	{
		return supportsExtension(extensionType.getName());
	}

	/**
	 * Keye is property name preceded by package name
	 */
	public DescriptorExtension getExtension(String key)
	{
		return extensions.get(key);
	}

	/**
	 * Key is property name preceded by package name
	 */
	public void addExtension(String key, DescriptorExtension extension)
	{
		extensions.put(key, extension);
	}

	public void addExtension(Class extensionType, DescriptorExtension extension)
	{
		addExtension(extensionType.getName(), extension);
	}

	/**
	 * Key is property name preceded by package name
	 */
	public void removeExtension(String key)
	{
		extensions.remove(key);
	}

	public void removeExtension(Class extensionType)
	{
		removeExtension(extensionType.getName());
	}

	public <E extends DescriptorExtension> E getExtension(Class<E> extensionType)
	{
		return (E) extensions.get(extensionType.getName());
	}

	/**
	 * This getter method is here just to allow clone(), copyFrom() and
	 * BeanUtils.copyProperties(this, descriptor); to work correctly
	 */
	public Map<String, DescriptorExtension> getExtensions()
	{
		return extensions;
	}

	/**
	 * This setter method is here just to allow clone(), copyFrom() and
	 * BeanUtils.copyProperties(this, descriptor); to work correctly
	 */
	public void setExtensions(Map<String, DescriptorExtension> extensions)
	{
		this.extensions = extensions;
	}
}
