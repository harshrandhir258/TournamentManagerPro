package com.harsh.tournamentmanagerpro.service.impl;

import com.harsh.tournamentmanagerpro.entity.Team;
import com.harsh.tournamentmanagerpro.entity.Tournament;
import com.harsh.tournamentmanagerpro.repository.TeamRepository;
import com.harsh.tournamentmanagerpro.service.TeamService;
import com.harsh.tournamentmanagerpro.service.TournamentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TournamentService tournamentService;

    // ✅ Constructor Injection
    public TeamServiceImpl(TeamRepository teamRepository, TournamentService tournamentService) {
        this.teamRepository = teamRepository;
        this.tournamentService = tournamentService;
    }

    // ✅ Only ADMIN will call this
    @Override
    public Team createTeam(Team team) {
        team.setCreatedAt(LocalDateTime.now());

        // Ensure tournament exists (safety check)
        Long tournamentId = team.getTournament().getId();
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        team.setTournament(tournament);

        return teamRepository.save(team);
    }

    @Override
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));
    }

    @Override
    public Team updateTeam(Long id, Team updatedTeam) {
        Team existing = getTeamById(id);

        existing.setName(updatedTeam.getName());
        existing.setCaptainName(updatedTeam.getCaptainName());
        existing.setContactEmail(updatedTeam.getContactEmail());

        // Tournament can't be changed once assigned, so skip setting it again
        return teamRepository.save(existing);
    }

    @Override
    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new RuntimeException("Team not found with id: " + id);
        }
        teamRepository.deleteById(id);
    }

    // ✅ New: Get teams for a specific tournament (used by OWNER)
    @Override
    public List<Team> getTeamsByTournament(Long tournamentId) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        return teamRepository.findByTournament(tournament);
    }
}
