package org.trails.demo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * A Person has a photo, eRole and application role
 *
 * @author kenneth.colassi        nhhockeyplayer@hotmail.com
 */
@MappedSuperclass
@ClassDescriptor(hidden = true)
public class Person implements Serializable {
    private static final Log log = LogFactory.getLog(Person.class);

    public enum ERole {
        USER, ADMIN, SYSTEMADMIN
    }

    public enum EApplicationRole {
        MANAGER, DIRECTOR, SALES, MARKETING
    }

    protected Integer id = new Integer ("-1");

    protected String firstName;

    protected String lastName;

    private Demographics demographics = new Demographics();

    protected Date dob;

    protected String emailAddress;

    protected String password;

    protected ERole eRole;

    protected EApplicationRole eApplicationRole;

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

    @BlobDescriptor(renderType = RenderType.IMAGE, contentDisposition = ContentDisposition.ATTACHMENT)
    @PropertyDescriptor(summary = true, index = 1)
    @OneToOne(cascade = CascadeType.ALL)
    public UploadableMedia getPhoto() {
        return photo;
    }

    @Enumerated(value = EnumType.STRING)
    @NotNull(message = "is required")
    @PropertyDescriptor(summary = true, index = 2)
    public ERole getERole() {
        return eRole;
    }

    @PropertyDescriptor(summary = true, index = 3)
    public String getFirstName() {
        return firstName;
    }

    @PropertyDescriptor(summary = true, index = 4)
    public String getLastName() {
        return lastName;
    }

    @PropertyDescriptor(summary = true, index = 5)
    public Date getDob() {
        return dob;
    }

    @Embedded
    public Demographics getDemographics() {
        return demographics;
    }

    @Column(unique = true)
    @PrimaryKeyJoinColumn
    @PropertyDescriptor(summary = true, index = 7)
    public String getEmailAddress() {
        return emailAddress;
    }

    @PropertyDescriptor(summary = true, index = 8)
    public String getPassword() {
        return password;
    }

    @Enumerated(value = EnumType.STRING)
    @PropertyDescriptor(summary = true, index = 9)
    public EApplicationRole getApplicationRole() {
        return eApplicationRole;
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

    public void setDemographics(Demographics demographics) {
        this.demographics = demographics;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setApplicationRole(EApplicationRole eApplicationRole) {
        this.eApplicationRole = eApplicationRole;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    @Transient
    @PropertyDescriptor(hidden = true, summary = false, searchable = false)
    public void setCreatedAsString(String value) throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(DatePattern.sdf.parse(value).getTime());
        this.created = new Long(cal.getTimeInMillis());
    }

    public void setAccessed(Long accessed) {
        this.accessed = accessed;
    }

    @Transient
    @PropertyDescriptor(hidden = true, summary = false, searchable = false)
    public void setAccessedAsString(String value) throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(DatePattern.sdf.parse(value).getTime());
        this.accessed = new Long(cal.getTimeInMillis());
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
        result = PRIME * result + ((getId() == null) ? 0 : getId().hashCode());
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
        if (getId() == null) {
            if (castedObject.getId() != null)
                return false;
        } else if (!getId().equals(castedObject.getId()))
            return false;
        return true;
    }
}