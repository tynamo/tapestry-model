package org.trails.demo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.NotNull;
import org.trails.descriptor.BlobDescriptorExtension.ContentDisposition;
import org.trails.descriptor.BlobDescriptorExtension.RenderType;
import org.trails.descriptor.annotation.BlobDescriptor;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.Collection;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.util.DatePattern;
import org.trails.validation.ValidateUniqueness;


/**
 * Leagues have Years, Officers, Organizations and Demographics
 *
 * @author kenneth.colassi        nhhockeyplayer@hotmail.com
 */
@Entity
@ValidateUniqueness(property = "name")
@ClassDescriptor(hasCyclicRelationships=true)
public class League implements Serializable {
    private static final Log log = LogFactory.getLog(League.class);

    private Integer id = null;

    private Set<TeamYear> years = new HashSet<TeamYear>();

    private Set<Officer> officers = new HashSet<Officer>();

    private Set<Organization> organizations = new HashSet<Organization>();

    private String name;

    private String abbreviation;

    private Demographics demographics = new Demographics();

    private Long created = new Long(GregorianCalendar.getInstance()
            .getTimeInMillis());

    private Long accessed = new Long(GregorianCalendar.getInstance()
            .getTimeInMillis());

    /**
     * CTOR
     */
    public League() {
    }

    public League(League dto) {
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "league_id", insertable = true, updatable = true, nullable = true)
    @Collection(child = true, inverse = "league")
    @PropertyDescriptor(readOnly = false, index = 1)
    @OrderBy("yearStart")
    public Set<TeamYear> getYears() {
        return years;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "officer_league_fk", insertable = true, updatable = true, nullable = true)
    @Collection(child = true, inverse = "league")
    @PropertyDescriptor(readOnly = false, searchable = true)
    @OrderBy("lastName")
    public Set<Officer> getOfficers() {
        return officers;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "organization_league_fk", insertable = true, updatable = true, nullable = true)
    @Collection(child = true, inverse = "league")
    @PropertyDescriptor(readOnly = false, searchable = true)
    @OrderBy("name")
    public Set<Organization> getOrganizations() {
        return organizations;
    }

    @Column(unique = true)
    @NotNull(message = "is required")
    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    @Embedded
    public Demographics getDemographics() {
        return demographics;
    }

    private UploadableMedia photo = new UploadableMedia();
    @BlobDescriptor(renderType = RenderType.IMAGE, contentDisposition = ContentDisposition.ATTACHMENT)
    @PropertyDescriptor(summary = false, index = 3)
    @OneToOne(cascade = CascadeType.ALL)
    public UploadableMedia getPhoto() {
        return photo;
    }

    private UploadableMedia header = new UploadableMedia();
    @BlobDescriptor(renderType = RenderType.IMAGE, contentDisposition = ContentDisposition.ATTACHMENT)
    @PropertyDescriptor(summary = false, index = 4)
    @OneToOne(cascade = CascadeType.ALL)
    public UploadableMedia getHeader() {
        return header;
    }

    private UploadableMedia logo = new UploadableMedia();
    @BlobDescriptor(renderType = RenderType.IMAGE, contentDisposition = ContentDisposition.ATTACHMENT)
    @PropertyDescriptor(summary = true, index = 5)
    @OneToOne(cascade = CascadeType.ALL)
    public UploadableMedia getLogo() {
        return logo;
    }

    @PropertyDescriptor(hidden = true, summary = false, searchable = false)
    public Long getCreated() {
        return created;
    }

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

    public void setYears(Set<TeamYear> teamYears) {
        this.years = teamYears;
    }

    public void setOfficers(Set<Officer> officers) {
        this.officers = officers;
    }

    public void setOrganizations(Set<Organization> organizations) {
        this.organizations = organizations;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public void setDemographics(Demographics demographics) {
        this.demographics = demographics;
    }

    public void setPhoto(UploadableMedia photo) {
        this.photo = photo;
    }

    public void setHeader(UploadableMedia header) {
        this.header = header;
    }

    public void setLogo(UploadableMedia logo) {
        this.logo = logo;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public void setAccessed(Long accessed) {
        this.accessed = accessed;
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

    public String toString() {
        return getName();
    }

    @Override
    public League clone() {
        return new League(this);
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
        if (!(rhs instanceof League))
            return false;
        final League castedObject = (League) rhs;
        if (getId() == null) {
            if (castedObject.getId() != null)
                return false;
        } else if (!getId().equals(castedObject.getId()))
            return false;
        return true;
    }
}
