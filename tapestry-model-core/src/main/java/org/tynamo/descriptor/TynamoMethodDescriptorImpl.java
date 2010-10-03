package org.tynamo.descriptor;

import java.lang.reflect.Method;

public class TynamoMethodDescriptorImpl extends TynamoDescriptor implements IMethodDescriptor
{

	private String name;
	private Class returnType;
	private Class[] argumentTypes;

	public TynamoMethodDescriptorImpl(IMethodDescriptor methodDescriptor)
	{
		super(methodDescriptor);
	}

	public TynamoMethodDescriptorImpl(Class beanType, String name, Class returnType, Class[] argumentTypes)
	{
		super(beanType);
		this.returnType = returnType;
		this.name = name;
		this.argumentTypes = argumentTypes;
		setNonVisual(true);
	}

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

	public Class getReturnType()
	{
		return returnType;
	}

	/**
	 * just for serialization pourposes
	 */
	public void setReturnType(Class returnType)
	{
		this.returnType = returnType;
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