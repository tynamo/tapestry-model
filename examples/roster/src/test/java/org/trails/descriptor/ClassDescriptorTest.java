package org.trails.descriptor;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.trails.demo.*;
import org.trails.persistence.PersistenceService;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author kenneth.colassi nhhockeyplayer@hotmail.com
 */
public class ClassDescriptorTest extends AbstractTransactionalSpringContextTests {
    private ApplicationContext appContext;
    private PersistenceService persistenceService;
    private DescriptorService descriptorService;
    private SessionFactory sessionFactory;
    private PlatformTransactionManager txManager;

    // constants
    private final String testString = "TestData";

    // domain model
    private Organization organization = new Organization();
    private TeamYear TeamYear = new TeamYear();
    private Director director = new Director();
    private Set<TeamYear> TeamYears = new HashSet<TeamYear>();
    private Set<Coach> coaches = new HashSet<Coach>();
    private Set<Team> teams = new HashSet<Team>();
    private Set<Player> players = new HashSet<Player>();

    // miscellaneous reusable references
    private UploadableMedia photo = new UploadableMedia();
    private Set<UploadableMedia> clips = new HashSet<UploadableMedia>();
    private Set<PlayerStat> stats = new HashSet<PlayerStat>();

    // locals
    Coach coach = new Coach();
    Team team = new Team();

    @Override
    public void onSetUpInTransaction() throws Exception
    {
        persistenceService = (PersistenceService) applicationContext.getBean(
                "persistenceService");
        sessionFactory = (SessionFactory)applicationContext.getBean("sessionFactory");

        appContext = new ClassPathXmlApplicationContext(
                "applicationContext-test.xml");
        persistenceService = (PersistenceService) appContext
                .getBean("persistenceService");
        descriptorService = (DescriptorService) appContext
                .getBean("descriptorService");

        sessionFactory = (SessionFactory) appContext.getBean("sessionFactory");

        txManager = (PlatformTransactionManager) appContext
                .getBean("transactionManager");

        populateDomain();
    }

    public void testSetup() throws Exception {
        PersistenceService psvc = (PersistenceService)applicationContext.getBean("persistenceService");

        Organization localOrganization = organization.clone();
        Coach localCoach = coach.clone();
        psvc.save(localOrganization);

/*
            Set TeamYears = organization.getTeamYears();
            Iterator TeamYearIter = TeamYears.iterator();
            while ( TeamYearIter.hasNext()) {
                TeamYear TeamYear = (TeamYear) TeamYearIter.next();
                psvc.save(TeamYear);
            }
*/

            Set coaches = organization.getCoaches();
            Iterator coachIter = coaches.iterator();
            while ( coachIter.hasNext()) {
                Coach coach = (Coach) coachIter.next();
                psvc.save(coach);
            }

            Set teams = organization.getTeams();
            Iterator teamIter = teams.iterator();
            while ( teamIter.hasNext()) {
                Team team = (Team) teamIter.next();
                psvc.save(team);
            }

            // OK here is the test case for back reference lazy
            for (int i = 1800; i < 1801; i++) {
                Integer iCounter = new Integer(i);
                Team team = new Team();
                team.setId(null);
                team.setAge(Team.EAge.U19);
                team.setGender(Team.EGender.MALE);
                team.setDivision(Team.EDivision.I);
                team.setGroupClassification(Team.EGroupClassification.Bantam);
                team.setLevel(Team.ELevel.A);
                team.setTier(Team.ETier.I);
                team.setTeamYear((TeamYear) (TeamYears.toArray())[0]);
                team.setPhoto(photo);
                teams.add(team);
                team.setOrganization(organization);
                psvc.save(team);
                assertNotNull(team);
            }
    }

    public void testRollback() throws Exception {

        TransactionTemplate tt = new TransactionTemplate(txManager);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                PersistenceService psvc = (PersistenceService)applicationContext.getBean("persistenceService");

                psvc.save(organization);

                status.setRollbackOnly(); // force rollback don't change db.
            }
        });
    }

    public void testTeamSave() throws Exception
    {
        // instantiate
        Organization localOrganization = organization.clone();
        Coach localCoach = coach.clone();
        Team localTeam = team.clone();

        // initialize
        localOrganization.getCoaches().clear();
        localOrganization.getTeams().clear();
        localTeam.getCoaches().clear();

        // configure
        localOrganization.getCoaches().add(localCoach);
        localOrganization.getTeams().add(localTeam);

        PersistenceService psvc = (PersistenceService)applicationContext.getBean("persistenceService");

        psvc.save(localOrganization);

        // initialize
        localOrganization.getCoaches().clear();
        localOrganization.getTeams().clear();
        localTeam.getCoaches().clear();

        // configure
        localOrganization.getCoaches().add(localCoach);
        localOrganization.getTeams().add(localTeam);

        psvc.save(localOrganization);
    }


    public void testTeams() throws Exception {
        testTeamSave();
        testTeamSave();
    }

    public void populateDomain() {

        for (int i = 500; i < 505; i++) {
            UploadableMedia bytes = new UploadableMedia();

            if (i == 500)
                photo = bytes;
            photo.setId(null);
            clips.add(photo);
        }

        for (int i = 600; i < 605; i++) {
            PlayerStat stat = new PlayerStat();

            stat.setId(null);
            stats.add(stat);
        }

        TeamYear.setYearStart(new Integer("2006"));
        TeamYear.setYearEnd(new Integer("2007"));
        TeamYear.setId(null);
        TeamYears.add(TeamYear);

        director.setFirstName("Chris");
        director.setLastName("Nelson");
        director.setPassword(testString);
        director.setId(null);
        director.setEmailAddress(director.getFirstName() + "."
                + director.getLastName() + "@" + organization.getName()
                + ".com");
        director.setApplicationRole(Person.EApplicationRole.DIRECTOR);
        director.setPhoto(photo);

        organization = new Organization();
        organization.setName("Trails Sharks");
        organization.setId(null);
//        organization.setCity(testString);
//        organization.setState(testString);
        organization.setHeader(photo);
        organization.setLogo(photo);
        organization.setPhoto(photo);
//        organization.setTeamYears(TeamYears);
//        TeamYear.setOrganization(organization);

        for (int i = 700; i < 705; i++) {
            Integer iCounter = new Integer(i);
            Coach coach = new Coach();
            if ( this.coach == null ) this.coach = coach;
            coach.setFirstName(iCounter.toString());
            coach.setLastName(iCounter.toString());
            coach.setPassword(testString);
            coach.setId(null);
            coach.setEmailAddress(coach.getFirstName() + "."
                    + coach.getLastName() + "@" + organization.getName()
                    + ".com");
            coach.setApplicationRole(Person.EApplicationRole.MANAGER); ////@note: it was Person.ApplicationRole.HEADCOACH
            coach.setPassword("test password");
            coach.setPhoto(photo);

            coaches.add(coach);
            coach.setOrganization(organization);
        }

        for (int i = 800; i < 810; i++) {
            Integer iCounter = new Integer(i);
            Team team = new Team();
            team.setId(null);
            team.setAge(Team.EAge.U19);
            team.setGender(Team.EGender.MALE);
            team.setDivision(Team.EDivision.I);
            team.setGroupClassification(Team.EGroupClassification.Bantam);
            team.setLevel(Team.ELevel.A);
            team.setTier(Team.ETier.I);
            team.setTeamYear((TeamYear) (TeamYears.toArray())[0]);
            team.setPhoto(photo);

            players.clear();
            for (int j = 900; j < 910; j++) {
                Integer jCounter = new Integer(j);
                Player player = new Player();
                player.setId(null);
                player.setEmailAddress(player.getFirstName() + "."
                        + player.getLastName() + "@" + organization.getName()
                        + ".com");
                player.setDexterity(Player.EDexterity.LEFTY);
                player.setPlayerPosition(Player.EPosition.CENTER);
                player.setDob(new Date());
                player.setTeam(team);
                player.setFirstName(jCounter.toString());
                player.setLastName(jCounter.toString());
                player.setPassword(testString);
                player.setPhoto(photo);
                player.setClips(clips);
                player.setStats(stats);

                players.add(player);
            }
            team.setPlayers(players);
            teams.add(team);
            team.setOrganization(organization);

            if ( this.team == null ) this.team = team;
        }

//        organization.setTeamYears(TeamYears);
        organization.setDirector(director);
        organization.setCoaches(coaches);
        organization.setTeams(teams);
    }

    @Override
    protected String[] getConfigLocations()
    {
        return new String[] {"applicationContext-test.xml"};
    }

    public SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }
}