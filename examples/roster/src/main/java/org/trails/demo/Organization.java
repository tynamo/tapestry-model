package org.trails.demo;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.NotNull;
import org.trails.descriptor.annotation.Collection;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.util.DatePattern;
import org.trails.validation.ValidateUniqueness;

import javax.persistence.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;


/**
 * Organizations have one Director, and Years, Coaches, Teams
 *
 * @author kenneth.colassi    nhhockeyplayer@hotmail.com
 */
@Entity
@ValidateUniqueness(property = "name")
public class Organization {

    private static final Log log = LogFactory.getLog(Organization.class);

    private Integer id = null;

    private Director director;

    private Set<Year> years = new HashSet<Year>();

    private Set<Coach> coaches = new HashSet<Coach>();

    private Set<Team> teams = new HashSet<Team>();

    private String name;

    private String city;

    private String state;

    private Long created = new Long(GregorianCalendar.getInstance()
            .getTimeInMillis());

    private Long accessed = new Long(GregorianCalendar.getInstance()
            .getTimeInMillis());

    /**
     * CTOR
     */
    public Organization() {
    }

    public Organization(Organization dto) {
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


    @OneToMany
    @JoinColumn(name = "year_organization_fk")
    @Collection(child = true, inverse = "organization")
    @PropertyDescriptor(index = 1)
    @OrderBy("yearStart")
    public Set<Year> getYears() {
        return years;
    }

    /**
     * Note: I couldn't find a better way to get a bidirectional read&write OneToOne relationship
     *
     * @return Director
     */

    @OneToOne
    @JoinTable(name = "OrganizationsDirectors",
            joinColumns = @JoinColumn(name = "director_fk"),
            inverseJoinColumns = {@JoinColumn(name = "organization_fk")}
    )
    @PropertyDescriptor(index = 2)
    public Director getDirector() {
        return director;
    }

    @OneToMany
    @JoinColumn(name = "coach_organization_fk")
    @Collection(child = false, inverse = "organization")
    @OrderBy("lastName")
    public Set<Coach> getCoaches() {
        return coaches;
    }

    @OneToMany
    @JoinColumn(name = "team_organization_fk")
    @Collection(child = false, inverse = "organization")
    @OrderBy("division")
    public Set<Team> getTeams() {
        return teams;
    }

    @Column(unique = true)
    @NotNull(message = "is required")
    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    /*
    private UploadableMedia photo = new UploadableMedia();
    @BlobDescriptor(renderType = RenderType.IMAGE, contentDisposition = ContentDisposition.ATTACHMENT)
    @PropertyDescriptor(summary = false, index = 3)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public UploadableMedia getPhoto() {
        return photo;
    }

    private UploadableMedia header = new UploadableMedia();
    @BlobDescriptor(renderType = RenderType.IMAGE, contentDisposition = ContentDisposition.ATTACHMENT)
    @PropertyDescriptor(summary = false, index = 4)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public UploadableMedia getHeader() {
        return header;
    }

    private UploadableMedia logo = new UploadableMedia();
    @BlobDescriptor(renderType = RenderType.IMAGE, contentDisposition = ContentDisposition.ATTACHMENT)
    @PropertyDescriptor(summary = true, index = 5)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public UploadableMedia getLogo() {
        return logo;
    }
*/

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

    public void setYears(Set<Year> years) {
        this.years = years;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public void setCoaches(Set<Coach> coaches) {
        this.coaches = coaches;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    /*
        public void setPhoto(UploadableMedia photo) {
            this.photo = photo;
        }

        public void setHeader(UploadableMedia header) {
            this.header = header;
        }

        public void setLogo(UploadableMedia logo) {
            this.logo = logo;
        }
    */
    public void setCreated(Long created) {
        this.created = created;
    }

    public void setAccessed(Long accessed) {
        this.accessed = accessed;
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

    public String toString() {
        return getName();
    }

    @Override
    public Organization clone() {
        return new Organization(this);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || (!(o instanceof Organization))) return false;

        Organization that = (Organization) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }
}
