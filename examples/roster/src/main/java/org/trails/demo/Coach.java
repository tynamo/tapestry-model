package org.trails.demo;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.descriptor.annotation.PropertyDescriptor;

import javax.persistence.*;

/**
 *
 * A Coach belongs to an organization and has a team
 *
 * @author kenneth.colassi	nhhockeyplayer@hotmail.com
 */
@Entity
public class Coach extends Person {

    private static final Log log = LogFactory.getLog(Coach.class);

    private Team team;

    private Organization organization;

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

    @ManyToOne
    @JoinColumn(name = "team_id")
    @PropertyDescriptor(index = 1)
    public Team getTeam() {
        return team;
    }

    @ManyToOne
    @JoinColumn(name = "coach_organization_fk")
    public Organization getOrganization() {
        return organization;
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