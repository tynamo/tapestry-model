package org.tynamo.descriptor;

import java.lang.reflect.Method;

public class TynamoMethodDescriptorImpl extends TynamoDescriptor implements IMethodDescriptor
{

	private String name;
	private Class beanType;
	private Class[] argumentTypes;

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	// constructors
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	public TynamoMethodDescriptorImpl(IMethodDescriptor methodDescriptor)
	{
		super(methodDescriptor);
	}

	public TynamoMethodDescriptorImpl(Class beanType, String name, Class returnType, Class[] argumentTypes)
	{
		super(returnType);
		this.beanType = beanType;
		this.name = name;
		this.argumentTypes = argumentTypes;
		setHidden(true);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	// bean setters/getters
	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * (non-Javadoc)
	 *
	 * @see org.tynamo.descriptor.IMethodDescriptor#getArgumentTypes()
	 */

	public Class[] getArgumentTypes()
	{
		return argumentTypes;
	}


	/**
	 * just for serialization pourposes
	 */
	public void setArgumentTypes(Class[] argumentTypes)
	{
		this.argumentTypes = argumentTypes;
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see org.tynamo.descriptor.IMethodDescriptor#getName()
	 */
	public String getName()
	{
		return name;
	}


	/**
	 * just for serialization pourposes
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	public Class getBeanType()
	{
		return beanType;
	}

	/**
	 * just for serialization pourposes
	 */
	public void setBeanType(Class beanType)
	{
		this.beanType = beanType;
	}

	public Method getMethod()
	{
		try
		{
			return beanType.getMethod(name, argumentTypes);
		} catch (NoSuchMethodException e)
		{
			return null;
		}
	}

	@Override
	public Object clone()
	{
		return new TynamoMethodDescriptorImpl(this);
	}
}