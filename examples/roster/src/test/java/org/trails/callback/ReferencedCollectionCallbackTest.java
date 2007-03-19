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

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.valid.IValidationDelegate;
import org.jmock.Mock;
import org.trails.demo.*;
import org.trails.descriptor.CollectionDescriptor;
import org.trails.descriptor.IClassDescriptor;
import org.trails.descriptor.IdentifierDescriptor;
import org.trails.descriptor.TrailsClassDescriptor;
import org.trails.page.EditPage;
import org.trails.persistence.PersistenceService;
import org.trails.component.ComponentTest;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Exercise references to collections using classic hibernate roster domain model
 *
 * @author kenneth.colassi    nhhockeyplayer@hotmail.com
 *
 */
public class ReferencedCollectionCallbackTest extends ComponentTest
{
    Mock cycleMock = new Mock(IRequestCycle.class);
    Mock validatorMock = new Mock(IValidationDelegate.class);

    IdentifierDescriptor organizationIdDescriptor = new IdentifierDescriptor(Organization.class, "id", Organization.class);
    IdentifierDescriptor teamYearIdDescriptor = new IdentifierDescriptor(TeamYear.class, "id", TeamYear.class);
    IdentifierDescriptor coachIdDescriptor = new IdentifierDescriptor(Coach.class, "id", Coach.class);
    IdentifierDescriptor teamIdDescriptor = new IdentifierDescriptor(Team.class, "id", Team.class);

    // constants
    private final String testString = "TestData";

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

    String organizationPageName = "organizationEdit";
    String teamYearPageName = "TeamYearEdit";
    String coachPageName = "coachEdit";
    String teamPageName = "teamEdit";

    EditCallback organizationCallBack = new EditCallback(organizationPageName, new Organization());
    CollectionCallback teamYearCallBack;
    CollectionCallback coachCallBack;
    CollectionCallback teamCallBack;

    Coach coach = new Coach();
    Team team = new Team();
    EditPage organizationEditPage = (EditPage)creator.newInstance(EditPage.class);
    EditPage teamYearEditPage = (EditPage)creator.newInstance(EditPage.class);
    EditPage coachEditPage = (EditPage)creator.newInstance(EditPage.class);
    EditPage teamEditPage = (EditPage)creator.newInstance(EditPage.class);

    IClassDescriptor organizationDescriptor = new TrailsClassDescriptor(Organization.class);
    CollectionDescriptor teamYearCollectionDescriptor = new CollectionDescriptor(Organization.class, "teamYears", Set.class);
    CollectionDescriptor coachCollectionDescriptor = new CollectionDescriptor(Organization.class, "coaches", Set.class);
    CollectionDescriptor teamCollectionDescriptor = new CollectionDescriptor(Organization.class, "teams", Set.class);

    IClassDescriptor teamYearDescriptor = new TrailsClassDescriptor(TeamYear.class);
    IClassDescriptor coachDescriptor = new TrailsClassDescriptor(Coach.class);
    IClassDescriptor teamDescriptor = new TrailsClassDescriptor(Team.class);

    public void setUp() throws Exception
    {
        teamYearCollectionDescriptor.setElementType(TeamYear.class);
        teamYearCollectionDescriptor.setChildRelationship(true);
        coachCollectionDescriptor.setElementType(Coach.class);
        coachCollectionDescriptor.setChildRelationship(true);
        teamCollectionDescriptor.setElementType(Team.class);
        teamCollectionDescriptor.setChildRelationship(true);
        teamYearDescriptor.setChild(true);
        coachDescriptor.setChild(true);
        teamDescriptor.setChild(true);

        teamYearCallBack = new CollectionCallback(teamYearPageName, organization, teamYearCollectionDescriptor);
        coachCallBack = new CollectionCallback(coachPageName, organization, coachCollectionDescriptor);
        teamCallBack = new CollectionCallback(teamPageName, organization, teamCollectionDescriptor);

        populateDomain();

        organizationDescriptor.getPropertyDescriptors().add(organizationIdDescriptor);
        organizationEditPage = buildEditPage();
        organizationEditPage.setModel(organization);

        teamYearDescriptor.getPropertyDescriptors().add(teamYearIdDescriptor);
        teamYearEditPage = buildEditPage();
        teamYearEditPage.setModel(teamYear);

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
        cycleMock.expects(once()).method("getPage").with(eq(teamYearPageName)).will(returnValue(teamYearEditPage));
        cycleMock.expects(once()).method("getPage").with(eq(coachPageName)).will(returnValue(coachEditPage));
        cycleMock.expects(once()).method("getPage").with(eq(teamPageName)).will(returnValue(teamEditPage));

        cycleMock.expects(once()).method("activate").with(same(organizationEditPage));
        cycleMock.expects(once()).method("activate").with(same(teamYearEditPage));
        cycleMock.expects(once()).method("activate").with(same(coachEditPage));
        cycleMock.expects(once()).method("activate").with(same(teamEditPage));

        organizationCallBack.performCallback((IRequestCycle)cycleMock.proxy());
        teamYearCallBack.performCallback((IRequestCycle)cycleMock.proxy());
        coachCallBack.performCallback((IRequestCycle)cycleMock.proxy());
        teamCallBack.performCallback((IRequestCycle)cycleMock.proxy());
    }

    public void testEditCollectionMemberCallback()
    {
        persistenceMock.expects(once()).method("reload").with(eq(organization)).will(returnValue(organization));
        EditCollectionMemberCallback editTeamYearCollectionMemberCallback = new EditCollectionMemberCallback(organizationPageName, organization, teamYearCollectionDescriptor);
        editTeamYearCollectionMemberCallback.save((PersistenceService)persistenceMock.proxy(), teamYear);
//        assertEquals(1, organization.getTeamYears().size());

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
//        organization.setTeamYears(teamYears);
//        teamYear.setOrganization(organization);

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
            coach.setApplicationRole(Person.EApplicationRole.MANAGER); //@note: it was Person.ApplicationRole.HEADCOACH
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
            team.setOrganization(organization);

            if ( this.team == null ) this.team = team;
        }

        organization.setDirector(director);
        organization.setCoaches(coaches);
        organization.setTeams(teams);
    }
}

