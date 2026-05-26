package com.harsh.tournamentmanagerpro.controller;

import com.harsh.tournamentmanagerpro.entity.Team;
import com.harsh.tournamentmanagerpro.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    // 1️⃣ Create a new team — ADMIN only
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Team> createTeam(@Valid @RequestBody Team team) {
        Team createdTeam = teamService.createTeam(team);
        return ResponseEntity.ok(createdTeam);
    }

    // 2️⃣ Get all teams — ADMIN only
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    // 3️⃣ Get team by ID — ADMIN only
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        Team team = teamService.getTeamById(id);
        return ResponseEntity.ok(team);
    }

    // 4️⃣ Update team — ADMIN only
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id, @Valid @RequestBody Team team) {
        Team updatedTeam = teamService.updateTeam(id, team);
        return ResponseEntity.ok(updatedTeam);
    }

    // 5️⃣ Delete team — ADMIN only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }

    // 6️⃣ Get teams by Tournament ID — ADMIN or OWNER
    @GetMapping("/tournament/{tournamentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public ResponseEntity<List<Team>> getTeamsByTournament(@PathVariable Long tournamentId) {
        List<Team> teams = teamService.getTeamsByTournament(tournamentId);
        return ResponseEntity.ok(teams);
    }
}
