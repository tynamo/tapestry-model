package org.trails.demo;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.trails.descriptor.BlobDescriptorExtension.ContentDisposition;
import org.trails.descriptor.BlobDescriptorExtension.RenderType;
import org.trails.descriptor.annotation.BlobDescriptor;
import org.trails.descriptor.annotation.ClassDescriptor;
import org.trails.descriptor.annotation.Collection;
import org.trails.descriptor.annotation.PropertyDescriptor;
import org.trails.util.DatePattern;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;


/**
 * A Team has players, coaches, clips and stats
 *
 * @author kenneth.colassi    nhhockeyplayer@hotmail.com
 */
@Entity
@ClassDescriptor(hasCyclicRelationships=true)
public class Team implements Cloneable, Serializable {
	private static final Log log = LogFactory.getLog(Team.class);

	public enum ESeason {
		WINTER, SPRING, SUMMER, FALL
	}

	public enum EGender {
		MALE, FEMALE
	}

	public enum EDivision {
		I, II, III, IV
	}

	public enum ELevel {
		AAA, AA, A, B, C, D
	}

	public enum EAge {
		U19, U18, U17, U16, U15, U14, U12, U10, U8
	}

	public enum EGroupClassification {
		Midget, Bantam, Peewee, Squirt, Mite
	}

	public enum ETier {
		I, II, III, IV
	}

	private Integer id = null;

	private EGender gender;

	private Organization organization = null;

	private Set<Coach> coaches = new HashSet<Coach>();

	private EDivision division;

	private ELevel level;

	private EAge age;

	private EGroupClassification groupClassification;

	private ETier tier;

	private Set<UploadableMedia> clips = new HashSet<UploadableMedia>();

	private Set<Player> players = new HashSet<Player>();

	private TeamYear teamYear = null;

	private Long created = new Long(GregorianCalendar.getInstance()
			.getTimeInMillis());

	private Long accessed = new Long(GregorianCalendar.getInstance()
			.getTimeInMillis());

	/**
	 * CTOR
	 */
	public Team() {
	}

	public Team(Team dto) {
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

	@ManyToOne
	@JoinColumn(name = "team_organization_fk", insertable = false, updatable = true, nullable = true)
	public Organization getOrganization() {
		return organization;
	}

	@OneToMany
	@JoinColumn(name = "team_id")
	@Collection(child = false, inverse = "team")
	@PropertyDescriptor(readOnly = false, searchable = true)
	@OrderBy("lastName")
	public Set<Coach> getCoaches() {
		return coaches;
	}

	@Enumerated(value = EnumType.STRING)
	@PropertyDescriptor(summary = true)
	public EGender getGender() {
		return gender;
	}

	@Enumerated(value = EnumType.STRING)
	@PropertyDescriptor(summary = true)
	public EDivision getDivision() {
		return division;
	}

	@Enumerated(value = EnumType.STRING)
	@PropertyDescriptor(summary = true)
	public ELevel getLevel() {
		return level;
	}

	@Enumerated(value = EnumType.STRING)
	@PropertyDescriptor(summary = true)
	public EAge getAge() {
		return age;
	}

	@Enumerated(value = EnumType.STRING)
	@PropertyDescriptor(summary = true)
	public EGroupClassification getGroupClassification() {
		return groupClassification;
	}

	@Enumerated(value = EnumType.STRING)
	@PropertyDescriptor(summary = true)
	public ETier getTier() {
		return tier;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "clips_team_fk", insertable = true, updatable = true, nullable = true)
	@Collection(child = true, inverse = "team")
	@PropertyDescriptor(searchable = false, readOnly = false)
	@OrderBy("name")
	public Set<UploadableMedia> getClips() {
		return clips;
	}

	@OneToMany
	@JoinColumn(name = "player_team_fk")
	@Collection(child = false, inverse = "team")
	@PropertyDescriptor(readOnly = false, searchable = true)
	@OrderBy("lastName")
	public Set<Player> getPlayers() {
		return players;
	}

	private UploadableMedia photo = new UploadableMedia();

	@BlobDescriptor(renderType = RenderType.IMAGE, contentDisposition = ContentDisposition.ATTACHMENT)
	@PropertyDescriptor(summary = true, index = 2)
	@OneToOne(cascade = CascadeType.ALL)
	public UploadableMedia getPhoto() {
		return photo;
	}

	@ManyToOne
	@JoinColumn(name = "team_teamyear_fk")
	@PrimaryKeyJoinColumn
	@PropertyDescriptor(index = 1)
	public TeamYear getTeamYear() {
		return teamYear;
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

	public void setGender(EGender gender) {
		this.gender = gender;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public void setCoaches(Set<Coach> coaches) {
		this.coaches = coaches;
	}

	public void setDivision(EDivision division) {
		this.division = division;
	}

	public void setLevel(ELevel level) {
		this.level = level;
	}

	public void setAge(EAge age) {
		this.age = age;
	}

	public void setGroupClassification(EGroupClassification group) {
		this.groupClassification = group;
	}

	public void setTier(ETier tier) {
		this.tier = tier;
	}

	public void setClips(Set<UploadableMedia> clips) {
		this.clips = clips;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}

	public void setPhoto(UploadableMedia photo) {
		this.photo = photo;
	}

	public void setTeamYear(TeamYear teamYear) {
		this.teamYear = teamYear;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public void setAccessed(Long accessed) {
		this.accessed = accessed;
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

	@Override
	public String toString() {
		String result = "";
		if (organization == null)
			return result;
		else
			return getOrganization().getDemographics().getCity() + ","
					+ getOrganization().getDemographics().getState() + " "
					+ getGender().toString() + " " + getAge().toString();
	}

	@Override
	public Team clone() {
		return new Team(this);
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
		if (!(rhs instanceof Team))
			return false;
		final Team castedObject = (Team) rhs;
		if (getId() == null) {
			if (castedObject.getId() != null)
				return false;
		} else if (!getId().equals(castedObject.getId()))
			return false;
		return true;
	}
}
