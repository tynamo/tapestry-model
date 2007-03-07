package org.trails.demo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.Collection;
import org.trails.descriptor.annotation.PropertyDescriptor;

/**
 * A player has a photo and team
 *
 * @author kenneth.colassi    nhhockeyplayer@hotmail.com
 */
@Entity
public class Player extends Person {

    private static final Log log = LogFactory.getLog(Player.class);

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

//    private Set<UploadableMedia> clips = new HashSet<UploadableMedia>();

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

    @PropertyDescriptor(index = 1)
    public Integer getPlayerNumber() {
        return playerNumber;
    }

    @Enumerated(value = EnumType.STRING)
    @Column(name = "pos")
    public EPosition getPosition() {
        return position;
    }

    @Enumerated(value = EnumType.STRING)
    public EDexterity getDexterity() {
        return dexterity;
    }

    @ManyToOne
    @JoinColumn(name = "player_team_fk")
    public Team getTeam() {
        return team;
    }

/*
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", insertable = true, updatable = true, nullable = true)
    @Collection(child = true)
    @PropertyDescriptor(searchable = false, readOnly = false)
    @OrderBy("name")
    public Set<UploadableMedia> getClips() {
        return clips;
    }
*/

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    @Collection(child = true, inverse = "player")
    public Set<Statistic> getStats() {
        return stats;
    }

    public void setPlayerNumber(Integer number) {
        this.playerNumber = number;
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

/*
    public void setClips(Set<UploadableMedia> clips) {
        this.clips = clips;
    }
*/

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
