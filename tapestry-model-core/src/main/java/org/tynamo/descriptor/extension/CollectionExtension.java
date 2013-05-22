package org.tynamo.descriptor.extension;

public class CollectionExtension implements DescriptorExtension
{
	private boolean childRelationship = false;

	private String inverseProperty = null;

	private String addExpression = null;

	private String removeExpression = null;

	private String swapExpression = null;

	private boolean allowRemove = true;

	public boolean isChildRelationship()
	{
		return childRelationship;
	}

	public void setChildRelationship(boolean childRelationship)
	{
		this.childRelationship = childRelationship;
	}

	public String getInverseProperty()
	{
		return inverseProperty;
	}

	public void setInverseProperty(String inverseProperty)
	{
		this.inverseProperty = inverseProperty;
	}

	public String getAddExpression()
	{
		return addExpression;
	}

	public void setAddExpression(String addExpression)
	{
		this.addExpression = addExpression;
	}

	public String getRemoveExpression()
	{
		return removeExpression;
	}

	public void setRemoveExpression(String removeExpression)
	{
		this.removeExpression = removeExpression;
	}

	public String getSwapExpression()
	{
		return swapExpression;
	}

	public void setSwapExpression(String swapExpression)
	{
		this.swapExpression = swapExpression;
	}

	public boolean isAllowRemove()
	{
		return allowRemove;
	}

	public void setAllowRemove(boolean allowRemove)
	{
		this.allowRemove = allowRemove;
	}
}
