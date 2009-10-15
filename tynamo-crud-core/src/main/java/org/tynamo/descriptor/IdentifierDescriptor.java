package org.tynamo.descriptor;

public interface IdentifierDescriptor extends TynamoPropertyDescriptor
{

	public boolean isGenerated();

	public void setGenerated(boolean generated);

}
