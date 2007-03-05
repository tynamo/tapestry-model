/*
 * Created on Feb 28, 2005
 *
 * Copyright 2004 Chris Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.trails.callback;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;
import org.jmock.Mock;
import org.trails.component.ComponentTest;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.page.EditPage;
import org.trails.persistence.PersistenceService;
import org.trails.test.Coach;
import org.trails.test.Director;
import org.trails.test.Organization;
import org.trails.test.Person;
import org.trails.test.Player;
import org.trails.test.Statistic;
import org.trails.test.Team;
import org.trails.test.UploadableMedia;
import org.trails.test.Year;

/**
 * Exercise references to collections using classic hibernate roster domain model
 *
 * @author kenneth.colassi    nhhockeyplayer@hotmail.com
 *
 */
public class ReferencedCollectionCallbackTest extends ComponentTest {
    Mock cycleMock = new Mock(IRequestCycle.class);
    Mock validatorMock = new Mock(IValidationDelegate.class);

    IdentifierDescriptor organizationIdDescriptor = new IdentifierDescriptor(Organization.class, "id", Organization.class);
    IdentifierDescriptor yearIdDescriptor = new IdentifierDescriptor(Year.class, "id", Year.class);
    IdentifierDescriptor coachIdDescriptor = new IdentifierDescriptor(Coach.class, "id", Coach.class);
    IdentifierDescriptor teamIdDescriptor = new IdentifierDescriptor(Team.class, "id", Team.class);

    // constants
    private final String testString = "TestData";

    // domain model
    private Organization organization = new Organization();
    private Year year = new Year();
    private Director director = new Director();
    private Set<Year> years = new HashSet<Year>();
    private Set<Coach> coaches = new HashSet<Coach>();
    private Set<Team> teams = new HashSet<Team>();
    private Set<Player> players = new HashSet<Player>();

    // miscellaneous reusable references
    private UploadableMedia photo = new UploadableMedia();
    private Set<UploadableMedia> clips = new HashSet<UploadableMedia>();
    private Set<Statistic> stats = new HashSet<Statistic>();

    String organizationPageName = "organizationEdit";
    String yearPageName = "yearEdit";
    String coachPageName = "coachEdit";
    String teamPageName = "teamEdit";

    EditCallback organizationCallBack = new EditCallback(organizationPageName, new Organization());
    CollectionCallback yearCallBack;
    CollectionCallback coachCallBack;
    CollectionCallback teamCallBack;

    Coach coach = new Coach();
    Team team = new Team();
    EditPage organizationEditPage = (EditPage)creator.newInstance(EditPage.class);
    EditPage yearEditPage = (EditPage)creator.newInstance(EditPage.class);
    EditPage coachEditPage = (EditPage)creator.newInstance(EditPage.class);
    EditPage teamEditPage = (EditPage)creator.newInstance(EditPage.class);

    IClassDescriptor organizationDescriptor = new TrailsClassDescriptor(Organization.class);
    CollectionDescriptor yearCollectionDescriptor = new CollectionDescriptor(Organization.class, "years", Set.class);
    CollectionDescriptor coachCollectionDescriptor = new CollectionDescriptor(Organization.class, "coaches", Set.class);
    CollectionDescriptor teamCollectionDescriptor = new CollectionDescriptor(Organization.class, "teams", Set.class);

    IClassDescriptor yearDescriptor = new TrailsClassDescriptor(Year.class);
    IClassDescriptor coachDescriptor = new TrailsClassDescriptor(Coach.class);
    IClassDescriptor teamDescriptor = new TrailsClassDescriptor(Team.class);

    public void setUp() throws Exception
    {
        yearCollectionDescriptor.setElementType(Year.class);
        yearCollectionDescriptor.setChildRelationship(true);
        coachCollectionDescriptor.setElementType(Coach.class);
        coachCollectionDescriptor.setChildRelationship(true);
        teamCollectionDescriptor.setElementType(Team.class);
        teamCollectionDescriptor.setChildRelationship(true);
        yearDescriptor.setChild(true);
        coachDescriptor.setChild(true);
        teamDescriptor.setChild(true);

        yearCallBack = new CollectionCallback(yearPageName, organization, yearCollectionDescriptor);
        coachCallBack = new CollectionCallback(coachPageName, organization, coachCollectionDescriptor);
        teamCallBack = new CollectionCallback(teamPageName, organization, teamCollectionDescriptor);

        populateDomain();

        organizationDescriptor.getPropertyDescriptors().add(organizationIdDescriptor);
        organizationEditPage = buildEditPage();
        organizationEditPage.setModel(organization);

        yearDescriptor.getPropertyDescriptors().add(yearIdDescriptor);
        yearEditPage = buildEditPage();
        yearEditPage.setModel(year);

        coachDescriptor.getPropertyDescriptors().add(coachIdDescriptor);
        coachEditPage = buildEditPage();
        coachEditPage.setModel(coach);

        teamDescriptor.getPropertyDescriptors().add(teamIdDescriptor);
        teamEditPage = buildEditPage();
        teamEditPage.setModel(team);
    }

    public void testCallback()
    {
        cycleMock.expects(once()).method("getPage").with(eq(organizationPageName)).will(returnValue(organizationEditPage));
        cycleMock.expects(once()).method("getPage").with(eq(yearPageName)).will(returnValue(yearEditPage));
        cycleMock.expects(once()).method("getPage").with(eq(coachPageName)).will(returnValue(coachEditPage));
        cycleMock.expects(once()).method("getPage").with(eq(teamPageName)).will(returnValue(teamEditPage));

        cycleMock.expects(once()).method("activate").with(same(organizationEditPage));
        cycleMock.expects(once()).method("activate").with(same(yearEditPage));
        cycleMock.expects(once()).method("activate").with(same(coachEditPage));
        cycleMock.expects(once()).method("activate").with(same(teamEditPage));

        organizationCallBack.performCallback((IRequestCycle)cycleMock.proxy());
        yearCallBack.performCallback((IRequestCycle)cycleMock.proxy());
        coachCallBack.performCallback((IRequestCycle)cycleMock.proxy());
        teamCallBack.performCallback((IRequestCycle)cycleMock.proxy());
    }

    public void testEditCollectionMemberCallback()
    {
        persistenceMock.expects(once()).method("reload").with(eq(organization)).will(returnValue(organization));
        EditCollectionMemberCallback editYearCollectionMemberCallback = new EditCollectionMemberCallback(organizationPageName, organization, yearCollectionDescriptor);
        editYearCollectionMemberCallback.save((PersistenceService)persistenceMock.proxy(), year);
        assertEquals(1, organization.getYears().size());

        persistenceMock.expects(once()).method("reload").with(eq(organization)).will(returnValue(organization));
        EditCollectionMemberCallback editCoachCollectionMemberCallback = new EditCollectionMemberCallback(organizationPageName, organization, coachCollectionDescriptor);
        editCoachCollectionMemberCallback.save((PersistenceService)persistenceMock.proxy(), coach);
        assertEquals(1, organization.getCoaches().size());

        persistenceMock.expects(once()).method("reload").with(eq(organization)).will(returnValue(organization));
        EditCollectionMemberCallback editTeamCollectionMemberCallback = new EditCollectionMemberCallback(organizationPageName, organization, teamCollectionDescriptor);
        editTeamCollectionMemberCallback.save((PersistenceService)persistenceMock.proxy(), team);
        assertEquals(1, organization.getTeams().size());
    }

    public void testTeamSave() throws Exception {
        Organization localOrganization = new Organization();
        persistenceMock.expects(once()).method("save").with(same(organization))
                .will(returnValue(localOrganization));
        organizationEditPage.save((IRequestCycle) cycleMock.proxy());
        assertEquals(localOrganization, organizationEditPage.getModel());

        Team localTeam = new Team();
        persistenceMock.expects(once()).method("save").with(same(team)).will(
                returnValue(localTeam));
        teamEditPage.save((IRequestCycle) cycleMock.proxy());
        assertEquals(localTeam, teamEditPage.getModel());

        Coach localCoach = new Coach();
        persistenceMock.expects(once()).method("save").with(same(coach)).will(
                returnValue(localCoach));
        coachEditPage.save((IRequestCycle) cycleMock.proxy());
        assertEquals(localCoach, coachEditPage.getModel());

        persistenceMock.expects(once()).method("save").with(same(localTeam))
                .will(returnValue(localTeam));
        localTeam.getCoaches().add(localCoach);
        teamEditPage.save((IRequestCycle) cycleMock.proxy());
        assertEquals(localCoach, localTeam.getCoaches().iterator().next());

        persistenceMock.expects(once()).method("save").with(
                same(localOrganization)).will(returnValue(localOrganization));
        localOrganization.getTeams().add(localTeam);
        organizationEditPage.save((IRequestCycle) cycleMock.proxy());
        assertEquals(localTeam, localOrganization.getTeams().iterator().next());
    }

    private void doCollectionCallback(Object parent, EditPage editPage,
            boolean isChild, CollectionDescriptor collectionDescriptor) {
        CollectionCallback callBack = new CollectionCallback(
                organizationPageName, parent, collectionDescriptor);

        callBack.setChildRelationship(isChild);
        editPage.getCallbackStack().push(callBack);
        editPage.getCallbackStack().push(
                new EditCallback(organizationPageName, new Organization()));
        cycleMock.expects(once()).method("getPage").with(
                eq(organizationPageName)).will(returnValue(editPage));
        cycleMock.expects(once()).method("activate").with(eq(editPage));
    }

    public void testCoachAdd() {
        // instantiate model
        Organization localOrganization = organization.clone();
        Coach localCoach = coach.clone();
        // initialize model
        localOrganization.getCoaches().clear();
        // configure model
        localCoach.setOrganization(localOrganization);
        localOrganization.getCoaches().add(localCoach);

        // pages
        organizationEditPage.setModel(localOrganization);
        coachEditPage.setModel(localCoach);
        // persistence expectations
        persistenceMock.expects(once()).method("save").with(eq(localCoach));
        persistenceMock.expects(once()).method("save").with(
                same(localOrganization)).will(returnValue(localOrganization));
        // operate
        coachEditPage.save((IRequestCycle) cycleMock.proxy());
        organizationEditPage.save((IRequestCycle) cycleMock.proxy());

        assertEquals("coaches collected", 1, localOrganization.getCoaches()
                .size());
    }

    public void testTeams() {
        testTeamAdd();
        /*
        testTeamAdd();
        testTeamAdd();
        testTeamAdd();
        testTeamRemove();
        testTeamRemove();
        testTeamRemove();
        testTeamRemove();
        */
    }

    public void testTeamAdd() {
        // instantiate model
        Organization localOrganization = organization.clone();
        Team localTeam = team.clone();
        // initialize model
        localOrganization.getTeams().clear();
        // configure model
        localTeam.setOrganization(localOrganization);
        localOrganization.getTeams().add(localTeam);

        // pages
        organizationEditPage.setModel(localOrganization);
        teamEditPage.setModel(localTeam);
        // persistence expectations
        persistenceMock.expects(once()).method("save").with(eq(localTeam));
        persistenceMock.expects(once()).method("save").with(
                same(localOrganization)).will(returnValue(localOrganization));
        // operate
        teamEditPage.save((IRequestCycle) cycleMock.proxy());
        organizationEditPage.save((IRequestCycle) cycleMock.proxy());

        assertEquals("teams collected", 1, localOrganization.getTeams().size());
    }

    public void testCoachRemove() {
        Organization localOrganization = organization.clone();
        organizationEditPage.setModel(localOrganization);
        Coach localCoach = coach.clone();
        coachEditPage.setModel(localCoach);

        localOrganization.getCoaches().clear();
        localOrganization.getCoaches().add(localCoach);

        persistenceMock.expects(once()).method("remove").with(eq(localCoach));
        persistenceMock.expects(once()).method("save").with(
                same(localOrganization)).will(returnValue(localOrganization));
        doCollectionCallback(localOrganization, coachEditPage, true,
                coachCollectionDescriptor);
        coachEditPage.remove((IRequestCycle) cycleMock.proxy());

        assertEquals("coaches collected", 0, localOrganization.getCoaches()
                .size());
    }

    public void testTeamRemove() {
        Organization localOrganization = organization.clone();
        organizationEditPage.setModel(localOrganization);
        Team localTeam = team.clone();
        teamEditPage.setModel(localTeam);

        localOrganization.getTeams().clear();
        localOrganization.getTeams().add(localTeam);

        persistenceMock.expects(once()).method("remove").with(eq(localTeam));
        persistenceMock.expects(once()).method("save").with(
                same(localOrganization)).will(returnValue(localOrganization));
        doCollectionCallback(localOrganization, teamEditPage, true,
                teamCollectionDescriptor);
        teamEditPage.remove((IRequestCycle) cycleMock.proxy());

        assertEquals("teams collected", 0, localOrganization.getTeams().size());
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
            Statistic stat = new Statistic();

            stat.setId(null);
            stats.add(stat);
        }

        year.setYearStart(new Integer("2006"));
        year.setYearEnd(new Integer("2007"));
        year.setId(null);
        years.add(year);

        director.setFirstName("Chris");
        director.setLastName("Nelson");
        director.setPassword(testString);
        director.setId(null);
        director.setEmailAddress(director.getFirstName() + "."
                + director.getLastName() + "@" + organization.getName()
                + ".com");
        director.setApplicationRole(Person.ApplicationRole.DIRECTOR);
        director.setPhoto(photo);

        organization = new Organization();
        organization.setName("Trails Sharks");
        organization.setId(null);
        organization.setCity(testString);
        organization.setState(testString);
        organization.setHeader(photo);
        organization.setLogo(photo);
        organization.setPhoto(photo);
        organization.setYears(years);
        year.setOrganization(organization);

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
            coach.setApplicationRole(Person.ApplicationRole.HEADCOACH);
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
            team.setYear((Year) (years.toArray())[0]);
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
                player.setPosition(Player.EPosition.CENTER);
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

        organization.setYears(years);
        organization.setDirector(director);
        organization.setCoaches(coaches);
        organization.setTeams(teams);
    }
}

