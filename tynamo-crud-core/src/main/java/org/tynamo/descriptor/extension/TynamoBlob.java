package org.tynamo.descriptor.extension;

import java.io.Serializable;

/**
 * Represents an uploaded file.
 * It's kind of a Tynamo clone of {@link org.apache.tapestry.request.IUploadFile}
 *
 */
public interface TynamoBlob extends Serializable
{

	/**
	 * @return the name of the file that was uploaded. This is just the filename portion of the complete path.
	 */
	String getFileName();

	void setFileName(String fileName);

	/**
	 * @return the complete path, as reported by the client browser. Different browsers report different things here.
	 */
	String getFilePath();

	void setFilePath(String filePath);

	/**
	 * @return the MIME type specified when the file was uploaded. May return null if the content type is not known.
	 */
	String getContentType();

	void setContentType(String contentType);

	/**
	 * @return the actual file contents
	 */
	byte[] getBytes();

	void setBytes(byte[] bytes);

	/**
	 * Clean and reset the internal state to leave it as a newly created object.
	 */
	void reset();

}
