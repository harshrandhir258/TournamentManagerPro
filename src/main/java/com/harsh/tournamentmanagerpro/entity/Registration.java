package com.harsh.tournamentmanagerpro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "registrations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tournament_id", "team_id"})
})
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Tournament is required")
    @ManyToOne(optional = false)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @NotNull(message = "Team is required")
    @ManyToOne(optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    // 🔧 Default constructor (JPA ke liye)
    public Registration() {}

    // 📦 Param constructor (testing/manual object creation ke liye)
    public Registration(Tournament tournament, Team team, LocalDateTime registeredAt) {
        this.tournament = tournament;
        this.team = team;
        this.registeredAt = registeredAt;
    }

    // 🧲 Getters & Setters
    public Long getId() {
        return id;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }
}
