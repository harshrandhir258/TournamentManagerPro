package com.harsh.tournamentmanagerpro.service;

import com.harsh.tournamentmanagerpro.entity.*;
import com.harsh.tournamentmanagerpro.repository.RegistrationRepository;
import com.harsh.tournamentmanagerpro.service.impl.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationServiceImplTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    private User adminUser;
    private User ownerUser;
    private User otherOwnerUser;
    private Tournament tournament;
    private Team team;
    private Registration registration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // ✅ Users
        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setUsername("admin");
        adminUser.setRole(Role.ADMIN);

        ownerUser = new User();
        ownerUser.setId(2L);
        ownerUser.setUsername("owner");
        ownerUser.setRole(Role.OWNER);

        otherOwnerUser = new User();
        otherOwnerUser.setId(3L);
        otherOwnerUser.setUsername("otherOwner");
        otherOwnerUser.setRole(Role.OWNER);

        // ✅ Tournament (full details)
        tournament = new Tournament();
        tournament.setName("Champions League");
        tournament.setDescription("Annual international tournament");
        tournament.setLocation("London, UK");
        tournament.setStartDate(LocalDateTime.of(2025, 9, 1, 10, 0));
        tournament.setEndDate(LocalDateTime.of(2025, 9, 15, 18, 0));
        tournament.setMaxTeams(32);
        tournament.setCreatedAt(LocalDateTime.now().minusDays(10));
        tournament.setUpdatedAt(LocalDateTime.now());
        tournament.setOwner(ownerUser);

        // ✅ Team (detailed)
        team = new Team();
        team.setName("Red Dragons");
        team.setCaptainName("John Doe");
        team.setContactEmail("red.dragons@example.com");
        team.setCreatedAt(LocalDateTime.now().minusDays(5));
        team.setTournament(tournament);

        // ✅ Registration (detailed)
        registration = new Registration();
        registration.setTeam(team);
        registration.setTournament(tournament);
        registration.setRegisteredAt(LocalDateTime.now().minusDays(1));
    }

    // ========================= TESTS ==============================

    @Test
    void testRegisterTeam_AdminCanRegister() {
        when(userService.getCurrentUser()).thenReturn(adminUser);
        when(registrationRepository.existsByTournamentIdAndTeamId(tournament.getId(), team.getId()))
                .thenReturn(false);
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration saved = registrationService.registerTeamToTournament(registration);

        assertNotNull(saved);
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void testRegisterTeam_OwnerCanRegisterOwnTournament() {
        when(userService.getCurrentUser()).thenReturn(ownerUser);
        when(registrationRepository.existsByTournamentIdAndTeamId(tournament.getId(), team.getId()))
                .thenReturn(false);
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration saved = registrationService.registerTeamToTournament(registration);

        assertNotNull(saved);
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void testRegisterTeam_OwnerCannotRegisterOtherTournament() {
        when(userService.getCurrentUser()).thenReturn(otherOwnerUser);
        when(registrationRepository.existsByTournamentIdAndTeamId(tournament.getId(), team.getId()))
                .thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> registrationService.registerTeamToTournament(registration));
        assertEquals("You are not allowed to register to this tournament.", exception.getMessage());
        verify(registrationRepository, never()).save(any());
    }

    @Test
    void testRegisterTeam_DuplicateRegistrationThrows() {
        when(userService.getCurrentUser()).thenReturn(adminUser);
        when(registrationRepository.existsByTournamentIdAndTeamId(tournament.getId(), team.getId()))
                .thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> registrationService.registerTeamToTournament(registration));
        assertEquals("Team is already registered for this tournament.", exception.getMessage());
        verify(registrationRepository, never()).save(any());
    }

    @Test
    void testDeleteRegistration_AdminCanDelete() {
        when(registrationRepository.findById(registration.getId())).thenReturn(Optional.of(registration));
        when(userService.getCurrentUser()).thenReturn(adminUser);

        registrationService.deleteRegistration(registration.getId());

        verify(registrationRepository, times(1)).deleteById(registration.getId());
    }

    @Test
    void testDeleteRegistration_OwnerCanDeleteOwnTournament() {
        when(registrationRepository.findById(registration.getId())).thenReturn(Optional.of(registration));
        when(userService.getCurrentUser()).thenReturn(ownerUser);

        registrationService.deleteRegistration(registration.getId());

        verify(registrationRepository, times(1)).deleteById(registration.getId());
    }

    @Test
    void testDeleteRegistration_OwnerCannotDeleteOtherTournament() {
        when(registrationRepository.findById(registration.getId())).thenReturn(Optional.of(registration));
        when(userService.getCurrentUser()).thenReturn(otherOwnerUser);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> registrationService.deleteRegistration(registration.getId()));
        assertEquals("You are not allowed to delete this registration.", exception.getMessage());
        verify(registrationRepository, never()).deleteById(any());
    }

    @Test
    void testGetAllRegistrations() {
        List<Registration> list = new ArrayList<>();
        list.add(registration);
        when(registrationRepository.findAll()).thenReturn(list);

        List<Registration> result = registrationService.getAllRegistrations();

        assertEquals(1, result.size());
        assertEquals(registration, result.get(0));
    }

    @Test
    void testGetRegistrationById_Found() {
        when(registrationRepository.findById(registration.getId())).thenReturn(Optional.of(registration));

        Registration found = registrationService.getRegistrationById(registration.getId());

        assertEquals(registration, found);
    }

    @Test
    void testGetRegistrationById_NotFound() {
        when(registrationRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> registrationService.getRegistrationById(999L));
        assertEquals("Registration not found with ID: 999", exception.getMessage());
    }

    @Test
    void testGetRegistrationsByTournamentId() {
        List<Registration> list = new ArrayList<>();
        list.add(registration);
        when(registrationRepository.findByTournamentId(tournament.getId())).thenReturn(list);

        List<Registration> result = registrationService.getRegistrationsByTournamentId(tournament.getId());

        assertEquals(1, result.size());
        assertEquals(registration, result.get(0));
    }

    @Test
    void testGetRegistrationsByTeamId() {
        List<Registration> list = new ArrayList<>();
        list.add(registration);
        when(registrationRepository.findByTeamId(team.getId())).thenReturn(list);

        List<Registration> result = registrationService.getRegistrationsByTeamId(team.getId());

        assertEquals(1, result.size());
        assertEquals(registration, result.get(0));
    }

    @Test
    void testCountRegistrationsByTournamentId() {
        when(registrationRepository.countByTournamentId(tournament.getId())).thenReturn(5L);

        long count = registrationService.countRegistrationsByTournamentId(tournament.getId());

        assertEquals(5L, count);
    }
}
