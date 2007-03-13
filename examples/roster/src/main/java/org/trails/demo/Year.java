package org.trails.demo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.util.DatePattern;

/**
 * @hibernate.class table="Year" lazy="true"
 *
 * @author kenneth.colassi
 */
@Entity
@ClassDescriptor(hasCyclicRelationships = true)
public class Year implements Serializable {
    private static final Log log = LogFactory.getLog(Year.class);

    private Integer id = null;

    private Organization organization;

    private Integer yearStart = new Integer("0");

    private Integer yearEnd = new Integer("0");

    private Long created = new Long(GregorianCalendar.getInstance()
            .getTimeInMillis());

    private Long accessed = new Long(GregorianCalendar.getInstance()
            .getTimeInMillis());

    /**
     * CTOR
     */

    public Year() {
    }

    public Year(Year dto) {
        try {
            BeanUtils.copyProperties(this, dto);
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Accessor for id
     *
     * @return Integer
     * @hibernate.id generator-class="increment" unsaved-value="-1"
     *               type="java.lang.Integer" unique="true" insert="true"
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @PropertyDescriptor(readOnly = true, summary = true, index = 0)
    public Integer getId() {
        return id;
    }

    /**
     * @hibernate.property
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", insertable = false, updatable = true, nullable = true)
    public Organization getOrganization() {
        return organization;
    }

    /**
     * @hibernate.property
     */
    @PropertyDescriptor(index = 1)
    public Integer getYearStart() {
        return yearStart;
    }

    /**
     * @hibernate.property
     */
    @PropertyDescriptor(index = 2)
    public Integer getYearEnd() {
        return yearEnd;
    }

    /**
     * @hibernate.property
     */
    @PropertyDescriptor(hidden = true, summary = false, searchable = false)
    public Long getCreated() {
        return created;
    }

    /**
     * @hibernate.property
     */
    @PropertyDescriptor(hidden = true, summary = false, searchable = false)
    public Long getAccessed() {
        return accessed;
    }

    @Transient
    @PropertyDescriptor(hidden = true, summary = false, searchable = false)
    public String getCreatedAsString() {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(created.longValue());
        return DatePattern.sdf.format(cal.getTime());
    }

    @Transient
    @PropertyDescriptor(hidden = true, summary = false, searchable = false)
    public String getAccessedAsString() {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(accessed.longValue());
        return DatePattern.sdf.format(cal.getTime());
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public void setYearStart(Integer yearStart) {
        this.yearStart = yearStart;
    }

    public void setYearEnd(Integer yearEnd) {
        this.yearEnd = yearEnd;
    }

    @Transient
    @PropertyDescriptor(hidden = true, summary = false, searchable = false)
    public void setCreatedAsString(String value) throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(DatePattern.sdf.parse(value).getTime());
        this.created = new Long(cal.getTimeInMillis());
    }

    @Transient
    @PropertyDescriptor(hidden = true, summary = false, searchable = false)
    public void setAccessedAsString(String value) throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(DatePattern.sdf.parse(value).getTime());
        this.accessed = new Long(cal.getTimeInMillis());
    }

    public void setAccessed(Long accessed) {
        this.accessed = accessed;
    }

    public void setCreated(Long created) {
        this.created = created;
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
        if (!(rhs instanceof Year))
            return false;
        final Year castedObject = (Year) rhs;
        if (getId() == null) {
            if (castedObject.getId() != null)
                return false;
        } else if (!getId().equals(castedObject.getId()))
            return false;
        return true;
    }

    public String toString() {
        return getYearStart().toString() + "/" + getYearEnd().toString();
    }
}