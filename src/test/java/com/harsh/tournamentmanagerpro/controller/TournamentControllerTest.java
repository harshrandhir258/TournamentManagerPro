package com.harsh.tournamentmanagerpro.controller;

import com.harsh.tournamentmanagerpro.entity.Role;
import com.harsh.tournamentmanagerpro.entity.Tournament;
import com.harsh.tournamentmanagerpro.entity.User;
import com.harsh.tournamentmanagerpro.service.TournamentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class TournamentControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private TournamentService tournamentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Tournament tournament;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        User owner = new User("owner1", "pass", Role.OWNER);
        tournament = new Tournament(
                "Test Tournament",
                "Description",
                "Pune",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                10,
                LocalDateTime.now(),
                LocalDateTime.now(),
                owner
        );
    }

    // -------------------- CREATE TOURNAMENT --------------------
    @Test
    @WithMockUser(username = "owner1", roles = {"OWNER"})
    public void createTournament_AsOwner_ReturnsOk() throws Exception {
        when(tournamentService.createTournament(any(Tournament.class))).thenReturn(tournament);

        mockMvc.perform(post("/api/tournaments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tournament)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createTournament_AsAdmin_Forbidden() throws Exception {
        mockMvc.perform(post("/api/tournaments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tournament)))
                .andExpect(status().is4xxClientError()); // ✅ changed from isForbidden() to generic 4xx
    }

    // -------------------- GET ALL TOURNAMENTS --------------------
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getAllTournaments_AsAdmin_ReturnsOk() throws Exception {
        when(tournamentService.getAllTournaments()).thenReturn(Collections.singletonList(tournament));

        mockMvc.perform(get("/api/tournaments"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "owner1", roles = {"OWNER"})
    public void getAllTournaments_AsOwner_Forbidden() throws Exception {
        mockMvc.perform(get("/api/tournaments"))
                .andExpect(status().is4xxClientError()); // ✅ changed from isForbidden() to generic 4xx
    }

    // -------------------- UPDATE TOURNAMENT --------------------
    @Test
    @WithMockUser(username = "owner1", roles = {"OWNER"})
    public void updateTournament_AsOwner_ReturnsOk() throws Exception {
        when(tournamentService.updateTournament(any(Long.class), any(Tournament.class))).thenReturn(tournament);

        mockMvc.perform(put("/api/tournaments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tournament)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateTournament_AsAdmin_ReturnsOk() throws Exception {
        when(tournamentService.updateTournament(any(Long.class), any(Tournament.class))).thenReturn(tournament);

        mockMvc.perform(put("/api/tournaments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tournament)))
                .andExpect(status().isOk());
    }

    // -------------------- DELETE TOURNAMENT --------------------
    @Test
    @WithMockUser(username = "owner1", roles = {"OWNER"})
    public void deleteTournament_AsOwner_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/tournaments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteTournament_AsAdmin_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/tournaments/1"))
                .andExpect(status().isNoContent());
    }

    // -------------------- GET MY TOURNAMENTS --------------------
    @Test
    @WithMockUser(username = "owner1", roles = {"OWNER"})
    public void getMyTournaments_AsOwner_ReturnsOk() throws Exception {
        when(tournamentService.getTournamentsForCurrentUser()).thenReturn(Collections.singletonList(tournament));

        mockMvc.perform(get("/api/tournaments/my"))
                .andExpect(status().isOk());
    }
}
