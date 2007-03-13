package org.trails.demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.PropertyDescriptor;

/**
 * @hibernate.class table="Director" lazy="true"
 *
 * A Director belongs to an organization
 *
 * @author kenneth.colassi
 */
@Entity
@ClassDescriptor(hasCyclicRelationships = true)
public class Director extends Person {
    private static final Log log = LogFactory.getLog(Director.class);

    private Organization organization;

    /**
     * CTOR
     */
    public Director(Director dto) {
        super(dto);

        try {
            BeanUtils.copyProperties(this, dto);
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
        }
    }

    public Director() {
        setERole(ERole.USER);
    }

    /**
     * Accessor for id
     *
     * @return Integer
     * @hibernate.id generator-class="increment" unsaved-value="-1"
     *               type="java.lang.Integer" unique="true" insert="false"
     *               update="false"
     */
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @PropertyDescriptor(readOnly = true, summary = true, index = 0)
    public Integer getId() {
        return super.getId();
    }

    /**
     * @hibernate.property
     */
    @OneToOne
    @JoinTable(name = "OrganizationsDirectors",
            joinColumns = @JoinColumn(name = "organization_fk"),
            inverseJoinColumns = {@JoinColumn(name = "director_fk")}
    )
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public Director clone() {
        return new Director(this);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object rhs) {
        if (this == rhs)
            return true;
        if (rhs == null)
            return false;
        if (!(rhs instanceof Director))
            return false;
        final Director castedObject = (Director) rhs;
        if (id == null) {
            if (castedObject.id != null)
                return false;
        } else if (!id.equals(castedObject.id))
            return false;
        return true;
    }
}
