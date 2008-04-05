package org.trails.demo;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.NotNull;
import org.trails.descriptor.annotation.BlobDescriptor;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.descriptor.extension.BlobDescriptorExtension.ContentDisposition;
import org.trails.descriptor.extension.BlobDescriptorExtension.RenderType;
import org.trails.security.annotation.RemoveRequiresRole;
import org.trails.security.annotation.UpdateRequiresRole;
import org.trails.security.annotation.ViewRequiresRole;
import org.trails.validation.ValidateUniqueness;

import javax.persistence.*;
import java.util.*;

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
@ClassDescriptor(hidden = true)
@Inheritance(strategy = InheritanceType.JOINED)
public class Person implements Cloneable
/**
 * TrailsUserDAO expects one and only one implementation of UserDetails
 * If you need more then one implementation you need to create your own UserDetailsService  
 */
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

	private Integer id = null;

	private String firstName;

	private String lastName;

	private Demographics demographics = new Demographics();

	private Date dob;

	private String emailAddress;

	private String password;

	private ERole eRole;

	private EApplicationRole eApplicationRole;

	private Set<Role> roles = new HashSet<Role>();

	private boolean accountNonExpired = true;

	private boolean accountNonLocked = true;

	private boolean credentialsNonExpired = true;

	private boolean enabled = true;

	private Long created = new Long(GregorianCalendar.getInstance().getTimeInMillis());

	private Long accessed = new Long(GregorianCalendar.getInstance().getTimeInMillis());

	/**
	 * CTOR
	 */
	public Person(Person dto)
	{
		try
		{
			BeanUtils.copyProperties(this, dto);
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
	public String getFirstName()
	{
		return firstName;
	}

	@PropertyDescriptor(summary = true, index = 4)
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
		return (getId() != null ? getId().hashCode() : 0);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof Person)) return false;

		Person person = (Person) o;

		return getId() != null ? getId().equals(person.getId()) : person.getId() == null;
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

	@ManyToMany
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
	@Transient
	public String getUsername()
	{
		return emailAddress;
	}
}