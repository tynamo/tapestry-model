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
 * @author fus8882
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
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

	/* (non-Javadoc)
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
