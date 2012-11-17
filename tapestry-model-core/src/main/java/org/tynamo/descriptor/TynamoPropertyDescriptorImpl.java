package org.tynamo.descriptor;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class TynamoPropertyDescriptorImpl extends TynamoDescriptor implements TynamoPropertyDescriptor
{

	private Class propertyType;

	private String name;

	private boolean searchable = true;

	private boolean required;

	private boolean readOnly;

	private int length = DEFAULT_LENGTH;

	private boolean large;

	private String format;

	private boolean richText;

	/**
	 * It's kinda like an old-skool C++ copy constructor
	 */
	public TynamoPropertyDescriptorImpl(Class beanType, TynamoPropertyDescriptor descriptor)
	{
		this(beanType, descriptor.getPropertyType());

		try
		{
			BeanUtils.copyProperties(this, (TynamoPropertyDescriptorImpl) descriptor);
		} catch (IllegalAccessException e)
		{
			LOGGER.error(e.getMessage(), e);
		} catch (InvocationTargetException e)
		{
			LOGGER.error(e.getMessage(), e);
		} catch (Exception e)
		{
			LOGGER.error(e.getMessage(), e);
		}
	}

	public TynamoPropertyDescriptorImpl(Class beanType, Class propertyType)
	{
		super(beanType);
		this.propertyType = propertyType;
	}

	public TynamoPropertyDescriptorImpl(Class beanType, String name, Class propertyType)
	{
		this(beanType, propertyType);
		this.setName(name);
	}

	/**
	 * @param dto
	 */
	public TynamoPropertyDescriptorImpl(TynamoPropertyDescriptorImpl dto)
	{
		super(dto);

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
			LOGGER.error(e.getMessage(), e);
		}
	}

	public Class getPropertyType()
	{
		return propertyType;
	}

	public void setPropertyType(Class propertyType)
	{
		this.propertyType = propertyType;
	}

	/**
	 * @return
	 */
	public boolean isNumeric()
	{
		return getPropertyType().getName().endsWith("Double")
				|| getPropertyType().getName().endsWith("Integer")
				|| getPropertyType().getName().endsWith("Float")
				|| getPropertyType().getName().endsWith("double")
				|| getPropertyType().getName().endsWith("int")
				|| getPropertyType().getName().endsWith("float")
				|| getPropertyType().getName().endsWith("BigDecimal");
	}

	public boolean isBoolean()
	{
		return getPropertyType().getName().endsWith("boolean")
				|| getPropertyType().getName().endsWith("Boolean");
	}

	/**
	 * @return
	 */
	public boolean isDate()
	{
		return getPropertyType().getName().endsWith("Date");
	}

	/**
	 * @return
	 */
	public boolean isString()
	{
		return getPropertyType().getName().endsWith("String");
	}

	public boolean isObjectReference()
	{
		return false;
	}

	/**
	 * @return Returns the required.
	 */
	public boolean isRequired()
	{
		return required;
	}

	/**
	 * @param required The required to set.
	 */
	public void setRequired(boolean required)
	{
		this.required = required;
	}

	/**
	 * @return
	 */
	public boolean isReadOnly()
	{
		return readOnly;
	}

	/**
	 * @param readOnly The readOnly to set.
	 */
	public void setReadOnly(boolean readOnly)
	{
		this.readOnly = readOnly;
	}

	/**
	 * @return Returns the identifier.
	 */
	public boolean isIdentifier()
	{
		return false;
	}

	/**
	 * @return Returns the collection.
	 */
	public boolean isCollection()
	{
		return false;
	}

	@Override
	public Object clone()
	{
		return new TynamoPropertyDescriptorImpl(this);
	}

	@Override
	public void copyFrom(Descriptor descriptor)
	{
		super.copyFrom(descriptor);

		if (descriptor instanceof TynamoPropertyDescriptorImpl)
		{
			try
			{
				BeanUtils.copyProperties(this, (TynamoPropertyDescriptorImpl) descriptor);
			} catch (IllegalAccessException e)
			{
				LOGGER.error(e.getMessage(), e);
			} catch (InvocationTargetException e)
			{
				LOGGER.error(e.getMessage(), e);
			} catch (Exception e)
			{
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public boolean isLarge()
	{
		return large;
	}

	public void setLarge(boolean large)
	{
		this.large = large;
	}

	public String getFormat()
	{
		return format;
	}

	public void setFormat(String format)
	{
		this.format = format;
	}

	public boolean isSearchable()
	{
		return searchable;
	}

	public void setSearchable(boolean searchable)
	{
		this.searchable = searchable;
	}

	public boolean isRichText()
	{
		return richText;
	}

	public void setRichText(boolean richText)
	{
		this.richText = richText;
	}

	public boolean isEmbedded()
	{
		return false;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
