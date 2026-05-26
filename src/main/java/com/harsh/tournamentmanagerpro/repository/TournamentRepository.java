package com.harsh.tournamentmanagerpro.repository;

import com.harsh.tournamentmanagerpro.entity.Tournament;
import com.harsh.tournamentmanagerpro.entity.User;  // updated line importing User entity
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    // ✅ Get all tournaments owned by a particular user (OWNER)
    List<Tournament> findByOwner(User owner);     // updated line new method in repository
}
