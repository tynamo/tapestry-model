package org.tynamo.descriptor.extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tynamo.blob.ContentDisposition;
import org.tynamo.blob.RenderType;
import org.tynamo.exception.TynamoRuntimeException;

public class BlobDescriptorExtension implements DescriptorExtension
{
	private static final Logger logger = LoggerFactory.getLogger(BlobDescriptorExtension.class);

	private enum BlobType
	{
		BYTES, TYNAMO_BLOB
	}

	private BlobType blobType = BlobType.BYTES;

	private String fileName = "";

	private String contentType = "";

	private ContentDisposition contentDisposition = ContentDisposition.INLINE;

	private RenderType renderType = RenderType.LINK;

	/**
	 * @param beanType
	 */
	public BlobDescriptorExtension(Class beanType)
	{
		if (TynamoBlob.class.isAssignableFrom(beanType))
		{
			blobType = BlobType.TYNAMO_BLOB;
		} else if (beanType.isArray())
		{
			blobType = BlobType.BYTES;
		} else
		{
			throw new TynamoRuntimeException("type: " + beanType + " - Not supported");
		}
	}

	public boolean isBytes()
	{
		return blobType == BlobType.BYTES;
	}

	public boolean isITynamoBlob()
	{
		return blobType == BlobType.TYNAMO_BLOB;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public RenderType getRenderType()
	{
		return renderType;
	}

	public void setRenderType(RenderType renderType)
	{
		this.renderType = renderType;
	}

	public ContentDisposition getContentDisposition()
	{
		return contentDisposition;
	}

	public void setContentDisposition(ContentDisposition contentDisposition)
	{
		this.contentDisposition = contentDisposition;
	}
}
