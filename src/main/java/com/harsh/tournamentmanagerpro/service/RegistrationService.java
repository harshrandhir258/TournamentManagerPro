package com.harsh.tournamentmanagerpro.service;

import com.harsh.tournamentmanagerpro.entity.Registration;

import java.util.List;

public interface RegistrationService {

    Registration registerTeamToTournament(Registration registration);

    List<Registration> getAllRegistrations();

    void deleteRegistration(Long id);

    Registration getRegistrationById(Long id);

    List<Registration> getRegistrationsByTournamentId(Long tournamentId);

    List<Registration> getRegistrationsByTeamId(Long teamId);

    long countRegistrationsByTournamentId(Long tournamentId);
}
