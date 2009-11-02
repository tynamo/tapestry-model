package org.tynamo.security;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.tynamo.descriptor.annotation.ClassDescriptor;

@Entity
@ClassDescriptor(hidden = true)
public class ExpiringKey {
	private String name;
	private String value;
	private Date expiresAfter = new Date();
	private Integer id;
	
	ExpiringKey() {}
	
	ExpiringKey(String name, String value, Date expiresAfter) {
		this();
		this.name = name;
		this.value = value;
		setExpiresAfter(expiresAfter);
	}
	
  /* (non-Javadoc)
	 * @see org.tynamo.security.ExpiringKey#getId()
	 */
  @Id @GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.tynamo.security.ExpiringKey#setId(java.lang.Integer)
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see org.tynamo.security.ExpiringKey#getValue()
	 */
	public String getValue() {
		return value;
	}
	/* (non-Javadoc)
	 * @see org.tynamo.security.ExpiringKey#setValue(java.lang.String)
	 */
	public void setValue(String token) {
		this.value = token;
	}
	/* (non-Javadoc)
	 * @see org.tynamo.security.ExpiringKey#getName()
	 */
	public String getName() {
		return name;
	}
	/* (non-Javadoc)
	 * @see org.tynamo.security.ExpiringKey#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see org.tynamo.security.ExpiringKey#getExpiresAfter()
	 */
	public Date getExpiresAfter() {
		return expiresAfter;
	}

	/* (non-Javadoc)
	 * @see org.tynamo.security.ExpiringKey#setExpiresAfter(java.util.Date)
	 */
	public void setExpiresAfter(Date expiresAfter) {
		if (expiresAfter != null) this.expiresAfter = expiresAfter;
	}
}
