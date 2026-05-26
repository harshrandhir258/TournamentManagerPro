package com.harsh.tournamentmanagerpro.service;

import com.harsh.tournamentmanagerpro.entity.Role;
import com.harsh.tournamentmanagerpro.entity.Tournament;
import com.harsh.tournamentmanagerpro.entity.User;
import com.harsh.tournamentmanagerpro.repository.TournamentRepository;
import com.harsh.tournamentmanagerpro.service.impl.TournamentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TournamentServiceImplTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    private User owner;
    private User admin;
    private Tournament tournament;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Owner setup
        owner = new User();
        owner.setUsername("owner1");
        owner.setRole(Role.OWNER);
        owner.setId(1L); // simulate auto-generated ID

        // Admin setup
        admin = new User();
        admin.setUsername("admin1");
        admin.setRole(Role.ADMIN);
        admin.setId(2L);

        // Tournament setup
        tournament = new Tournament();
        tournament.setName("Test Tournament");
        tournament.setDescription("Desc");
        tournament.setLocation("Location");
        tournament.setStartDate(LocalDateTime.now().plusDays(1));
        tournament.setEndDate(LocalDateTime.now().plusDays(2));
        tournament.setMaxTeams(10);
        tournament.setOwner(owner);
    }

    // Helper to set Tournament ID via reflection
    private void setTournamentId(Tournament tournament, Long id) {
        try {
            Field idField = Tournament.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(tournament, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCreateTournament() {
        when(userService.getCurrentUser()).thenReturn(owner);
        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(invocation -> {
            Tournament t = invocation.getArgument(0);
            setTournamentId(t, 100L);
            return t;
        });

        Tournament created = tournamentService.createTournament(tournament);

        assertNotNull(created);
        assertEquals("Test Tournament", created.getName());
        assertEquals(owner, created.getOwner());
        assertEquals(100L, created.getId());
        verify(tournamentRepository, times(1)).save(any(Tournament.class));
    }

    @Test
    void testGetTournamentById_AdminAccess() {
        setTournamentId(tournament, 10L);
        when(tournamentRepository.findById(10L)).thenReturn(Optional.of(tournament));
        when(userService.getCurrentUser()).thenReturn(admin);
        when(userService.isAdmin(admin)).thenReturn(true);

        Tournament t = tournamentService.getTournamentById(10L);
        assertEquals(tournament, t);
    }

    @Test
    void testGetTournamentById_OwnerAccess() {
        setTournamentId(tournament, 10L);
        when(tournamentRepository.findById(10L)).thenReturn(Optional.of(tournament));
        when(userService.getCurrentUser()).thenReturn(owner);
        when(userService.isAdmin(owner)).thenReturn(false);

        Tournament t = tournamentService.getTournamentById(10L);
        assertEquals(tournament, t);
    }

    @Test
    void testGetTournamentById_AccessDenied() {
        setTournamentId(tournament, 10L);
        User otherUser = new User();
        otherUser.setId(99L);
        otherUser.setRole(Role.OWNER);

        when(tournamentRepository.findById(10L)).thenReturn(Optional.of(tournament));
        when(userService.getCurrentUser()).thenReturn(otherUser);
        when(userService.isAdmin(otherUser)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                tournamentService.getTournamentById(10L));
        assertEquals("Access denied: You are not allowed to view this tournament", ex.getMessage());
    }

    @Test
    void testGetAllTournaments() {
        when(tournamentRepository.findAll()).thenReturn(Arrays.asList(tournament));
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        assertEquals(1, tournaments.size());
        assertEquals(tournament, tournaments.get(0));
    }

    @Test
    void testGetTournamentsForCurrentUser_Admin() {
        when(userService.getCurrentUser()).thenReturn(admin);
        when(userService.isAdmin(admin)).thenReturn(true);
        when(tournamentRepository.findAll()).thenReturn(Arrays.asList(tournament));

        List<Tournament> tournaments = tournamentService.getTournamentsForCurrentUser();
        assertEquals(1, tournaments.size());
        assertEquals(tournament, tournaments.get(0));
    }

    @Test
    void testGetTournamentsForCurrentUser_Owner() {
        Tournament otherTournament = new Tournament();
        otherTournament.setOwner(admin);
        setTournamentId(otherTournament, 20L);

        when(userService.getCurrentUser()).thenReturn(owner);
        when(userService.isAdmin(owner)).thenReturn(false);
        when(tournamentRepository.findAll()).thenReturn(Arrays.asList(tournament, otherTournament));

        List<Tournament> tournaments = tournamentService.getTournamentsForCurrentUser();
        assertEquals(1, tournaments.size());
        assertEquals(tournament, tournaments.get(0)); // only owner tournament
    }

    @Test
    void testUpdateTournament() {
        setTournamentId(tournament, 10L);
        Tournament updated = new Tournament();
        updated.setName("Updated Name");
        updated.setDescription("Updated Desc");
        updated.setLocation("Updated Loc");
        updated.setStartDate(LocalDateTime.now().plusDays(3));
        updated.setEndDate(LocalDateTime.now().plusDays(4));
        updated.setMaxTeams(20);

        when(tournamentRepository.findById(10L)).thenReturn(Optional.of(tournament));
        when(userService.getCurrentUser()).thenReturn(owner);
        when(userService.isAdmin(owner)).thenReturn(false);
        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tournament result = tournamentService.updateTournament(10L, updated);
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Desc", result.getDescription());
        assertEquals("Updated Loc", result.getLocation());
        assertEquals(20, result.getMaxTeams());
        verify(tournamentRepository, times(1)).save(any(Tournament.class));
    }

    @Test
    void testDeleteTournament() {
        setTournamentId(tournament, 10L);

        when(tournamentRepository.findById(10L)).thenReturn(Optional.of(tournament));
        when(userService.getCurrentUser()).thenReturn(owner);
        when(userService.isAdmin(owner)).thenReturn(false);

        tournamentService.deleteTournament(10L);
        verify(tournamentRepository, times(1)).deleteById(10L);
    }

    @Test
    void testDeleteTournament_AccessDenied() {
        setTournamentId(tournament, 10L);
        User otherUser = new User();
        otherUser.setId(99L);
        otherUser.setRole(Role.OWNER);

        when(tournamentRepository.findById(10L)).thenReturn(Optional.of(tournament));
        when(userService.getCurrentUser()).thenReturn(otherUser);
        when(userService.isAdmin(otherUser)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> tournamentService.deleteTournament(10L));
        assertEquals("Access denied: You are not allowed to view this tournament", ex.getMessage());
    }
}
