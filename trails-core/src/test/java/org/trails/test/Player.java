package org.trails.test;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.component.blob.model.UploadableMedia;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.Collection;
import org.trails.descriptor.annotation.PropertyDescriptor;

/**
 * @hibernate.class table="Player" lazy="true"
 *
 * A player has a photo and team
 *
 * @author kenneth.colassi    nhhockeyplayer@hotmail.com
 */
@Entity
@ClassDescriptor(hasCyclicRelationships=true)
public class Player extends Person {
    private static final Log log = LogFactory.getLog(Player.class);

    private Integer id = null;

    public enum EPosition {
        GOALIE, DEFENSE, LEFTWING, RIGHTWING, CENTER, ALTERNATE, SPARE, BACKUP, WALKON
    }

    public enum EDexterity {
        LEFTY, RIGHTY
    }

    private Integer playerNumber;

    private EPosition position;

    private EDexterity dexterity;

    private Team team;

    private Set<UploadableMedia> clips = new HashSet<UploadableMedia>();

    private Set<Statistic> stats = new HashSet<Statistic>();

    /**
     * CTOR
     */
    public Player() {
    }

    public Player(Player dto) {
        super(dto);

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
    @PropertyDescriptor(summary = true, index = 1)
    public Integer getPlayerNumber() {
        return playerNumber;
    }

    /**
     * @hibernate.property
     */
    @Enumerated(value = EnumType.STRING)
    public EPosition getPosition() {
        return position;
    }

    /**
     * @hibernate.property
     */
    @Enumerated(value = EnumType.STRING)
    public EDexterity getDexterity() {
        return dexterity;
    }

    /**
     * @hibernate.property
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", insertable = false, updatable = true, nullable = true)
    public Team getTeam() {
        return team;
    }

    /**
     * @hibernate.property
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", insertable = true, updatable = true, nullable = true)
    @Collection(child = true)
    @PropertyDescriptor(searchable = false, readOnly = false)
    @OrderBy("name")
    public Set<UploadableMedia> getClips() {
        return clips;
    }

    /**
     * @hibernate.property
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", insertable = true, updatable = true, nullable = true)
    @Collection(child = true)
    @PropertyDescriptor(searchable = true, readOnly = false)
    public Set<Statistic> getStats() {
        return stats;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPlayerNumber(Integer number) {
        this.playerNumber = number;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPosition(EPosition position) {
        this.position = position;
    }

    public void setDexterity(EDexterity dexterity) {
        this.dexterity = dexterity;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setClips(Set<UploadableMedia> clips) {
        this.clips = clips;
    }

    public void setStats(Set<Statistic> statistic) {
        this.stats = statistic;
    }

    public Player clone(Object dto) {
        if (dto instanceof Player)
            return new Player((Player) dto);
        else
            return null;
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
        if (!(rhs instanceof Player))
            return false;
        final Player castedObject = (Player) rhs;
        if (id == null) {
            if (castedObject.id != null)
                return false;
        } else if (!id.equals(castedObject.id))
            return false;
        return true;
    }
}
