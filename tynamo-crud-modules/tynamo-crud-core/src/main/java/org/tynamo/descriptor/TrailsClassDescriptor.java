/*
 * Created on Mar 18, 2005
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

import java.util.List;

public interface TrailsClassDescriptor extends Descriptor
{
	public Class getType();

	/**
	 * @return Returns the methodDescriptors.
	 */
	public List<IMethodDescriptor> getMethodDescriptors();

	/**
	 * @param methodDescriptors The methodDescriptors to set.
	 */
	public void setMethodDescriptors(List<IMethodDescriptor> methodDescriptors);

	/**
	 * @return Returns the propertyDescriptors.
	 */
	public List<TrailsPropertyDescriptor> getPropertyDescriptors();

	/**
	 * @param propertyDescriptors The propertyDescriptors to set.
	 */
	public void setPropertyDescriptors(List<TrailsPropertyDescriptor> propertyDescriptors);

	public TrailsPropertyDescriptor getIdentifierDescriptor();

	/**
	 * @param string
	 * @return
	 */
	public TrailsPropertyDescriptor getPropertyDescriptor(String name);

	/**
	 * @return
	 */
	public boolean isChild();

	/**
	 * @param
	 */
	public void setChild(boolean child);

	public List<TrailsPropertyDescriptor> getPropertyDescriptors(List<String> propertyNames);

	public boolean isAllowSave();

	public void setAllowSave(boolean allowSave);

	public boolean isAllowRemove();

	public void setAllowRemove(boolean allowRemove);

	public boolean getHasCyclicRelationships();

	public void setHasCyclicRelationships(boolean hasBidirectionalRelationship);

}