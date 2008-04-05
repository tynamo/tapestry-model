/**
 * $Author: alejandroscandroli $
 * $Id: User.java,v 1.1 2006/01/16 11:43:37 alejandroscandroli Exp $
 */

package org.trails.demo;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.NotNull;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.security.annotation.UpdateRequiresAssociation;
import org.trails.security.annotation.UpdateRequiresRole;
import org.trails.security.annotation.ViewRequiresAssociation;
import org.trails.security.annotation.ViewRequiresRole;
import org.trails.security.password.DigestUtil;
import org.trails.validation.ValidateUniqueness;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TRAILS_USER")
@ValidateUniqueness(property = "username")
@ViewRequiresRole( { "ROLE_MANAGER", "ROLE_ROOT" })
@ViewRequiresAssociation
@UpdateRequiresAssociation
public class User implements UserDetails
{
	private static final Log log = LogFactory.getLog(User.class);

	private Integer id;

	private String username;

	private String encodedPassword;

	private String confirmPassword;

	private String firstName;

	private String lastName;

	private Integer version;

	private Set<Role> roles = new HashSet<Role>();

	private boolean enabled = true;

	private boolean accountNonExpired = true;

	private boolean accountNonLocked = true;

	private boolean credentialsNonExpired = true;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@PropertyDescriptor(index = 0)
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	@Column(name = "username", length = 20, unique = true, nullable = false)
	@NotNull
	@PropertyDescriptor(index = 1)
	public String getUsername()
	{
		return username;
	}

	@Transient
	@NotNull
	@PropertyDescriptor(index = 2, summary = false)
	public String getPassword()
	{
		return encodedPassword;
	}

	public void setPassword(String password)
	{
		if (password != null && !password.equals(encodedPassword))
			encodedPassword = DigestUtil.encode(password);
	}

	@Column(name = "password", nullable = false)
	@PropertyDescriptor(hidden = true)
	public String getEncodedPassword()
	{
		return encodedPassword;
	}

	public void setEncodedPassword(String encodedPassword)
	{
		this.encodedPassword = encodedPassword;
	}

	// @Transient
	@NotNull
	// for validation purposes
	@PropertyDescriptor(index = 3, summary = false)
	public String getConfirmPassword()
	{
		return confirmPassword;
	}

	@Column(name = "first_name", length = 50, nullable = false)
	@NotNull
	@PropertyDescriptor(index = 4)
	public String getFirstName()
	{
		return firstName;
	}

	@Column(name = "last_name", length = 50, nullable = false)
	@NotNull
	@PropertyDescriptor(index = 5)
	public String getLastName()
	{
		return lastName;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@UpdateRequiresRole( { "ROLE_MANAGER" })
	public Set<Role> getRoles()
	{
		return roles;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public void setConfirmPassword(String confirmPassword)
	{
		this.confirmPassword = confirmPassword;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public void setRoles(Set<Role> roles)
	{
		this.roles = roles;
	}

	@Version
	@PropertyDescriptor(hidden = true)
	public Integer getVersion()
	{
		return version;
	}

	public void setVersion(Integer version)
	{
		this.version = version;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof User))
			return false;

		final User user = (User) o;

		if (username != null ? !username.equals(user.getUsername()) : user.getUsername() != null)
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		return (username != null ? username.hashCode() : 0);
	}

	public String toString()
	{
		return getFirstName() + " " + getLastName() + "(" + getUsername() + ")";
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

	@Transient
	@PropertyDescriptor(hidden = true)
	public GrantedAuthority[] getAuthorities()
	{
		log.debug("User " + getUsername() + " has roles " + roles);
		if (roles == null || roles.size() == 0)
			throw new UsernameNotFoundException("User has no GrantedAuthority");
		return roles.toArray(new GrantedAuthority[roles.size()]);
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

}