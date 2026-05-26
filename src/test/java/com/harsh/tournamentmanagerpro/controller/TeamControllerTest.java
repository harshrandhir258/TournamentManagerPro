package com.harsh.tournamentmanagerpro.controller;

import com.harsh.tournamentmanagerpro.entity.Team;
import com.harsh.tournamentmanagerpro.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TeamControllerTest {

    @Mock
    private TeamService teamService;

    @InjectMocks
    private TeamController teamController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(teamController).build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateTeam_Admin() throws Exception {
        Team team = new Team();
        team.setName("Alpha Team");
        team.setCaptainName("John Doe");
        team.setContactEmail("john@example.com");

        when(teamService.createTeam(any(Team.class))).thenReturn(team);

        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Alpha Team\",\"captainName\":\"John Doe\",\"contactEmail\":\"john@example.com\"}")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(teamService, times(1)).createTeam(any(Team.class));
    }

    @Test
    @WithMockUser(username = "owner", roles = {"OWNER"})
    void testOwnerCannotCreateTeam() throws Exception {
        mockMvc.perform(post("/api/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Alpha Team\",\"captainName\":\"John Doe\",\"contactEmail\":\"john@example.com\"}")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllTeams_Admin() throws Exception {
        when(teamService.getAllTeams()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/teams")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "owner", roles = {"OWNER"})
    void testOwnerCannotGetAllTeams() throws Exception {
        mockMvc.perform(get("/api/teams")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateTeam_Admin() throws Exception {
        Team team = new Team();
        team.setName("Beta Team");
        team.setCaptainName("Jane Doe");
        team.setContactEmail("jane@example.com");

        when(teamService.updateTeam(anyLong(), any(Team.class))).thenReturn(team);

        mockMvc.perform(put("/api/teams/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Beta Team\",\"captainName\":\"Jane Doe\",\"contactEmail\":\"jane@example.com\"}")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "owner", roles = {"OWNER"})
    void testOwnerCannotUpdateTeam() throws Exception {
        mockMvc.perform(put("/api/teams/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Beta Team\",\"captainName\":\"Jane Doe\",\"contactEmail\":\"jane@example.com\"}")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteTeam_Admin() throws Exception {
        doNothing().when(teamService).deleteTeam(anyLong());

        mockMvc.perform(delete("/api/teams/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "owner", roles = {"OWNER"})
    void testOwnerCannotDeleteTeam() throws Exception {
        mockMvc.perform(delete("/api/teams/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
}
