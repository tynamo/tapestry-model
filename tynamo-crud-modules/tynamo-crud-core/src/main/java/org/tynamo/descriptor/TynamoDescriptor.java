/*
 * Created on Jan 28, 2005
 *
 * Copyright 2004 Chris Nelson
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.tynamo.descriptor;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Style - Code Templates
 */
public class TynamoDescriptor implements Descriptor, Serializable
{
	protected static final Log LOG = LogFactory.getLog(TynamoDescriptor.class);

	protected Class type;

	private boolean hidden;

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
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e)
		{
			LOG.error(e.toString());
			e.printStackTrace();
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
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e)
		{
			LOG.error(e.toString());
			e.printStackTrace();
		}
	}

	public TynamoDescriptor(Class type)
	{
		this.type = type;
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
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e)
		{
			LOG.error(e.toString());
			e.printStackTrace();
		}
	}

	public void copyExtensionsFrom(Descriptor descriptor)
	{
		Map<String, DescriptorExtension> exts = descriptor.getExtensions();

		for (Map.Entry<String, DescriptorExtension> entry : exts.entrySet())
		{
			String keye = entry.getKey();
			DescriptorExtension value = entry.getValue();
			try
			{
				this.addExtension(keye, (DescriptorExtension) BeanUtils.cloneBean(value));
			} catch (Exception e)
			{
				//@todo fix clone methods.
			}
		}
	}

	public boolean isHidden()
	{
		return hidden;
	}

	public void setHidden(boolean hidden)
	{
		this.hidden = hidden;
	}

	public Class getType()
	{
		return type;
	}

	public void setType(Class type)
	{
		this.type = type;
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
	public DescriptorExtension getExtension(String keye)
	{
		return extensions.get(keye);
	}

	/**
	 * Keye is property name preceded by package name
	 */
	public void addExtension(String keye, DescriptorExtension extension)
	{
		extensions.put(keye, extension);
	}

	public void addExtension(Class extensionType, DescriptorExtension extension)
	{
		addExtension(extensionType.getName(), extension);
	}

	/**
	 * Keye is property name preceded by package name
	 */
	public void removeExtension(String keye)
	{
		extensions.remove(keye);
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
