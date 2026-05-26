package com.harsh.tournamentmanagerpro.service.impl;

import com.harsh.tournamentmanagerpro.entity.Tournament;
import com.harsh.tournamentmanagerpro.entity.User;
import com.harsh.tournamentmanagerpro.repository.TournamentRepository;
import com.harsh.tournamentmanagerpro.service.TournamentService;
import com.harsh.tournamentmanagerpro.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final UserService userService;

    // ✅ Constructor Injection
    public TournamentServiceImpl(TournamentRepository tournamentRepository, UserService userService) {
        this.tournamentRepository = tournamentRepository;
        this.userService = userService;
    }

    @Override
    public Tournament createTournament(Tournament tournament) {
        User currentUser = userService.getCurrentUser(); // 🔁 new line — associate owner
        tournament.setOwner(currentUser); // 🔁 updated line — set tournament owner
        tournament.setCreatedAt(LocalDateTime.now());
        tournament.setUpdatedAt(LocalDateTime.now());
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament getTournamentById(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tournament not found with id: " + id));
        User currentUser = userService.getCurrentUser();

        // 🛡️ ADMIN can access any, OWNER only their own
        if (userService.isAdmin(currentUser) || tournament.getOwner().getId().equals(currentUser.getId())) {
            return tournament;
        } else {
            throw new RuntimeException("Access denied: You are not allowed to view this tournament");
        }
    }

    @Override
    public List<Tournament> getAllTournaments() {
        // 👑 Only ADMIN can call this — enforce in controller layer
        return tournamentRepository.findAll();
    }

    @Override
    public List<Tournament> getTournamentsForCurrentUser() {
        // 📌 OWNER can fetch only their own tournaments
        User currentUser = userService.getCurrentUser();
        if (userService.isAdmin(currentUser)) {
            return tournamentRepository.findAll(); // 👑 ADMIN can see all
        } else {
            return tournamentRepository.findAll().stream()
                    .filter(t -> t.getOwner().getId().equals(currentUser.getId()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Tournament updateTournament(Long id, Tournament updated) {
        Tournament existing = getTournamentById(id); // also does access check

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setLocation(updated.getLocation());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setMaxTeams(updated.getMaxTeams());
        existing.setUpdatedAt(LocalDateTime.now());

        return tournamentRepository.save(existing);
    }

    @Override
    public void deleteTournament(Long id) {
        Tournament existing = getTournamentById(id); // also does access check
        tournamentRepository.deleteById(existing.getId());
    }
}
