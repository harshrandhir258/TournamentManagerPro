package com.harsh.tournamentmanagerpro.controller;

import com.harsh.tournamentmanagerpro.entity.Tournament;
import com.harsh.tournamentmanagerpro.service.TournamentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    // ✅ 1. Create a new tournament — OWNER only
    @PostMapping
    @PreAuthorize("hasRole('OWNER')") // ✅ Only OWNER can create tournaments
    public ResponseEntity<Tournament> createTournament(@Valid @RequestBody Tournament tournament) {
        Tournament created = tournamentService.createTournament(tournament);
        return ResponseEntity.ok(created);
    }

    // ✅ 2. Get all tournaments — ADMIN only
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // ✅ Only ADMIN can see all tournaments
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        List<Tournament> tournaments = tournamentService.getAllTournaments();
        return ResponseEntity.ok(tournaments);
    }

    // ✅ 3. Get tournaments for current owner
    @GetMapping("/my")
    @PreAuthorize("hasRole('OWNER')") // ✅ Only OWNER gets their own tournaments
    public ResponseEntity<List<Tournament>> getMyTournaments() {
        List<Tournament> tournaments = tournamentService.getTournamentsForCurrentUser();
        return ResponseEntity.ok(tournaments);
    }

    // ✅ 4. Get tournament by ID — ADMIN & OWNER (handled in service logic)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')") // ✅ Access control in service
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        Tournament tournament = tournamentService.getTournamentById(id);
        return ResponseEntity.ok(tournament);
    }

    // ✅ 5. Update tournament by ID — ADMIN or OWNER (only own tournament)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')") // ✅ Ownership check in service
    public ResponseEntity<Tournament> updateTournament(@PathVariable Long id, @Valid @RequestBody Tournament tournament) {
        Tournament updated = tournamentService.updateTournament(id, tournament);
        return ResponseEntity.ok(updated);
    }

    // ✅ 6. Delete tournament by ID — ADMIN or OWNER (only own tournament)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')") // ✅ Ownership check in service
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }
}
