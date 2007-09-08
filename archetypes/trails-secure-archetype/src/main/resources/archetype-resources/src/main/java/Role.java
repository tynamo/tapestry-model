package sample;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import org.acegisecurity.GrantedAuthority;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.security.annotation.ViewRequiresRole;
import org.trails.validation.ValidateUniqueness;

@Entity
@Table(name = "TRAILS_ROLE")
@ValidateUniqueness(property = "name")
@ViewRequiresRole("ROLE_MANAGER")
@ClassDescriptor(hasCyclicRelationships = true)
public class Role implements GrantedAuthority, Serializable
{

	private Integer id;
	private String name;

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
//    @Id @GeneratedValue(strategy = GenerationType.NONE)
	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final Role role = (Role) o;

		if (id != null ? !id.equals(role.id) : role.id != null) return false;

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
