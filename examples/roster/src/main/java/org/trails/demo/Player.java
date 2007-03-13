package org.trails.demo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.Collection;
import org.trails.descriptor.annotation.PropertyDescriptor;

/**
 * @hibernate.class table="Player" lazy="true"
 *
 * A player has a photo and team
 *
 * @author kenneth.colassi        nhhockeyplayer@hotmail.com
 */
@Entity
@ClassDescriptor(hasCyclicRelationships=true, hidden = true)
public class Player extends Person {
    private static final Log log = LogFactory.getLog(Player.class);

    protected enum EPosition {
        GOALIE, DEFENSE, LEFTWING, RIGHTWING, CENTER, ALTERNATE, SPARE, BACKUP, WALKON
    }

    protected enum EDexterity {
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
    @Column(name = "pos")
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
    @ManyToOne
    @JoinColumn(name = "player_team_fk")
    public Team getTeam() {
        return team;
    }

    /**
     * @hibernate.property
     */
    @OneToMany
    @JoinColumn(name = "clips_player_fk", insertable = true, updatable = true, nullable = true)
    @Collection(child = true, inverse = "player")
    @PropertyDescriptor(searchable = false, readOnly = false)
    @OrderBy("name")
    public Set<UploadableMedia> getClips() {
        return clips;
    }

    /**
     * @hibernate.property
     */
    @OneToMany
    @JoinColumn(name = "stats_player_fk")
    @Collection(child = true, inverse = "player")
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

    @Override
    public Player clone() {
        return new Player(this);
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
        if (!(rhs instanceof Player))
            return false;
        final Player castedObject = (Player) rhs;
        if (getId() == null) {
            if (castedObject.getId() != null)
                return false;
        } else if (!getId().equals(castedObject.getId()))
            return false;
        return true;
    }
}
