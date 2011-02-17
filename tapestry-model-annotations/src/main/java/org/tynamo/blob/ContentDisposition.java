package org.tynamo.blob;


public enum ContentDisposition
{
	INLINE, ATTACHMENT;

	public String getValue()
	{
		return name().toLowerCase();
	}
}