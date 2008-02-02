package org.trails.descriptor.extension;

import java.io.Serializable;

public interface ITrailsBlob extends Serializable
{

	String getFileName();

	void setFileName(String fileName);

	String getFilePath();

	void setFilePath(String filePath);

	String getContentType();

	void setContentType(String contentType);

	byte[] getBytes();

	void setBytes(byte[] bytes);

	void reset();

}
