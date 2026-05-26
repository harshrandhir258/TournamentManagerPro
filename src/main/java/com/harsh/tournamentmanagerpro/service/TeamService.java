package com.harsh.tournamentmanagerpro.service;

import com.harsh.tournamentmanagerpro.entity.Team;

import java.util.List;

public interface TeamService {

    Team createTeam(Team team);

    List<Team> getAllTeams();

    Team getTeamById(Long id);

    Team updateTeam(Long id, Team updatedTeam);

    void deleteTeam(Long id);

    // ✅ NEW: Get all teams for a specific tournament
    List<Team> getTeamsByTournament(Long tournamentId);
}
