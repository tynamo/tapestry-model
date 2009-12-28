package org.tynamo.descriptor.extension;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tynamo.descriptor.DescriptorExtension;
import org.tynamo.exception.TynamoRuntimeException;

public class BlobDescriptorExtension implements DescriptorExtension
{
	protected static final Log LOG = LogFactory.getLog(BlobDescriptorExtension.class);

	public enum ContentDisposition
	{
		INLINE, ATTACHMENT;

		public String getValue()
		{
			return name().toLowerCase();
		}
	}

	public enum RenderType
	{
		IMAGE, LINK; //, IFRAME, ICON; not yet supported in Tynamo

		public boolean isImage()
		{
			return this == IMAGE;
		}

		public boolean isLink()
		{
			return this == LINK;
		}

		public boolean isIFrame()
		{
			return false; // this == IFRAME;
		}

		public boolean isIcon()
		{
			return false; // this == ICON;
		}
	}

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
