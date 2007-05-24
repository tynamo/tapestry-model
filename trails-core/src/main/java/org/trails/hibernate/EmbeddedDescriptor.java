package org.trails.hibernate;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IDescriptor;
import org.trails.descriptor.IPropertyDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;

public class EmbeddedDescriptor extends TrailsClassDescriptor implements
		IPropertyDescriptor {

	private int index;

	private boolean readOnly;

	private String name;

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////
	// constructors
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////

	public EmbeddedDescriptor(Class beanType, String displayName, Class type) {
		super(type, displayName);
		this.beanType = beanType;
	}

	public EmbeddedDescriptor(Class beanType, IClassDescriptor descriptor) {
		super(descriptor);
		this.beanType = beanType;
	}

	/**
	 * 
	 * @param dto
	 */
	public EmbeddedDescriptor(EmbeddedDescriptor dto) {
		super(dto);

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

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////
	// methods
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////
	// bean setters/getters
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////
	public boolean isNumeric() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isBoolean() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isDate() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isString() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isObjectReference() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isOwningObjectReference() {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean required;

	private int length;

	private boolean large;

	private String format;

	private boolean searchable;

	private boolean summary;

	private boolean richText;

	private Class beanType;

	public boolean isEmbedded() {
		return true;
	}

	public Class getPropertyType() {
		return getType();
	}

	public void setPropertyType(Class propertyType) {
		setType(propertyType);
	}

	public int getIndex() {
		return index;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCollection() {
		return false;
	}

	public IClassDescriptor getParentClassDescriptor() {
		return null;// To change body of implemented methods use File | Settings
					// | File Templates.
	}

	public void setParentClassDescriptor(IClassDescriptor parent) {
		// To change body of implemented methods use File | Settings | File
		// Templates.
	}

	@Override
	public Object clone() {
		// return new EmbeddedDescriptor(getBeanType(), this);
		return new EmbeddedDescriptor(this);
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public boolean isLarge() {
		return large;
	}

	public void setLarge(boolean large) {
		this.large = large;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public boolean isSummary() {
		return summary;
	}

	public void setSummary(boolean summary) {
		this.summary = summary;
	}

	public Class getBeanType() {
		return beanType;
	}

	public void setBeanType(Class beanType) {
		this.beanType = beanType;
	}

	public boolean isRichText() {
		return richText;
	}

	public void setRichText(boolean richText) {
		this.richText = richText;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	@Override
	public void copyFrom(IDescriptor descriptor) {
		super.copyFrom(descriptor);

		if (descriptor instanceof EmbeddedDescriptor) {
			try {
				BeanUtils.copyProperties(this, (EmbeddedDescriptor) descriptor);
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
	}

	public boolean isIdentifier() {
		return false;
	}
}
