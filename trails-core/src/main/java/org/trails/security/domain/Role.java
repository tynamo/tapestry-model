package org.trails.security.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.acegisecurity.GrantedAuthority;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.security.RestrictionType;
import org.trails.security.annotation.Restriction;
import org.trails.security.annotation.Security;
import org.trails.validation.ValidateUniqueness;

@Entity
@Table(name="TRAILS_ROLE") 
@ValidateUniqueness(property = "name")
@Security(restrictions = {@Restriction(restrictionType = RestrictionType.VIEW, requiredRole = "ROLE_MANAGER")})
@ClassDescriptor(hasCyclicRelationships = true)
public class Role implements GrantedAuthority, Serializable
{

    private Integer id;
    private String name;
    private String description;
    private Integer version;
    private Set<User> users = new HashSet<User>();


    @Id @GeneratedValue(strategy = GenerationType.AUTO)
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

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "role_ID")},
            inverseJoinColumns = {@JoinColumn(name = "user_ID")})
    public Set<User> getUsers()
    {
        return users;
    }


    public void setUsers(Set<User> users)
    {
        this.users = users;
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
    public String getAuthority() {
        return getName();
    }
}
