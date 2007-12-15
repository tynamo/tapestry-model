package org.trails.descriptor.extension;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.exception.TrailsRuntimeException;
import org.trails.descriptor.IDescriptorExtension;
import org.trails.descriptor.IExpressionSupport;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.IDescriptor;
import org.trails.descriptor.extension.ITrailsBlob;

public class BlobDescriptorExtension implements IDescriptorExtension,
		IExpressionSupport
{
	protected static final Log LOG = LogFactory
			.getLog(BlobDescriptorExtension.class);

	private Class beanType;

	private Class propertyType;

	private boolean hidden = true;

	private boolean searchable = true;

	private IPropertyDescriptor propertyDescriptor = null;

	public enum ContentDisposition {

		INLINE("inline"), ATTACHMENT("attachment");

		private String value = "";

		ContentDisposition(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum RenderType {
		IMAGE, LINK, IFRAME;

		public boolean isImage() {
			return this == IMAGE;
		}

		public boolean isLink() {
			return this == LINK;
		}

		public boolean isIFrame() {
			return this == IFRAME;
		}
	}

	private enum BlobType {
		BYTES, ITRAILSBLOB
	}

	private BlobType blobType = BlobType.BYTES;

	private String fileName = "";

	private String contentType = "";

	private ContentDisposition contentDisposition = ContentDisposition.INLINE;

	private RenderType renderType = RenderType.LINK;

	/**
	 * 
	 * @param beanType
	 * @param propertyDescriptor
	 */
	public BlobDescriptorExtension(Class beanType,
			IPropertyDescriptor propertyDescriptor) {
		this.beanType = beanType;
		this.propertyType = propertyDescriptor.getPropertyType();
		this.propertyDescriptor = propertyDescriptor;

		if (ITrailsBlob.class.isAssignableFrom(beanType)) {
			blobType = BlobType.ITRAILSBLOB;
		} else if (beanType.isArray()) {
			blobType = BlobType.BYTES;
		} else {
			throw new TrailsRuntimeException("type: " + beanType
					+ " - Not supported");
		}
	}

	/**
	 * 
	 * @param dto
	 */
	public BlobDescriptorExtension(BlobDescriptorExtension dto) {
		try {
			BeanUtils.copyProperties(this, dto);
		} catch (IllegalAccessException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LOG.error(e.toString());
			e.printStackTrace();
		}
	}

	public boolean isBytes() {
		return blobType == BlobType.BYTES;
	}

	public boolean isITrailsBlob() {
		return blobType == BlobType.ITRAILSBLOB;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public RenderType getRenderType() {
		return renderType;
	}

	public void setRenderType(RenderType renderType) {
		this.renderType = renderType;
	}

	public ContentDisposition getContentDisposition() {
		return contentDisposition;
	}

	public void setContentDisposition(ContentDisposition contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	/**
	 * Overrides
	 */
	@Override
	public Object clone() {
		return new BlobDescriptorExtension(this);
	}

	public void copyFrom (IDescriptor descriptor) {

		try {
			BeanUtils.copyProperties(this, (BlobDescriptorExtension)descriptor);
		} catch (IllegalAccessException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LOG.error(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * Interface Implementation
	 */

	public String findAddExpression() {
		return getExpression("");
	}

	public String findRemoveExpression() {
		return getExpression("");
	}

	public String getExpression(String method) {
		return getPropertyDescriptor().getName();
	}

	public Class getBeanType() {
		return beanType;
	}

	public void setBeanType(Class beanType) {
		this.beanType = beanType;
	}

	public Class getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(Class propertyType) {
		this.propertyType = propertyType;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public IPropertyDescriptor getPropertyDescriptor() {
		return propertyDescriptor;
	}

	public void setPropertyDescriptor(IPropertyDescriptor propertyDescriptor) {
		this.propertyDescriptor = propertyDescriptor;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}
