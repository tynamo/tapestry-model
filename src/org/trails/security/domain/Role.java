/**
 * $Author: alejandroscandroli $
 * $Id: Role.java,v 1.1 2006/01/16 11:43:37 alejandroscandroli Exp $
 */

package org.trails.security.domain;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.security.RestrictionType;
import org.trails.security.annotation.Restriction;
import org.trails.security.annotation.Security;
import org.trails.validation.ValidateUniqueness;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@ValidateUniqueness(property = "name")
@Security(restrictions = {@Restriction(restrictionType = RestrictionType.VIEW, requiredRole = "ROLE_MANAGER")})
public class Role implements Serializable
{

    private Integer id;
    private String name;
    private String description;
    private Integer version;
    private Set<User> users = new HashSet<User>();


    @Id(generate = GeneratorType.AUTO)
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
//    @Id(generate = GeneratorType.NONE)
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

    @ManyToMany
    @JoinTable(
            table = @Table(name = "user_role"),
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
        return (id != null ? id.hashCode() : 0);
    }

    public String toString()
    {
        return name;
    }

}
