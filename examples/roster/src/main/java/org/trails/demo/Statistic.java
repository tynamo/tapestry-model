package org.trails.demo;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.util.DatePattern;
import org.hibernate.validator.Length;

import javax.persistence.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;


/**
 * Statistics
 *
 * @author kenneth.colassi    nhhockeyplayer@hotmail.com
 */
@Entity
@ClassDescriptor(hidden = true)
public class Statistic {

    private static final Log log = LogFactory.getLog(Statistic.class);

    private Integer id;

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

    private Date date = Calendar.getInstance().getTime();

    private String commnents;

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
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @PropertyDescriptor(hidden = true)
    public Integer getId() {
        return id;
    }

    @PropertyDescriptor(index = 3)
    public Integer getA() {
        return a;
    }

    @PropertyDescriptor(index = 2)
    public Integer getG() {
        return g;
    }

    @PropertyDescriptor(index = 1)
    public Integer getGp() {
        return gp;
    }

    @PropertyDescriptor(index = 9)
    public Integer getGwg() {
        return gwg;
    }

    public Integer getPim() {
        return pim;
    }

    @PropertyDescriptor(index = 6)
    public Integer getPpa() {
        return ppa;
    }

    @PropertyDescriptor(index = 5)
    public Integer getPpg() {
        return ppg;
    }

    @PropertyDescriptor(index = 4)
    public Integer getPts() {
        return pts;
    }

    @PropertyDescriptor(index = 8)
    public Integer getSha() {
        return sha;
    }

    @PropertyDescriptor(index = 7)
    public Integer getShg() {
        return shg;
    }

    @ManyToOne
    @JoinColumn(name = "player_id", insertable = false, updatable = false, nullable = true)
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Length(max = 250)
    public String getCommnents() {
        return commnents;
    }

    public void setCommnents(String commnents) {
        this.commnents = commnents;
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

    public void setAccessed(Long accessed) {
        this.accessed = accessed;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String toString() {
        DateFormat formatter = SimpleDateFormat.getDateInstance();
        return formatter.format(getDate());
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
        if (!(rhs instanceof Statistic))
            return false;
        final Statistic castedObject = (Statistic) rhs;
        if (id == null) {
            if (castedObject.id != null)
                return false;
        } else if (!id.equals(castedObject.id))
            return false;
        return true;
	}
}