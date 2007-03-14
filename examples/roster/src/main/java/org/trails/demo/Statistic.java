package org.trails.demo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
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
 * Statistics
 *
 * @author kenneth.colassi        nhhockeyplayer@hotmail.com
 */
@Entity
@ClassDescriptor(hasCyclicRelationships=true, hidden = true)
public class Statistic implements Serializable {
    private static final Log log = LogFactory.getLog(Statistic.class);

    private Integer id = null;

    private Integer gp;

    private Integer g;

    private Integer a;

    private Integer pts;

    private Integer pim;

    private Integer ppg;

    private Integer ppa;

    private Integer shg;

    private Integer sha;

    private Integer gwg;

    private Player player;

    private Long created = new Long(GregorianCalendar.getInstance()
            .getTimeInMillis());

    private Long accessed = new Long(GregorianCalendar.getInstance()
            .getTimeInMillis());

    /**
     * CTOR
     */

    public Statistic() {
    }

    public Statistic(Statistic dto) {
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

    @PropertyDescriptor(index = 3, hidden = false, summary = true, searchable = true)
    public Integer getA() {
        return a;
    }

    @PropertyDescriptor(index = 2, hidden = false, summary = true, searchable = true)
    public Integer getG() {
        return g;
    }

    @PropertyDescriptor(index = 1, hidden = false, summary = true, searchable = true)
    public Integer getGp() {
        return gp;
    }

    @PropertyDescriptor(index = 9, hidden = false, summary = true, searchable = true)
    public Integer getGwg() {
        return gwg;
    }

    @PropertyDescriptor(hidden = false, summary = true, searchable = true)
    public Integer getPim() {
        return pim;
    }

    @PropertyDescriptor(index = 6, hidden = false, summary = true, searchable = true)
    public Integer getPpa() {
        return ppa;
    }

    @PropertyDescriptor(index = 5, hidden = false, summary = true, searchable = true)
    public Integer getPpg() {
        return ppg;
    }

    @PropertyDescriptor(index = 4, hidden = false, summary = true, searchable = true)
    public Integer getPts() {
        return pts;
    }

    @PropertyDescriptor(index = 8, hidden = false, summary = true, searchable = true)
    public Integer getSha() {
        return sha;
    }

    @PropertyDescriptor(index = 7, hidden = false, summary = true, searchable = true)
    public Integer getShg() {
        return shg;
    }

    @ManyToOne
    @JoinColumn(name = "statistic_player_fk", insertable = false, updatable = false, nullable = true)
    @PropertyDescriptor(readOnly = true, index = 0)
    public Player getPlayer() {
        return player;
    }

    public void setA(Integer a) {
        this.a = a;
    }

    public void setG(Integer g) {
        this.g = g;
    }

    public void setGp(Integer gp) {
        this.gp = gp;
    }

    public void setGwg(Integer gwg) {
        this.gwg = gwg;
    }

    public void setPim(Integer pim) {
        this.pim = pim;
    }

    public void setPpa(Integer ppa) {
        this.ppa = ppa;
    }

    public void setPpg(Integer ppg) {
        this.ppg = ppg;
    }

    public void setPts(Integer pts) {
        this.pts = pts;
    }

    public void setSha(Integer sha) {
        this.sha = sha;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setShg(Integer shg) {
        this.shg = shg;
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
        if (!(rhs instanceof Statistic))
            return false;
        final Statistic castedObject = (Statistic) rhs;
        if (getId() == null) {
            if (castedObject.getId() != null)
                return false;
        } else if (!getId().equals(castedObject.getId()))
            return false;
        return true;
    }
}