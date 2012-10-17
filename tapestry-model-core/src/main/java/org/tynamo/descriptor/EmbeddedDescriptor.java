package org.tynamo.descriptor;

public class EmbeddedDescriptor extends TynamoClassDescriptorImpl implements IdentifierDescriptor
{

	private static final long serialVersionUID = 1L;

	private boolean readOnly;

	private boolean identifier;

	private boolean generated;

	private String name;

	private boolean required;

	private int length;

	private boolean large;

	private String format;

	private boolean searchable;

	private boolean richText;

	private Class propertyType;

	public EmbeddedDescriptor(Class propertyType, TynamoClassDescriptor descriptor)
	{
		super(descriptor);
		this.propertyType = propertyType;
	}

	public EmbeddedDescriptor(Class beanType, String name, Class propertyType)
	{
		super(beanType);
		this.name = name;
		this.propertyType = propertyType;
	}

	public boolean isNumeric()
	{
		return false;
	}

	public boolean isBoolean()
	{
		return false;
	}

	public boolean isDate()
	{
		return false;
	}

	public boolean isString()
	{
		return false;
	}

	public boolean isObjectReference()
	{
		return false;
	}

	public boolean isTransient() {
		return false;
	}

	public void setTransient(boolean value) {
	}

	public boolean isOwningObjectReference()
	{
		return false;
	}

	public boolean isEmbedded()
	{
		return true;
	}

	public Class getPropertyType()
	{
		return propertyType;
	}

	public void setPropertyType(Class propertyType)
	{
		this.propertyType = propertyType;
	}

	public boolean isReadOnly()
	{
		return readOnly;
	}

	public void setReadOnly(boolean readOnly)
	{
		this.readOnly = readOnly;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isCollection()
	{
		return false;
	}

	public String getFormat()
	{
		return format;
	}

	public void setFormat(String format)
	{
		this.format = format;
	}

	public boolean isLarge()
	{
		return large;
	}

	public void setLarge(boolean large)
	{
		this.large = large;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public boolean isSearchable()
	{
		return searchable;
	}

	public void setSearchable(boolean searchable)
	{
		this.searchable = searchable;
	}

	public Class getBeanType()
	{
		return beanType;
	}

	public void setBeanType(Class beanType)
	{
		this.beanType = beanType;
	}

	public boolean isRichText()
	{
		return richText;
	}

	public void setRichText(boolean richText)
	{
		this.richText = richText;
	}

	public boolean isRequired()
	{
		return required;
	}

	public void setRequired(boolean required)
	{
		this.required = required;
	}

	public boolean isIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(boolean identifier)
	{
		this.identifier = identifier;
	}

	public boolean isGenerated()
	{
		return generated;
	}

	public void setGenerated(boolean generated)
	{
		this.generated = generated;
	}

	@Override
	public void copyFrom(Descriptor descriptor)
	{

		super.copyFrom(descriptor);
	}

	@Override
	public Object clone()
	{
		return new EmbeddedDescriptor(getPropertyType(), this);
	}

}
