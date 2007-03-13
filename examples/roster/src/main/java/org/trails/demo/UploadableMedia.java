package org.trails.demo;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.component.blob.ITrailsBlob;
import org.trails.descriptor.BlobDescriptorExtension.ContentDisposition;
import org.trails.descriptor.BlobDescriptorExtension.RenderType;
import org.trails.descriptor.annotation.BlobDescriptor;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.util.DatePattern;

/**
 * @hibernate.class table="UploadableMedia" lazy="false"
 *
 * UploadableMedia Media, implements domain model for all possible media types
 *
 * @author kenneth.colassi    nhhockeyplayer@hotmail.com
 *
    Here are two valid usages for inside your own trails domain object(s):

    private UploadableMedia photo = new UploadableMedia();
    @BlobDescriptor(renderType = RenderType.IMAGE, contentDisposition = ContentDisposition.ATTACHMENT)
    @PropertyDescriptor(summary = true, index = 1)
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public UploadableMedia getPhoto() {
        return photo;
    }

    private Set<UploadableMedia> clips = new HashSet<UploadableMedia>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id", insertable = true, updatable = true, nullable = true)
    @Collection(child = true)
    @PropertyDescriptor(searchable = false, readOnly = false)
    @OrderBy("name")
    public Set<UploadableMedia> getClips() {
        return clips;
    }
 */
@Entity
public class UploadableMedia implements ITrailsBlob {
    private static final Log log = LogFactory.getLog(UploadableMedia.class);

    protected enum EMedia {
        DOCUMENT, PHOTO, CLIP
    }

    private Integer id = null;

    private String name;

    private String description;

    private EMedia mediaType;

    private String fileName;

    private String filePath;

    private String fileExtension;

    private String contentType;

    private Long numBytes;

    private Long created = new Long(GregorianCalendar.getInstance()
            .getTimeInMillis());

    private Long accessed = new Long(GregorianCalendar.getInstance()
            .getTimeInMillis());

    /**
     * CTOR
     */

    public UploadableMedia() {
    }

    public UploadableMedia(UploadableMedia dto) {
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
    @PropertyDescriptor(hidden = false, summary = true, searchable = true)
    public String getName() {
        return name;
    }

    /**
     * @hibernate.property
     */
    @PropertyDescriptor(hidden = false, summary = true, searchable = true)
    public String getDescription() {
        return description;
    }

    /**
     * @hibernate.property
     */
    @Enumerated(value = EnumType.STRING)
    @PropertyDescriptor(hidden = false, summary = true, searchable = true)
    public EMedia getMediaType() {
        return mediaType;
    }

    @PropertyDescriptor(readOnly = true)
    public String getFileName() {
        return fileName;
    }

    /**
     * @hibernate.property
     */
    @PropertyDescriptor(hidden = true, readOnly = true)
    public String getFilePath() {
        return filePath;
    }

    /**
     * @hibernate.property
     */
    @PropertyDescriptor(hidden = true, readOnly = true)
    public String getFileExtension() {
        return fileExtension;
    }

    /**
     * @hibernate.property
     */
    @PropertyDescriptor(hidden = true, readOnly = true)
    public Long getNumBytes() {
        return numBytes;
    }

    /**
     * @hibernate.property
     */
    @PropertyDescriptor(readOnly = true)
    public String getContentType() {
        return contentType;
    }

    private byte[] bytes = new byte[0];

    /**
     * @hibernate.property
     */
    @BlobDescriptor(renderType = RenderType.IMAGE, contentDisposition = ContentDisposition.ATTACHMENT)
    @PropertyDescriptor(summary = false)
    @Lob
    // uncomment for mysql - @Column(columnDefinition = "longblob", length = 6291456)
    public byte[] getBytes() {
        return bytes;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMediaType(EMedia mediaType) {
        this.mediaType = mediaType;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public void setNumBytes(Long numBytes) {
        this.numBytes = numBytes;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setAccessed(Long accessed) {
        this.accessed = accessed;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String toString() {
        return getName();
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
        if (!(rhs instanceof UploadableMedia))
            return false;
        final UploadableMedia castedObject = (UploadableMedia) rhs;
        if (getId() == null) {
            if (castedObject.getId() != null)
                return false;
        } else if (!getId().equals(castedObject.getId()))
            return false;
        return true;
    }
}