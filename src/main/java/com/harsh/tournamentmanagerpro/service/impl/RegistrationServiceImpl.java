package com.harsh.tournamentmanagerpro.service.impl;

import com.harsh.tournamentmanagerpro.entity.Registration;
import com.harsh.tournamentmanagerpro.entity.Tournament;
import com.harsh.tournamentmanagerpro.entity.User;
import com.harsh.tournamentmanagerpro.repository.RegistrationRepository;
import com.harsh.tournamentmanagerpro.service.RegistrationService;
import com.harsh.tournamentmanagerpro.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;

    // ✅ Added: Injected UserService for current user + role logic
    private final UserService userService;

    // ✅ Updated constructor
    public RegistrationServiceImpl(RegistrationRepository registrationRepository,
                                   UserService userService) {
        this.registrationRepository = registrationRepository;
        this.userService = userService;
    }

    // ✅ 1. Register a team to a tournament
    @Override
    public Registration registerTeamToTournament(Registration registration) {
        Long tournamentId = registration.getTournament().getId();
        Long teamId = registration.getTeam().getId();

        boolean alreadyExists = registrationRepository.existsByTournamentIdAndTeamId(tournamentId, teamId);
        if (alreadyExists) {
            throw new RuntimeException("Team is already registered for this tournament.");
        }

        // 🔐 Access Check Start
        User currentUser = userService.getCurrentUser(); // ✅ Get logged-in user
        Tournament tournament = registration.getTournament();

        if (!userService.isAdmin(currentUser) && // ✅ If not admin, must be owner of the tournament
                !tournament.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to register to this tournament.");
        }
        // 🔐 Access Check End

        registration.setRegisteredAt(LocalDateTime.now());
        return registrationRepository.save(registration);
    }

    // ✅ 2. Get all registrations
    @Override
    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    // ✅ 3. Delete registration by ID
    @Override
    public void deleteRegistration(Long id) {
        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found with ID: " + id));

        // 🔐 Access Check Start
        User currentUser = userService.getCurrentUser(); // ✅ Get logged-in user
        Tournament tournament = registration.getTournament();

        if (!userService.isAdmin(currentUser) && // ✅ OWNER only allowed on their own tournament
                !tournament.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to delete this registration.");
        }
        // 🔐 Access Check End

        registrationRepository.deleteById(id);
    }

    // ✅ 4. Get registration by ID
    @Override
    public Registration getRegistrationById(Long id) {
        return registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found with ID: " + id));
    }

    // ✅ 5. Get all registrations for a tournament
    @Override
    public List<Registration> getRegistrationsByTournamentId(Long tournamentId) {
        return registrationRepository.findByTournamentId(tournamentId);
    }

    // ✅ 6. Get all registrations for a team
    @Override
    public List<Registration> getRegistrationsByTeamId(Long teamId) {
        return registrationRepository.findByTeamId(teamId);
    }

    // ✅ 7. Count of registrations in a tournament
    @Override
    public long countRegistrationsByTournamentId(Long tournamentId) {
        return registrationRepository.countByTournamentId(tournamentId);
    }
}
