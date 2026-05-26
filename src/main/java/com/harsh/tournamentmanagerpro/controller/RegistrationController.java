package com.harsh.tournamentmanagerpro.controller;

import com.harsh.tournamentmanagerpro.entity.Registration;
import com.harsh.tournamentmanagerpro.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // ✅ Add this
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    // ✅ 1. Register a team to a tournament
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')") // ✅ Only ADMIN or OWNER
    public ResponseEntity<Registration> registerTeam(@Valid @RequestBody Registration registration) {
        Registration saved = registrationService.registerTeamToTournament(registration);
        return ResponseEntity.ok(saved);
    }

    // ✅ 2. Get all registrations — ADMIN only
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // ✅ Only ADMIN
    public ResponseEntity<List<Registration>> getAllRegistrations() {
        return ResponseEntity.ok(registrationService.getAllRegistrations());
    }

    // ✅ 3. Delete registration by ID — ADMIN or OWNER of that tournament
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')") // ✅ Allowed, actual check in service
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        registrationService.deleteRegistration(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ 4. Get registration by ID — ADMIN only (can be extended later)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // 🔒 Optional: you can relax later
    public ResponseEntity<Registration> getRegistrationById(@PathVariable Long id) {
        return ResponseEntity.ok(registrationService.getRegistrationById(id));
    }

    // ✅ 5. Get all registrations for a tournament — OWNER + ADMIN
    @GetMapping("/tournament/{tournamentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')") // ✅ Owner access check is in service (if needed)
    public ResponseEntity<List<Registration>> getRegistrationsByTournamentId(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(registrationService.getRegistrationsByTournamentId(tournamentId));
    }

    // ✅ 6. Get all registrations for a team — ADMIN only
    @GetMapping("/team/{teamId}")
    @PreAuthorize("hasRole('ADMIN')") // ✅ Only ADMIN
    public ResponseEntity<List<Registration>> getRegistrationsByTeamId(@PathVariable Long teamId) {
        return ResponseEntity.ok(registrationService.getRegistrationsByTeamId(teamId));
    }

    // ✅ 7. Count registrations — OWNER + ADMIN

    @GetMapping("/count/tournament/{tournamentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')") // ✅ Owner will be verified in service layer
    public ResponseEntity<Long> countRegistrationsInTournament(@PathVariable Long tournamentId) {
        return ResponseEntity.ok(registrationService.countRegistrationsByTournamentId(tournamentId));
    }
}
