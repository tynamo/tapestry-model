package org.trails.demo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.NotNull;
import org.trails.descriptor.BlobDescriptorExtension.ContentDisposition;
import org.trails.descriptor.BlobDescriptorExtension.RenderType;
import org.trails.descriptor.annotation.BlobDescriptor;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.security.annotation.RemoveRequiresRole;
import org.trails.security.annotation.UpdateRequiresRole;
import org.trails.security.annotation.ViewRequiresRole;
import org.trails.util.DatePattern;
import org.trails.validation.ValidateUniqueness;

/**
 * A Person has a photo, eRole and application role
 * 
 * @author kenneth.colassi nhhockeyplayer@hotmail.com
 */
@Entity
@RemoveRequiresRole( { "ROLE_ADMIN", "ROLE_MANAGER" })
@UpdateRequiresRole( { "ROLE_ADMIN", "ROLE_MANAGER" })
@ViewRequiresRole( { "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER" })
@ValidateUniqueness(property = "emailAddress")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// @Inheritance(strategy = InheritanceType.JOINED)
// @MappedSuperclass
@ClassDescriptor(hidden = true)
public class Person implements UserDetails, Cloneable, Serializable
{
	private static final Log log = LogFactory.getLog(Person.class);

	public enum ERole
	{
		USER, ADMIN
	}

	public enum EApplicationRole
	{
		MANAGER, DIRECTOR, SALES, MARKETING
	}

	protected Integer id = null;

	protected String firstName;

	protected String lastName;

	protected Demographics demographics = new Demographics();

	protected Date dob;

	protected String emailAddress;

	protected String password;

	protected ERole eRole;

	protected EApplicationRole eApplicationRole;

	protected Set<Role> roles = new HashSet<Role>();

	protected boolean accountNonExpired = true;

	protected boolean accountNonLocked = true;

	protected boolean credentialsNonExpired = true;

	protected boolean enabled = true;

	protected Long created = new Long(GregorianCalendar.getInstance().getTimeInMillis());

	protected Long accessed = new Long(GregorianCalendar.getInstance().getTimeInMillis());

	/**
	 * CTOR
	 */
	public Person(Person dto)
	{
		try
		{
			BeanUtils.copyProperties(this, dto);
			setUsername(emailAddress);
		} catch (Exception e)
		{
			log.error(e.toString());
			e.printStackTrace();
		}
	}

	public Person()
	{
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
	public Integer getId()
	{
		return id;
	}

	private UploadableMedia photo = new UploadableMedia();

	@BlobDescriptor(renderType = RenderType.IMAGE, contentDisposition = ContentDisposition.ATTACHMENT)
	@PropertyDescriptor(summary = true, index = 1)
	@OneToOne(cascade = CascadeType.ALL)
	public UploadableMedia getPhoto()
	{
		return photo;
	}

	@Enumerated(value = EnumType.STRING)
	@NotNull(message = "is required")
	@PropertyDescriptor(summary = true, index = 2)
	public ERole getERole()
	{
		return eRole;
	}

	@Enumerated(value = EnumType.STRING)
	@PropertyDescriptor(summary = true, index = 9)
	public EApplicationRole getEApplicationRole()
	{
		return eApplicationRole;
	}

	@PropertyDescriptor(summary = true, index = 3)
	@NotNull
	public String getFirstName()
	{
		return firstName;
	}

	@PropertyDescriptor(summary = true, index = 4)
	@NotNull
	public String getLastName()
	{
		return lastName;
	}

	@PropertyDescriptor(summary = true, index = 5)
	public Date getDob()
	{
		return dob;
	}

	@Embedded
	public Demographics getDemographics()
	{
		return demographics;
	}

	@Column(unique = true)
	@PrimaryKeyJoinColumn
	@PropertyDescriptor(summary = true, index = 7)
	public String getEmailAddress()
	{
		return emailAddress;
	}

	@PropertyDescriptor(summary = true, index = 8)
	public String getPassword()
	{
		return password;
	}

	@PropertyDescriptor(hidden = true, summary = false, searchable = false)
	public Long getCreated()
	{
		return created;
	}

	@PropertyDescriptor(hidden = true, summary = false, searchable = false)
	public Long getAccessed()
	{
		return accessed;
	}

	@Transient
	@PropertyDescriptor(hidden = true, summary = false, searchable = false)
	public String getCreatedAsString()
	{
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(created.longValue());
		return DatePattern.sdf.format(cal.getTime());
	}

	@Transient
	@PropertyDescriptor(hidden = true, summary = false, searchable = false)
	public String getAccessedAsString()
	{
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(accessed.longValue());
		return DatePattern.sdf.format(cal.getTime());
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public void setPhoto(UploadableMedia photo)
	{
		this.photo = photo;
	}

	public void setERole(ERole role)
	{
		this.eRole = role;
	}

	public void setEApplicationRole(EApplicationRole applicationRole)
	{
		eApplicationRole = applicationRole;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public void setDob(Date dob)
	{
		this.dob = dob;
	}

	public void setDemographics(Demographics demographics)
	{
		this.demographics = demographics;
	}

	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
		setUsername(emailAddress);
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setCreated(Long created)
	{
		this.created = created;
	}

	@Transient
	@PropertyDescriptor(hidden = true, summary = false, searchable = false)
	public void setCreatedAsString(String value) throws Exception
	{
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(DatePattern.sdf.parse(value).getTime());
		this.created = new Long(cal.getTimeInMillis());
	}

	public void setAccessed(Long accessed)
	{
		this.accessed = accessed;
	}

	@Transient
	@PropertyDescriptor(hidden = true, summary = false, searchable = false)
	public void setAccessedAsString(String value) throws Exception
	{
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(DatePattern.sdf.parse(value).getTime());
		this.accessed = new Long(cal.getTimeInMillis());
	}

	@Override
	public Person clone()
	{
		return new Person(this);
	}

	@Override
	public String toString()
	{
		return getFirstName() + " " + getLastName() + " [" + getUsername() + "]";
	}

	@Override
	public int hashCode()
	{
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object rhs)
	{
		if (this == rhs)
			return true;
		if (rhs == null)
			return false;
		if (!(rhs instanceof Person))
			return false;
		final Person castedObject = (Person) rhs;
		if (getId() == null)
		{
			if (castedObject.getId() != null)
				return false;
		} else if (!getId().equals(castedObject.getId()))
			return false;
		return true;
	}

	public boolean isAccountNonExpired()
	{
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired)
	{
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked()
	{
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked)
	{
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired()
	{
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired)
	{
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	@Transient
	@PropertyDescriptor(hidden = true)
	public GrantedAuthority[] getAuthorities()
	{
		log.debug("Person " + getUsername() + " has roles " + roles);
		if (roles == null || roles.size() == 0)
			throw new UsernameNotFoundException("Person has no GrantedAuthority");
		return roles.toArray(new GrantedAuthority[roles.size()]);
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "join_table_person_role", joinColumns = { @JoinColumn(name = "person_fk") }, inverseJoinColumns = { @JoinColumn(name = "role_fk") })
	public Set<Role> getRoles()
	{
		return roles;
	}

	public void setRoles(Set<Role> roles)
	{
		this.roles = roles;
	}

	@PropertyDescriptor(hidden = true)
	public String getUsername()
	{
		return emailAddress;
	}

	public void setUsername(String username)
	{
		this.emailAddress = username;
	}
}