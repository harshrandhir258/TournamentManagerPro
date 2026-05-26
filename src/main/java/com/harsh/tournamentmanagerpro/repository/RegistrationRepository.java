package com.harsh.tournamentmanagerpro.repository;

import com.harsh.tournamentmanagerpro.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByTournamentIdAndTeamId(Long tournamentId, Long teamId);

    // 🔍 Get all registrations for a given tournament
    List<Registration> findByTournamentId(Long tournamentId);

    // 🔍 Get all registrations for a given team
    List<Registration> findByTeamId(Long teamId);

    // 🔢 Count registrations for a tournament
    long countByTournamentId(Long tournamentId);
}
