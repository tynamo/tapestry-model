package org.trails.page;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.callback.ICallback;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.valid.IValidationDelegate;
import org.hibernate.SessionFactory;
import org.jmock.Mock;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.trails.callback.CollectionCallback;
import org.trails.callback.EditCallback;
import org.trails.component.ComponentTest;
import org.trails.demo.*;
import org.trails.descriptor.*;
import org.trails.persistence.PersistenceService;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author kenneth.colassi nhhockeyplayer@hotmail.com
 */
public class DetachedCollectionTest extends ComponentTest {
    private ApplicationContext appContext = new ClassPathXmlApplicationContext(
            "applicationContext-test.xml");
    private PersistenceService psvc = (PersistenceService) appContext
            .getBean("persistenceService");
    private DescriptorService descriptorService = (DescriptorService) appContext
            .getBean("descriptorService");
    private SessionFactory sessionFactory = (SessionFactory) appContext
            .getBean("sessionFactory");
    private PlatformTransactionManager txManager = (PlatformTransactionManager) appContext
            .getBean("transactionManager");

    // constants
    private final String testString = "JustSomeFillInTestData hee hee";

    // domain model
    private Organization organization = new Organization();
    private TeamYear teamYear = new TeamYear();
    private Director director = new Director();
    private Set<TeamYear> teamYears = new HashSet<TeamYear>();
    private Set<Coach> coaches = new HashSet<Coach>();
    private Set<Team> teams = new HashSet<Team>();
    private Set<Player> players = new HashSet<Player>();

    // miscellaneous reusable references
    private UploadableMedia photo = new UploadableMedia();
    private Set<UploadableMedia> clips = new HashSet<UploadableMedia>();
    private Set<PlayerStat> stats = new HashSet<PlayerStat>();

    // locals
    private Coach coach = new Coach();
    private Team team = new Team();

    String organizationPageName = "organizationEdit";
    String TeamYearPageName = "TeamYearEdit";
    String coachPageName = "coachEdit";
    String teamPageName = "teamEdit";

    Mock cycleMock = new Mock(IRequestCycle.class);
    Mock validatorMock = new Mock(IValidationDelegate.class);
    Mock markupWriterMock = new Mock(IMarkupWriter.class);

    IdentifierDescriptor organizationIdDescriptor = new IdentifierDescriptor(Organization.class, "id", Organization.class);
    IdentifierDescriptor TeamYearIdDescriptor = new IdentifierDescriptor(TeamYear.class, "id", TeamYear.class);
    IdentifierDescriptor coachIdDescriptor = new IdentifierDescriptor(Coach.class, "id", Coach.class);
    IdentifierDescriptor teamIdDescriptor = new IdentifierDescriptor(Team.class, "id", Team.class);

    EditPage organizationEditPage = (EditPage)creator.newInstance(EditPage.class,
            new Object[] {
            "descriptorService", descriptorService,
            "persistenceService", psvc
        });
    EditPage TeamYearEditPage = (EditPage)creator.newInstance(EditPage.class,
            new Object[] {
        "descriptorService", descriptorService,
        "persistenceService", psvc
    });
    EditPage coachEditPage = (EditPage)creator.newInstance(EditPage.class,
            new Object[] {
        "descriptorService", descriptorService,
        "persistenceService", psvc
    });
    EditPage teamEditPage = (EditPage)creator.newInstance(EditPage.class,
            new Object[] {
        "descriptorService", descriptorService,
        "persistenceService", psvc
    });

    CollectionDescriptor TeamYearCollectionDescriptor = new CollectionDescriptor(Organization.class, "teamYears", Set.class);
    CollectionDescriptor coachCollectionDescriptor = new CollectionDescriptor(Organization.class, "coaches", Set.class);
    CollectionDescriptor teamCollectionDescriptor = new CollectionDescriptor(Organization.class, "teams", Set.class);

    IClassDescriptor organizationClassDescriptor = new TrailsClassDescriptor(Organization.class);
    IClassDescriptor TeamYearClassDescriptor = new TrailsClassDescriptor(TeamYear.class);
    IClassDescriptor coachClassDescriptor = new TrailsClassDescriptor(Coach.class);
    IClassDescriptor teamClassDescriptor = new TrailsClassDescriptor(Team.class);

    EditCallback organizationCallBack = new EditCallback(organizationPageName, new Organization());
    CollectionCallback TeamYearCallBack;
    CollectionCallback coachCallBack;
    CollectionCallback teamCallBack;

    public void setUp() throws Exception {
        psvc = (PersistenceService) appContext.getBean("persistenceService");
        sessionFactory = (SessionFactory) appContext.getBean("sessionFactory");

        appContext = new ClassPathXmlApplicationContext(
                "applicationContext-test.xml");
        psvc = (PersistenceService) appContext.getBean("persistenceService");
        descriptorService = (DescriptorService) appContext
                .getBean("descriptorService");

        sessionFactory = (SessionFactory) appContext.getBean("sessionFactory");

        txManager = (PlatformTransactionManager) appContext
                .getBean("transactionManager");

        setupDomain();

        TeamYearCollectionDescriptor.setElementType(TeamYear.class);
        TeamYearCollectionDescriptor.setChildRelationship(true);
        coachCollectionDescriptor.setElementType(Coach.class);
        coachCollectionDescriptor.setChildRelationship(true);
        teamCollectionDescriptor.setElementType(Team.class);
        teamCollectionDescriptor.setChildRelationship(true);
        TeamYearClassDescriptor.setChild(true);
        coachClassDescriptor.setChild(true);
        teamClassDescriptor.setChild(true);

        TeamYearCallBack = new CollectionCallback(TeamYearPageName, organization, TeamYearCollectionDescriptor);
        coachCallBack = new CollectionCallback(coachPageName, organization, coachCollectionDescriptor);
        teamCallBack = new CollectionCallback(teamPageName, organization, teamCollectionDescriptor);

        organizationClassDescriptor.getPropertyDescriptors().add(organizationIdDescriptor);
        organizationEditPage = buildEditPage();
        organizationEditPage.setModel(organization);

        TeamYearClassDescriptor.getPropertyDescriptors().add(TeamYearIdDescriptor);
        TeamYearEditPage = buildEditPage();
        TeamYearEditPage.setModel(teamYear);

        coachClassDescriptor.getPropertyDescriptors().add(coachIdDescriptor);
        coachEditPage = buildEditPage();
        coachEditPage.setModel(coach);

        teamClassDescriptor.getPropertyDescriptors().add(teamIdDescriptor);
        teamEditPage = buildEditPage();
        teamEditPage.setModel(team);

        // move these in when you need'em
        //descriptorServiceMock.expects(atLeastOnce()).method("getClassDescriptor").with(eq(teamYear.class)).will(returnValue(TeamYearClassDescriptor));
        //descriptorServiceMock.expects(atLeastOnce()).method("getClassDescriptor").with(eq(Organization.class)).will(returnValue(organizationClassDescriptor));
        //descriptorServiceMock.expects(atLeastOnce()).method("getClassDescriptor").with(eq(Coach.class)).will(returnValue(coachClassDescriptor));
        //descriptorServiceMock.expects(atLeastOnce()).method("getClassDescriptor").with(eq(Team.class)).will(returnValue(teamClassDescriptor));

    }

    public void testSetup() throws Exception {
        TransactionTemplate tt = new TransactionTemplate(txManager);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {

                psvc = (PersistenceService) appContext
                        .getBean("persistenceService");

                Organization org = organization.clone();
                org.setId(null);
                org.getDirector().setId(null);
                org.getDirector().setOrganization(org);
                org.getHeader().setId(null);
                org.getPhoto().setId(null);
                org.getLogo().setId(null);
/*

                Set<teamYear> teamYears = org.getTeamYears();
                for (Iterator y = teamYears.iterator(); y.hasNext();) {
                    teamYear obj = (teamYear) y.next();
                    obj.setId(null);
                    obj.setOrganization(org);
                }

*/
                Set<Coach> coaches = org.getCoaches();
                for (Iterator c = coaches.iterator(); c.hasNext();) {
                    Coach obj = (Coach) c.next();
                    obj.setId(null);
                    obj.setOrganization(org);
                }
                Set<Team> teams = org.getTeams();
                for (Iterator t = teams.iterator(); t.hasNext();) {
                    Team obj = (Team) t.next();
                    obj.setId(null);
                    obj.setOrganization(org);
                }
                psvc.save(org);
                status.setRollbackOnly(); // force rollback don't change db.
            }
        });
    }

    public void testRollback() throws Exception {

        TransactionTemplate tt = new TransactionTemplate(txManager);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                PersistenceService persistenceService = (PersistenceService) appContext
                        .getBean("persistenceService");

                persistenceService.save(organization);

                status.setRollbackOnly(); // force rollback don't change db.
            }
        });
    }

    public void testDetachedCoaches() throws Exception {
        TransactionTemplate tt = new TransactionTemplate(txManager);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                Mock cycleMock = new Mock(IRequestCycle.class);
                psvc = (PersistenceService) appContext
                        .getBean("persistenceService");

                Organization org = organization.clone();
                org.setId(null);
                org.getDirector().setId(null);
                org.getDirector().setOrganization(org);
                org.getHeader().setId(null);
                org.getPhoto().setId(null);
                org.getLogo().setId(null);
/*

                Set<TeamYear> TeamYears = org.getTeamYears();
                for (Iterator y = TeamYears.iterator(); y.hasNext();) {
                    TeamYear obj = (TeamYear) y.next();
                    obj.setId(null);
                    obj.setOrganization(org);
                }

*/
                Set<Coach> coaches = org.getCoaches();
                for (Iterator c = coaches.iterator(); c.hasNext();) {
                    Coach obj = (Coach) c.next();
                    obj.setId(null);
                    obj.setOrganization(org);
                }
                Set<Team> teams = org.getTeams();
                for (Iterator t = teams.iterator(); t.hasNext();) {
                    Team obj = (Team) t.next();
                    obj.setId(null);
                    obj.setOrganization(org);
                }
                psvc.save(org);

                // onformsubmit
                Mock callbackMock = new Mock(ICallback.class);
                callbackMock.expects(once()).method("performCallback").with(isA(IRequestCycle.class));
                organizationEditPage.setNextPage((ICallback)callbackMock.proxy());
                organizationEditPage.onFormSubmit((IRequestCycle)cycleMock.proxy());
                callbackMock.verify();

                // Now populate page/cycle objects and operate them
                Team localTeam = team.clone(); team.setId(null); team.setOrganization(org); team.setAge(Team.EAge.U19); team.setGender(Team.EGender.MALE);
                teamEditPage.setModel(localTeam);
                persistenceMock.expects(once()).method("save").with(same(localTeam)).will(
                        returnValue(localTeam));
                teamEditPage.save((IRequestCycle) cycleMock.proxy());
                assertEquals(localTeam, teamEditPage.getModel());

                Coach localCoach = coach.clone(); coach.setId(null); coach.setOrganization(org);
                coachEditPage.setModel(localCoach);
                persistenceMock.expects(once()).method("save").with(same(localCoach))
                .will(returnValue(localCoach));
                coachEditPage.save((IRequestCycle) cycleMock.proxy());

                persistenceMock.expects(once()).method("save").with(same(localTeam)).will(
                        returnValue(localTeam));
                localTeam.getCoaches().add(localCoach);
                teamEditPage.save((IRequestCycle) cycleMock.proxy());
                assertEquals(localTeam, teamEditPage.getModel());

                // onformsubmit
                callbackMock.expects(once()).method("performCallback").with(isA(IRequestCycle.class));
                teamEditPage.setNextPage((ICallback)callbackMock.proxy());
                teamEditPage.onFormSubmit((IRequestCycle)cycleMock.proxy());
                callbackMock.verify();

                // org beginRender
                descriptorServiceMock.expects(atLeastOnce()).method("getClassDescriptor").with(eq(Organization.class)).will(returnValue(organizationClassDescriptor));
                Mock pageMock = new Mock(IPage.class);
                pageMock.expects(once()).method("beginPageRender").with(isA(IRequestCycle.class));
                organizationEditPage.getCallbackStack().getStack().clear();
                PageEvent pageEvent = new PageEvent((IPage)pageMock.proxy(), (IRequestCycle)cycleMock.proxy());
                organizationEditPage.pageBeginRender(pageEvent);
                assertNotNull(org.getCoaches());
                assertNotNull(org.getTeams());
                EditCallback poppedCallback = (EditCallback)organizationEditPage.getCallbackStack().getStack().pop();
                assertNotSame(org, poppedCallback.getModel());
                assertTrue(organizationEditPage.getCallbackStack().getStack().isEmpty());
                cycleMock.verify();

                sessionFactory.close(); // trigger detached collection

                // team beginRender
                teamEditPage.setModel(team);
                descriptorServiceMock.expects(atLeastOnce()).method("getClassDescriptor").with(eq(Team.class)).will(returnValue(teamClassDescriptor));
                pageMock = new Mock(IPage.class);
                pageMock.expects(once()).method("beginPageRender").with(isA(IRequestCycle.class));
                teamEditPage.getCallbackStack().getStack().clear();
                pageEvent = new PageEvent((IPage)pageMock.proxy(), (IRequestCycle)cycleMock.proxy());
                teamEditPage.pageBeginRender(pageEvent);
                assertNotNull(team.getCoaches());
                poppedCallback = (EditCallback)teamEditPage.getCallbackStack().getStack().pop();
                assertSame(team, poppedCallback.getModel());
                assertNotNull(team.getCoaches());
                assertTrue(team.getCoaches().iterator().hasNext());
                assertTrue(teamEditPage.getCallbackStack().getStack().isEmpty());
                cycleMock.verify();

                pageMock.expects(once()).method("endPageRender").with(isA(IRequestCycle.class));
                teamEditPage.getCallbackStack().getStack().clear();
                pageEvent = new PageEvent((IPage)pageMock.proxy(), (IRequestCycle)cycleMock.proxy());
                teamEditPage.pageEndRender(pageEvent);
                cycleMock.verify();

                status.setRollbackOnly(); // force rollback don't change db.
            }
        });
    }

    public void setupDomain() {

        for (int i = 500; i < 505; i++) {
            UploadableMedia bytes = new UploadableMedia();

            if (i == 500)
                photo = bytes;
            photo.setId(null);
            photo.setDescription(testString);
            photo.setFileName(testString);
            photo.setFileExtension(testString);
            photo.setFilePath(testString);
            clips.add(photo);
        }

        for (int i = 600; i < 605; i++) {
            PlayerStat stat = new PlayerStat();

            stat.setId(null);
            stats.add(stat);
        }

        teamYear.setYearStart(new Integer("2006"));
        teamYear.setYearEnd(new Integer("2007"));

        teamYear.setId(null);
        teamYears.add(teamYear);

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
//        teamYear.setOrganization(organization);
//        organization.setTeamYears(teamYears);

        for (int i = 700; i < 705; i++) {
            Integer iCounter = new Integer(i);
            Coach coach = new Coach();
            coach.setFirstName(iCounter.toString());
            coach.setLastName(iCounter.toString());
            coach.setPassword(testString);
            coach.setId(null);
            coach.setEmailAddress(coach.getFirstName() + "."
                    + coach.getLastName() + "@" + organization.getName()
                    + ".com");
            coach.setApplicationRole(Person.EApplicationRole.MANAGER); //@note: it was Person.ApplicationRole.HEADCOACH 
            coach.setPassword("test password");
            coach.setPhoto(photo);

            if (i == 700) this.coach = coach;

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
            team.setTeamYear((TeamYear) (teamYears.toArray())[0]);
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

            if ( i == 800 ) this.team = team;

            team.setOrganization(organization);

            if (this.team == null)
                this.team = team;
        }

        organization.setDirector(director);
        organization.setCoaches(coaches);
        organization.setTeams(teams);
    }
}