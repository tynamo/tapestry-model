package org.trails.test;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.NotNull;
import org.trails.descriptor.BlobDescriptorExtension.ContentDisposition;
import org.trails.descriptor.BlobDescriptorExtension.RenderType;
import org.trails.descriptor.annotation.BlobDescriptor;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.util.DatePattern;


/**
 * @hibernate.class table="Person" lazy="true"
 *
 * @author kenneth.colassi    nhhockeyplayer@hotmail.com
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@MappedSuperclass
@ClassDescriptor(hidden = true)
public class Person implements Serializable {
    private static final Log log = LogFactory.getLog(Person.class);

    public enum ERole {
        USER, ADMIN
    }

    public enum ApplicationRole {
        MANAGER, DIRECTOR, HEADCOACH, ASSISTANTCOACH, EQUIPMENTMGR, OPERATIONS, TRAINER, SALES, MARKETING, PLAYER
    }

    protected Integer id = null;

    protected String firstName;

    protected String lastName;

    protected Date dob;

    protected String emailAddress;

    protected String password;

    protected ERole eRole;

    protected ApplicationRole applicationRole;

    protected Long created = new Long(GregorianCalendar.getInstance()
            .getTimeInMillis());

    protected Long accessed = new Long(GregorianCalendar.getInstance()
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
     * @hibernate.id generator-class="increment" unsaved-value="-1"
     *               type="java.lang.Integer" unique="true" insert="true"
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @PropertyDescriptor(readOnly = true, summary = true, index = 0)
    public Integer getId() {
        return id;
    }

    private UploadableMedia photo = new UploadableMedia();

    /**
     * @hibernate.property
     */
    @BlobDescriptor(renderType = RenderType.IMAGE, contentDisposition = ContentDisposition.ATTACHMENT)
    @PropertyDescriptor(summary = true, index = 1)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public UploadableMedia getPhoto() {
        return photo;
    }

    /**
     * @hibernate.property
     */
    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "is required")
    public ERole getERole() {
        return eRole;
    }

    /**
     * @hibernate.property
     */
    @PropertyDescriptor(summary = true, index = 3)
    public String getFirstName() {
        return firstName;
    }

    /**
     * @hibernate.property
     */
    @PropertyDescriptor(summary = true, index = 2)
    public String getLastName() {
        return lastName;
    }

    public Date getDob() {
        return dob;
    }

    /**
     * @hibernate.property
     */
    @Column(unique = true)
    @PrimaryKeyJoinColumn
    @PropertyDescriptor(summary = true, index = 4)
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @hibernate.property
     */
    public String getPassword() {
        return password;
    }

    /**
     * @hibernate.property
     */
    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "is required")
    public ApplicationRole getApplicationRole() {
        return applicationRole;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPhoto(UploadableMedia photo) {
        this.photo = photo;
    }

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

    public Person clone(Object dto) {
        if (dto instanceof Person)
            return new Person((Person) dto);
        else
            return null;
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