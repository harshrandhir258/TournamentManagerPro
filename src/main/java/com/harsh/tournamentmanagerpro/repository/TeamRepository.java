package com.harsh.tournamentmanagerpro.repository;

import com.harsh.tournamentmanagerpro.entity.Team;
import com.harsh.tournamentmanagerpro.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    // ✅ Only what's needed for now

    // Get all teams for a specific tournament (useful for OWNER viewing)
    List<Team> findByTournament(Tournament tournament);
}
