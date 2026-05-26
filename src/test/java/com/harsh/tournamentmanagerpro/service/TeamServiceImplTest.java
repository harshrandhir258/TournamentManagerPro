package com.harsh.tournamentmanagerpro.service;

import com.harsh.tournamentmanagerpro.entity.Role;
import com.harsh.tournamentmanagerpro.entity.Team;
import com.harsh.tournamentmanagerpro.entity.Tournament;
import com.harsh.tournamentmanagerpro.entity.User;
import com.harsh.tournamentmanagerpro.repository.TeamRepository;
import com.harsh.tournamentmanagerpro.service.impl.TeamServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeamServiceImplTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TournamentService tournamentService;

    @InjectMocks
    private TeamServiceImpl teamService;

    private Team team;
    private Tournament tournament;
    private User adminUser;
    private User ownerUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Users
        adminUser = new User("admin", "pass", Role.ADMIN);
        ownerUser = new User("owner", "pass", Role.OWNER);

        // Tournament
        tournament = new Tournament();
        tournament.setName("Summer Cup");
        tournament.setStartDate(LocalDateTime.now().plusDays(1));
        tournament.setEndDate(LocalDateTime.now().plusDays(10));
        tournament.setMaxTeams(10);
        tournament.setCreatedAt(LocalDateTime.now());
        tournament.setUpdatedAt(LocalDateTime.now());
        tournament.setOwner(ownerUser);

        // Team
        team = new Team();
        team.setName("Alpha Team");
        team.setCaptainName("John Doe");
        team.setContactEmail("john@example.com");
        team.setCreatedAt(LocalDateTime.now());
        team.setTournament(tournament);
    }

    // ======================== ADMIN actions ========================
    @Test
    void testAdminCreateTeam() {
        when(tournamentService.getTournamentById(tournament.getId())).thenReturn(tournament);
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Team created = teamService.createTeam(team);

        assertNotNull(created);
        assertEquals("Alpha Team", created.getName());
        assertEquals(tournament, created.getTournament());

        verify(tournamentService).getTournamentById(tournament.getId());
        verify(teamRepository).save(team);
    }

    @Test
    void testAdminUpdateTeam() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Team updatedTeam = new Team();
        updatedTeam.setName("Beta Team");
        updatedTeam.setCaptainName("Jane Doe");
        updatedTeam.setContactEmail("jane@example.com");

        Team updated = teamService.updateTeam(1L, updatedTeam);

        assertEquals("Beta Team", updated.getName());
        assertEquals("Jane Doe", updated.getCaptainName());
        assertEquals("jane@example.com", updated.getContactEmail());
        assertEquals(tournament, updated.getTournament()); // tournament unchanged
    }

    @Test
    void testAdminDeleteTeam() {
        when(teamRepository.existsById(1L)).thenReturn(true);

        teamService.deleteTeam(1L);

        verify(teamRepository).deleteById(1L);
    }

    @Test
    void testAdminGetAllTeams() {
        when(teamRepository.findAll()).thenReturn(Arrays.asList(team));

        List<Team> teams = teamService.getAllTeams();

        assertEquals(1, teams.size());
        assertEquals(team, teams.get(0));
    }

    @Test
    void testAdminGetTeamById() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        Team found = teamService.getTeamById(1L);

        assertEquals(team, found);
    }

    // ======================== OWNER actions ========================
    @Test
    void testOwnerGetTeamsByTournament() {
        when(tournamentService.getTournamentById(tournament.getId())).thenReturn(tournament);
        when(teamRepository.findByTournament(tournament)).thenReturn(Arrays.asList(team));

        List<Team> teams = teamService.getTeamsByTournament(tournament.getId());

        assertEquals(1, teams.size());
        assertEquals(team, teams.get(0));
    }

    // ======================== Negative scenarios ========================
    @Test
    void testDeleteNonExistentTeam() {
        when(teamRepository.existsById(2L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> teamService.deleteTeam(2L));
        assertEquals("Team not found with id: 2", exception.getMessage());
    }

    @Test
    void testGetNonExistentTeamById() {
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> teamService.getTeamById(99L));
        assertEquals("Team not found with id: 99", exception.getMessage());
    }
}
