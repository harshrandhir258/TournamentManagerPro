package com.harsh.tournamentmanagerpro.controller;

import com.harsh.tournamentmanagerpro.entity.Registration;
import com.harsh.tournamentmanagerpro.service.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    private Registration registration;

    @BeforeEach
    void setUp() {
        registration = new Registration();
    }

    // Helper method to simulate auto-generated ID
    private Registration withId(long id) {
        return new Registration() {
            @Override
            public Long getId() {
                return id;
            }
        };
    }

    // 1. POST /api/registrations — ADMIN allowed
    @Test
    @WithMockUser(roles = "ADMIN")
    void registerTeam_Admin_Success() throws Exception {
        Mockito.when(registrationService.registerTeamToTournament(any()))
                .thenReturn(withId(1L));

        mockMvc.perform(post("/api/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tournament\":{}, \"team\":{}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // 1b. OWNER allowed
    @Test
    @WithMockUser(roles = "OWNER")
    void registerTeam_Owner_Success() throws Exception {
        Mockito.when(registrationService.registerTeamToTournament(any()))
                .thenReturn(withId(2L));

        mockMvc.perform(post("/api/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tournament\":{}, \"team\":{}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }

    // 1c. Unauthorized
    @Test
    void registerTeam_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tournament\":{}, \"team\":{}}"))
                .andExpect(status().isUnauthorized());
    }

    // 2. GET all registrations — ADMIN only
    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllRegistrations_Admin_Success() throws Exception {
        List<Registration> list = Arrays.asList(withId(1L));
        Mockito.when(registrationService.getAllRegistrations()).thenReturn(list);

        mockMvc.perform(get("/api/registrations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void getAllRegistrations_Owner_Forbidden() throws Exception {
        mockMvc.perform(get("/api/registrations"))
                .andExpect(status().isForbidden());
    }

    // 3. DELETE registration — ADMIN or OWNER
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteRegistration_Admin_Success() throws Exception {
        mockMvc.perform(delete("/api/registrations/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(registrationService).deleteRegistration(1L);
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void deleteRegistration_Owner_Success() throws Exception {
        mockMvc.perform(delete("/api/registrations/1"))
                .andExpect(status().isNoContent());
    }

    // 4. GET registration by ID — ADMIN only
    @Test
    @WithMockUser(roles = "ADMIN")
    void getRegistrationById_Admin_Success() throws Exception {
        Mockito.when(registrationService.getRegistrationById(1L)).thenReturn(withId(1L));

        mockMvc.perform(get("/api/registrations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void getRegistrationById_Owner_Forbidden() throws Exception {
        mockMvc.perform(get("/api/registrations/1"))
                .andExpect(status().isForbidden());
    }

    // 5. GET registrations by tournamentId — ADMIN or OWNER
    @Test
    @WithMockUser(roles = "ADMIN")
    void getRegistrationsByTournamentId_Admin_Success() throws Exception {
        Mockito.when(registrationService.getRegistrationsByTournamentId(1L))
                .thenReturn(Arrays.asList(withId(1L)));

        mockMvc.perform(get("/api/registrations/tournament/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void getRegistrationsByTournamentId_Owner_Success() throws Exception {
        Mockito.when(registrationService.getRegistrationsByTournamentId(1L))
                .thenReturn(Arrays.asList(withId(2L)));

        mockMvc.perform(get("/api/registrations/tournament/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2));
    }

    // 6. GET registrations by teamId — ADMIN only
    @Test
    @WithMockUser(roles = "ADMIN")
    void getRegistrationsByTeamId_Admin_Success() throws Exception {
        Mockito.when(registrationService.getRegistrationsByTeamId(1L))
                .thenReturn(Arrays.asList(withId(1L)));

        mockMvc.perform(get("/api/registrations/team/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void getRegistrationsByTeamId_Owner_Forbidden() throws Exception {
        mockMvc.perform(get("/api/registrations/team/1"))
                .andExpect(status().isForbidden());
    }

    // 7. Count registrations — ADMIN or OWNER
    @Test
    @WithMockUser(roles = "ADMIN")
    void countRegistrations_Admin_Success() throws Exception {
        Mockito.when(registrationService.countRegistrationsByTournamentId(1L)).thenReturn(5L);

        mockMvc.perform(get("/api/registrations/count/tournament/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    @WithMockUser(roles = "OWNER")
    void countRegistrations_Owner_Success() throws Exception {
        Mockito.when(registrationService.countRegistrationsByTournamentId(1L)).thenReturn(3L);

        mockMvc.perform(get("/api/registrations/count/tournament/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }
}
