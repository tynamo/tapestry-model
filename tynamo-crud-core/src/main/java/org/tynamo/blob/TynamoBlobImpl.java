package org.tynamo.blob;

import org.tynamo.descriptor.extension.TynamoBlob;

public class TynamoBlobImpl implements TynamoBlob
{
	private static final long serialVersionUID = 283361091832362230L;

	private String fileName;
	private String filePath;
	private String contentType;
	private byte[] bytes;

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public byte[] getBytes()
	{
		return bytes;
	}

	public void setBytes(byte[] bytes)
	{
		this.bytes = bytes;
	}

	public void reset()
	{
		fileName = null;
		filePath = null;
		contentType = null;
		bytes = null;
	}
}
