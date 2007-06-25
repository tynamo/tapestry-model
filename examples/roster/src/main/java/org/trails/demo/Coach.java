package org.trails.demo;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.security.annotation.RemoveRequiresRole;
import org.trails.security.annotation.UpdateRequiresRole;
import org.trails.security.annotation.ViewRequiresRole;

/**
 * A Coach belongs to an organization and has a team
 * 
 * @author kenneth.colassi nhhockeyplayer@hotmail.com
 */
@Entity
@RemoveRequiresRole({"ROLE_ADMIN", "ROLE_MANAGER"})
@UpdateRequiresRole({"ROLE_ADMIN", "ROLE_MANAGER"})
@ViewRequiresRole({"ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER"})
@ClassDescriptor(hasCyclicRelationships = true, hidden = true)
public class Coach extends Person
{
	private static final Log log = LogFactory.getLog(Coach.class);

	protected enum ETeamRole
	{
		HEADCOACH, ASSISTANTCOACH, MANAGER, EQUIPMENTMGR, OPERATIONS, TRAINER
	}

	private ETeamRole eTeamRole;

	private Team team = null;

	private Organization organization = null;

	/**
	 * CTOR
	 */
	public Coach(Coach dto)
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

	public Coach()
	{
		setERole(ERole.USER);
	}

	@Enumerated(value = EnumType.STRING)
	@PropertyDescriptor(summary = true)
	public ETeamRole getETeamRole()
	{
		return eTeamRole;
	}

	@ManyToOne
	@JoinColumn(name = "team_id")
	@PropertyDescriptor(searchable = true, index = 1)
	public Team getTeam()
	{
		return team;
	}

	@ManyToOne
	@JoinColumn(name = "coach_organization_fk", insertable = false, updatable = true, nullable = true)
	public Organization getOrganization()
	{
		return organization;
	}

	public void setETeamRole(ETeamRole role)
	{
		this.eTeamRole = role;
	}

	public void setTeam(Team team)
	{
		this.team = team;
	}

	public void setOrganization(Organization organization)
	{
		this.organization = organization;
	}

	@Override
	public Coach clone()
	{
		return new Coach(this);
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
		if (!(rhs instanceof Coach))
			return false;
		final Coach castedObject = (Coach) rhs;
		if (getId() == null)
		{
			if (castedObject.getId() != null)
				return false;
		} else if (!getId().equals(castedObject.getId()))
			return false;
		return true;
	}
}