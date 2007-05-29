package org.trails.test;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.descriptor.BlobDescriptorExtension.ContentDisposition;
import org.trails.descriptor.BlobDescriptorExtension.RenderType;
import org.trails.descriptor.annotation.BlobDescriptor;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.PropertyDescriptor;

/**
 * A UploadableMediaDelegate has a document
 * 
 * @author kenneth.colassi 				nhhockeyplayer@hotmail.com
 */
@Entity
@ClassDescriptor(hidden = false)
public class UploadableMediaDelegate implements Serializable {
	private static final Log log = LogFactory.getLog(UploadableMediaDelegate.class);

	protected Integer id = null;

	private UploadableMedia document = new UploadableMedia();

	/**
	 * CTOR
	 */
	public UploadableMediaDelegate(UploadableMediaDelegate dto) {
		try {
			BeanUtils.copyProperties(this, dto);
		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}
	}

	public UploadableMediaDelegate() {
	}

	/**
	 * Accessor for id
	 * 
	 * @return Integer
	 * @hibernate.id generator-class="increment" unsaved-value="-1"
	 *               type="java.lang.Integer" unique="true" insert="true"
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@PropertyDescriptor(readOnly = true, summary = true, index = 0)
	public Integer getId() {
		return id;
	}

	@BlobDescriptor(renderType = RenderType.LINK, contentDisposition = ContentDisposition.ATTACHMENT)
	@PropertyDescriptor(summary = true, index = 1)
	@OneToOne(cascade = CascadeType.ALL)
	public UploadableMedia getDocument() {
		return document;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setDocument(UploadableMedia document) {
		this.document = document;
	}

	@Override
	public UploadableMediaDelegate clone() {
		return new UploadableMediaDelegate(this);
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object rhs) {
		if (this == rhs)
			return true;
		if (rhs == null)
			return false;
		if (!(rhs instanceof UploadableMediaDelegate))
			return false;
		final UploadableMediaDelegate castedObject = (UploadableMediaDelegate) rhs;
		if (getId() == null) {
			if (castedObject.getId() != null)
				return false;
		} else if (!getId().equals(castedObject.getId()))
			return false;
		return true;
	}
}