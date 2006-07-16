/**
 * $Author: alejandroscandroli $
 * $Id: User.java,v 1.1 2006/01/16 11:43:37 alejandroscandroli Exp $
 */

package org.trails.security.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.validator.NotNull;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.security.RestrictionType;
import org.trails.security.annotation.Restriction;
import org.trails.security.annotation.Security;
import org.trails.validation.ValidateUniqueness;

@Entity
@Table(name="TRAILS_USER") 
@ValidateUniqueness(property = "username")
@Security(restrictions = {@Restriction(restrictionType = RestrictionType.VIEW, 
		requiredRole = "ROLE_MANAGER")})
public class User implements Serializable
{

    private Integer id;
    private String username;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private Integer version;
    private Set<Role> roles = new HashSet<Role>();
    private boolean enabled = true;
    private boolean accountExpired = false;
    private boolean accountLocked = false;
    private boolean credentialsExpired = false;

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

    @Column(name = "username", length = 20, unique = true, nullable = false)
    @NotNull
    @PropertyDescriptor(index = 1)
    public String getUsername()
    {
        return username;
    }

    @Column(name = "password", nullable = false)
    @NotNull
    @PropertyDescriptor(index = 2, summary=false)
    public String getPassword()
    {
        return password;
    }

//    @Transient

    @NotNull
//para la validación // for validation porpuoses
    @PropertyDescriptor(index = 3, summary=false)
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

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_ID")},
            inverseJoinColumns = {@JoinColumn(name = "role_ID")})
    public Set<Role> getRoles()
    {
        return roles;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
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

    public boolean getEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean getAccountExpired()
    {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired)
    {
        this.accountExpired = accountExpired;
    }

    public boolean getAccountLocked()
    {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked)
    {
        this.accountLocked = accountLocked;
    }

    public boolean getCredentialsExpired()
    {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired)
    {
        this.credentialsExpired = credentialsExpired;
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
        if (!(o instanceof User)) return false;

        final User user = (User) o;

        if (username != null ? !username.equals(user.getUsername()) : user.getUsername() != null) return false;

        return true;
    }

    public int hashCode()
    {
        return (username != null ? username.hashCode() : 0);
    }

    public String toString()
    {
        return getFirstName() + " " + getLastName() + "(" + getUsername() + ")";
    }
}