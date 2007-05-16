package org.trails.demo;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.PropertyDescriptor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * A Coach belongs to an organization and has a team
 *
 * @author kenneth.colassi        nhhockeyplayer@hotmail.com
 */
@Entity
@ClassDescriptor(hasCyclicRelationships = true, hidden = true)
public class Coach extends Person {
    private static final Log log = LogFactory.getLog(Coach.class);

    protected enum ETeamRole {
        HEADCOACH, ASSISTANTCOACH, MANAGER, EQUIPMENTMGR, OPERATIONS, TRAINER
    }

    private ETeamRole    role;

    private Team team = null;

    private Organization organization = null;

    /**
     * CTOR
     */
    public Coach(Coach dto) {
        super(dto);

        try {
            BeanUtils.copyProperties(this, dto);
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
        }
    }

    public Coach() {
        setERole(ERole.USER);
    }

    public ETeamRole getRole() {
        return role;
    }

    @ManyToOne
    @JoinColumn(name = "team_id")
    @PropertyDescriptor(searchable = true, index = 1)
    public Team getTeam() {
        return team;
    }

    @ManyToOne
    @JoinColumn(name = "coach_organization_fk", insertable = false, updatable = true, nullable = true)
    public Organization getOrganization() {
        return organization;
    }

    public void setRole(ETeamRole role) {
        this.role = role;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        //return this.getApplicationRole().toString() + " - " + getLastName() + "," + getFirstName();
        return getLastName() + "," + getFirstName();
    }

    @Override
    public Coach clone() {
        return new Coach(this);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object rhs) {
        if (this == rhs)
            return true;
        if (rhs == null)
            return false;
        if (!(rhs instanceof Coach))
            return false;
        final Coach castedObject = (Coach) rhs;
        if (getId() == null) {
            if (castedObject.getId() != null)
                return false;
        } else if (!getId().equals(castedObject.getId()))
            return false;
        return true;
    }
}