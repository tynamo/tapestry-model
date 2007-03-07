package org.trails.demo;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.NotNull;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.util.DatePattern;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author kenneth.colassi    nhhockeyplayer@hotmail.com
 */
@MappedSuperclass
@ClassDescriptor(hidden = true)
public class Person {

    private static final Log log = LogFactory.getLog(Person.class);

    public enum ERole {
        USER, ADMIN
    }

    public enum ApplicationRole {
        MANAGER, DIRECTOR, HEADCOACH, ASSISTANTCOACH, EQUIPMENTMGR, OPERATIONS, TRAINER, SALES, MARKETING, PLAYER
    }

    private Integer id = null;

    private String firstName;

    private String lastName;

    private Date dob;

    private String emailAddress;

    private String password;

    private ERole eRole;

    private ApplicationRole applicationRole;

    private Long created = new Long(GregorianCalendar.getInstance()
            .getTimeInMillis());

    private Long accessed = new Long(GregorianCalendar.getInstance()
            .getTimeInMillis());

    /**
     * CTOR
     */
    public Person(Person dto) {
        try {
            BeanUtils.copyProperties(this, dto);
        } catch (Exception e) {
            log.error(e.toString());
            e.printStackTrace();
        }
    }

    public Person() {
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

/*
    private UploadableMedia photo = new UploadableMedia();

    @BlobDescriptor(renderType = RenderType.IMAGE, contentDisposition = ContentDisposition.ATTACHMENT)
    @PropertyDescriptor(summary = true, index = 1)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public UploadableMedia getPhoto() {
        return photo;
    }
*/

    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "is required")
    public ERole getERole() {
        return eRole;
    }

    @PropertyDescriptor(index = 3)
    public String getFirstName() {
        return firstName;
    }

    @PropertyDescriptor(index = 2)
    public String getLastName() {
        return lastName;
    }

    public Date getDob() {
        return dob;
    }

    @Column(unique = true)
    @PropertyDescriptor(index = 4)
    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "is required")
    public ApplicationRole getApplicationRole() {
        return applicationRole;
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

    public void setId(Integer id) {
        this.id = id;
    }

/*
    public void setPhoto(UploadableMedia photo) {
        this.photo = photo;
    }
*/

    public void setERole(ERole role) {
        this.eRole = role;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setApplicationRole(ApplicationRole applicationRole) {
        this.applicationRole = applicationRole;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public void setAccessed(Long accessed) {
        this.accessed = accessed;
    }

    @Override
    public Person clone() {
        return new Person(this);
    }

    @Override
    public String toString() {
        return getLastName() + ", " + getFirstName();
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
        if (!(rhs instanceof Person))
            return false;
        final Person castedObject = (Person) rhs;
        if (id == null) {
            if (castedObject.id != null)
                return false;
        } else if (!id.equals(castedObject.id))
            return false;
        return true;
    }
}