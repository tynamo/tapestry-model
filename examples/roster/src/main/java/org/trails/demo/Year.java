package org.trails.demo;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.util.DatePattern;

import javax.persistence.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author kenneth.colassi    nhhockeyplayer@hotmail.com
 */
@Entity
public class Year {

    private static final Log log = LogFactory.getLog(Year.class);

    private Integer id = null;

    private Organization organization;

    private Integer yearStart;

    private Integer yearEnd;

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
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @PropertyDescriptor(index = 0)
    public Integer getId() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "year_organization_fk")
    public Organization getOrganization() {
        return organization;
    }

    @PropertyDescriptor(index = 1)
    public Integer getYearStart() {
        return yearStart;
    }

    @PropertyDescriptor(index = 2)
    public Integer getYearEnd() {
        return yearEnd;
    }

    @PropertyDescriptor(hidden = true)
    public Long getCreated() {
        return created;
    }

    @PropertyDescriptor(hidden = true)
    public Long getAccessed() {
        return accessed;
    }

    @Transient
    @PropertyDescriptor(hidden = true)
    public String getCreatedAsString() {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(created.longValue());
        return DatePattern.sdf.format(cal.getTime());
    }

    @Transient
    @PropertyDescriptor(hidden = true)
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
    @PropertyDescriptor(hidden = true)
    public void setCreatedAsString(String value) throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(DatePattern.sdf.parse(value).getTime());
        this.created = new Long(cal.getTimeInMillis());
    }

    @Transient
    @PropertyDescriptor(hidden = true)
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
        result = PRIME * result + ((id == null) ? 0 : id.hashCode());
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
        if (id == null) {
            if (castedObject.id != null)
                return false;
        } else if (!id.equals(castedObject.id))
            return false;
        return true;
    }

    public String toString() {
        return getYearStart().toString() + "/" + getYearEnd().toString();
    }
}