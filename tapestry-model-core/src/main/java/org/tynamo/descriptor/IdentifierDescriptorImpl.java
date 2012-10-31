package org.tynamo.descriptor;


public class IdentifierDescriptorImpl extends TynamoPropertyDescriptorImpl implements IdentifierDescriptor
{
	private boolean generated = true;

	public IdentifierDescriptorImpl(Class beanType, TynamoPropertyDescriptor descriptor)
	{
		super(beanType, descriptor.getPropertyType());
		copyFrom(descriptor);
		setSearchable(false);
	}

	/**
	 * @param realDescriptor
	 */
	public IdentifierDescriptorImpl(Class beanType, Class type)
	{
		super(beanType, type);
		setSearchable(false);
	}

	public IdentifierDescriptorImpl(Class beanType, String name, Class type)
	{
		super(beanType, name, type);
		setSearchable(false);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.tynamo.descriptor.PropertyDescriptor#isIdentifier()
	 */
	public boolean isIdentifier()
	{
		return true;
	}

	/**
	 * @return Returns the generated.
	 */
	public boolean isGenerated()
	{
		return generated;
	}

	/**
	 * @param generated The generated to set.
	 */
	public void setGenerated(boolean generated)
	{
		this.generated = generated;
	}

	public Object clone()
	{
		return new IdentifierDescriptorImpl(getBeanType(), this);
	}


}
