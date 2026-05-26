package com.harsh.tournamentmanagerpro.service;

import com.harsh.tournamentmanagerpro.entity.Tournament;

import java.util.List;

public interface TournamentService {

    Tournament createTournament(Tournament tournament);

    Tournament getTournamentById(Long id);

    List<Tournament> getAllTournaments(); // ADMIN only

    List<Tournament> getTournamentsForCurrentUser(); // OWNER — apne tournament   // updated line

    Tournament updateTournament(Long id, Tournament tournament);

    void deleteTournament(Long id);
}
