/*
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

/**
 * This class represents a to-one association and is created by HibernateDescriptorDecorator
 *
 * @author Chris Nelson
 * @see HibernateDescriptorDecorator
 */
public class ObjectReferenceDescriptor extends TynamoPropertyDescriptorImpl
{
	private Class actualType;

	public ObjectReferenceDescriptor(Class beanType, TynamoPropertyDescriptor descriptor,
									 Class actualType)
	{
		this(beanType, descriptor.getPropertyType(), actualType);
		copyFrom(descriptor);
	}

	/**
	 * @param realDescriptor
	 */
	public ObjectReferenceDescriptor(
		Class beanType, Class declaredType, Class actualType)
	{
		super(beanType, declaredType);
		this.actualType = actualType;
	}

	/* (non-Javadoc)
		 * @see org.tynamo.descriptor.PropertyDescriptor#getPropertyType()
		 */
	public Class getPropertyType()
	{
		return actualType;
	}

	/* (non-Javadoc)
		 * @see org.tynamo.descriptor.PropertyDescriptor#isObjectReference()
		 */
	public boolean isObjectReference()
	{
		return true;
	}

	public Object clone()
	{
		return new ObjectReferenceDescriptor(getBeanType(), this, getPropertyType());
	}
}