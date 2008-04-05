package org.trails.demo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.acegisecurity.GrantedAuthority;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.descriptor.annotation.Collection;
import org.trails.security.annotation.ViewRequiresRole;
import org.trails.validation.ValidateUniqueness;

@Entity
@Table(name = "TRAILS_ROLE")
@ValidateUniqueness(property = "name")
@ViewRequiresRole("ROLE_MANAGER")
public class Role implements GrantedAuthority
{

	private Integer id;

	private String name;

	private String description;

	private Integer version;

	private Set<User> users = new HashSet<User>();

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

	@PropertyDescriptor(index = 1)
	@Length(min = 1, max = 20)
	@NotNull
	// @Id @GeneratedValue(strategy = GenerationType.NONE)
	public String getName()
	{
		return this.name;
	}

	@Length(max = 250)
	@NotNull
	public String getDescription()
	{
		return this.description;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	@ManyToMany(mappedBy = "roles")
	public Set<User> getUsers()
	{
		return users;
	}

	public void setUsers(Set<User> users)
	{
		this.users = users;
	}

	public void addUsers(User user)
	{
		user.getRoles().add(this);
		users.add(user);
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

	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final Role role = (Role) o;

		if (id != null ? !id.equals(role.id) : role.id != null)
			return false;

		return true;
	}

	public int hashCode()
	{
		return (id != null ? id.hashCode() : super.hashCode());
	}

	public String toString()
	{
		return name;
	}

	@Transient
	@PropertyDescriptor(hidden = true)
	public String getAuthority()
	{
		return getName();
	}
}
