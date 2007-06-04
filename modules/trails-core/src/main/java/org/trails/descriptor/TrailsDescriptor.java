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
package org.trails.descriptor;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.component.Utils;

/**
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Style - Code Templates
 */
public class TrailsDescriptor implements IDescriptor, Serializable
{
	protected static final Log LOG = LogFactory.getLog(TrailsDescriptor.class);

	private String displayName;

	private String shortDescription;

	protected Class type;

	private boolean hidden;

	Map<String, IDescriptorExtension> extensions = new Hashtable<String, IDescriptorExtension>();

	/**
	 * @param dto
	 */
	public TrailsDescriptor(TrailsDescriptor dto)
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

	public TrailsDescriptor(IDescriptor descriptor)
	{
		try
		{
			BeanUtils.copyProperties(this, (TrailsDescriptor) descriptor);
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

	public TrailsDescriptor(Class type)
	{
		this.type = type;
	}

	public String getDisplayName()
	{
		return Utils.unCamelCase(displayName);
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public String getShortDescription()
	{
		return shortDescription;
	}

	public void setShortDescription(String shortDescription)
	{
		this.shortDescription = shortDescription;
	}

	@Override
	public Object clone()
	{
		return new TrailsDescriptor(this);
	}

	public void copyFrom(IDescriptor descriptor)
	{
		try
		{
			BeanUtils.copyProperties(this, (TrailsDescriptor) descriptor);
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

	public void copyExtensionsFrom(IDescriptor descriptor)
	{
		Map<String, IDescriptorExtension> exts = ((TrailsDescriptor) descriptor).getExtensions();

		for (Map.Entry<String, IDescriptorExtension> entry : exts.entrySet())
		{
			String keye = entry.getKey();
			IDescriptorExtension value = entry.getValue();
			try
			{
				this.addExtension(keye, (IDescriptorExtension) BeanUtils.cloneBean(value));
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

	/**
	 * Keye is property name preceded by package name
	 */
	public IDescriptorExtension getExtension(String keye)
	{
		return extensions.get(keye);
	}

	/**
	 * Keye is property name preceded by package name
	 */
	public void addExtension(String keye, IDescriptorExtension extension)
	{
		extensions.put(keye, extension);
	}

	/**
	 * Keye is property name preceded by package name
	 */
	public void removeExtension(String keye)
	{
		extensions.remove(keye);
	}

	public <E extends IDescriptorExtension> E getExtension(Class<E> extensionType)
	{
		return (E) extensions.get(extensionType.getName());
	}

	/**
	 * This getter method is here just to allow clone(), copyFrom() and
	 * BeanUtils.copyProperties(this, descriptor); to work correctly
	 */
	public Map<String, IDescriptorExtension> getExtensions()
	{
		return extensions;
	}

	/**
	 * This setter method is here just to allow clone(), copyFrom() and
	 * BeanUtils.copyProperties(this, descriptor); to work correctly
	 */
	public void setExtensions(Map<String, IDescriptorExtension> extensions)
	{
		this.extensions = extensions;
	}
}
