package org.trails.demo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.HardOneToOne;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.descriptor.annotation.HardOneToOne.Identity;
import org.trails.security.annotation.RemoveRequiresRole;
import org.trails.security.annotation.UpdateRequiresRole;
import org.trails.security.annotation.ViewRequiresRole;

/**
 * A Director belongs to an Organization
 * 
 * @author kenneth.colassi nhhockeyplayer@hotmail.com
 */
@Entity
@RemoveRequiresRole({"ROLE_ADMIN", "ROLE_MANAGER"})
@UpdateRequiresRole({"ROLE_ADMIN", "ROLE_MANAGER"})
@ViewRequiresRole({"ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER"})
public class Director extends Person
{
	private static final Log log = LogFactory.getLog(Director.class);

	private Organization organization;

	/**
	 * Copy CTOR
	 */
	public Director(Director dto)
	{
		super(dto);

		try
		{
			BeanUtils.copyProperties(this, dto);
		} catch (Exception e)
		{
			log.error(e.toString());
			e.printStackTrace();
		}
	}

	public Director()
	{
		setERole(ERole.USER);
		setEApplicationRole(EApplicationRole.DIRECTOR);
	}

	@OneToOne(mappedBy = "director")
	@HardOneToOne(identity = Identity.ASSOCIATION)
	@PropertyDescriptor(readOnly = true)
	public Organization getOrganization()
	{
		return organization;
	}

	public void setOrganization(Organization organization)
	{
		this.organization = organization;
	}

	@Override
	public Director clone()
	{
		return new Director(this);
	}
}
