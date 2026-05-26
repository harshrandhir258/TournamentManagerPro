package com.harsh.tournamentmanagerpro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tournament name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @Size(max = 500, message = "Description can't exceed 500 character")
    @Column(length = 500)
    private String description;

    @Size(max = 500, message = "Location can't exceed 500 character")
    @Column(length = 500)
    private String location;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @NotNull(message = "Max teams is required")
    @Min(value = 2, message = "Minimum 2 teams required")
    @Max(value = 100, message = "Maximum 100 teams allowed")
    @Column(name = "max_teams", nullable = false)
    private Integer maxTeams;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @NotNull(message = "Owner is required")                        // ye hai updated code jab login security add kkiya tournament access sif uska owner he kar sakte hai
    @ManyToOne(fetch = FetchType.LAZY, optional = false)           // updated line  FetchType.LAZY meaning ye hai ki jab tu function call karega jidr sachi tereko owner ki need hai tabhi he voh db se ye value fetch karegi nhi toh agar jarurat lagi toh lagech value nikalegi
    @JoinColumn(name = "owner_id", nullable = false)               // updated line
    private User owner;                                            // updated line
    public Tournament() {
    }

    public Tournament(String name, String description, String location,
                      LocalDateTime startDate, LocalDateTime endDate,
                      Integer maxTeams, LocalDateTime createdAt, LocalDateTime updatedAt,
                      User owner) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxTeams = maxTeams;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getMaxTeams() {
        return maxTeams;
    }
    public void setMaxTeams(Integer maxTeams) {
        this.maxTeams = maxTeams;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getOwner() {                                                     // updated line
        return owner;
    }
    public void setOwner(User owner) {                                           // updated line
        this.owner = owner;
    }
}
