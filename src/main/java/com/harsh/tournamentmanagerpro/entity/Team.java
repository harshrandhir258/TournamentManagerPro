package com.harsh.tournamentmanagerpro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Team name is required")
    @Size(min = 3, max = 100, message = "Team name must be between 3 and 100 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Captain name is required")
    @Size(min = 3, max = 100, message = "Captain name must be between 3 and 100 characters")
    @Column(name = "captain_name", nullable = false)
    private String captainName;

    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid email format")
    @Column(name = "contact_email", nullable = false)
    private String contactEmail;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // ✅ Team belongs to a Tournament (existing one)
    @ManyToOne(fetch = FetchType.LAZY)                          // updated line
    @JoinColumn(name = "tournament_id", nullable = false)       // updated line
    private Tournament tournament;                              // updated line

    // === Constructors ===

    public Team() {
    }

    public Team(String name, String captainName, String contactEmail, LocalDateTime createdAt, Tournament tournament) {
        this.name = name;
        this.captainName = captainName;
        this.contactEmail = contactEmail;
        this.createdAt = createdAt;
        this.tournament = tournament;
    }

    // === Getters and Setters ===

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Tournament getTournament() {                              // updated line
        return tournament;
    }

    public void setTournament(Tournament tournament) {              // updated line
        this.tournament = tournament;
    }
}
