package org.tynamo.descriptor;

import java.util.List;

public interface TynamoClassDescriptor extends Descriptor
{
	/**
	 * Returns the type of bean to which this property belongs.
	 */
	Class getBeanType();

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
	public List<TynamoPropertyDescriptor> getPropertyDescriptors();

	/**
	 * @param propertyDescriptors The propertyDescriptors to set.
	 */
	public void setPropertyDescriptors(List<TynamoPropertyDescriptor> propertyDescriptors);

	public TynamoPropertyDescriptor getIdentifierDescriptor();

	/**
	 * @param string
	 * @return
	 */
	public TynamoPropertyDescriptor getPropertyDescriptor(String name);

	/**
	 * @return
	 */
	public boolean isChild();

	/**
	 * @param
	 */
	public void setChild(boolean child);

	public List<TynamoPropertyDescriptor> getPropertyDescriptors(List<String> propertyNames);

	public boolean isAllowSave();

	public void setAllowSave(boolean allowSave);

	public boolean isAllowRemove();

	public void setAllowRemove(boolean allowRemove);

	public boolean getHasCyclicRelationships();

	public void setHasCyclicRelationships(boolean hasBidirectionalRelationship);

	public boolean isSearchable();

	public void setSearchable(boolean searchable);

}