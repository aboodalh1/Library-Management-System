package com.library.patron.controller;

import com.library.patron.model.Patron;
import com.library.patron.request.PatronDTO;
import com.library.patron.response.PatronResponse;
import com.library.patron.service.PatronService;
import com.library.utils.response.MyAPIResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PatronControllerTest {

    @Mock
    private PatronService patronService;

    @InjectMocks
    private PatronController patronController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(patronController).build();
    }

    @Test
    void testGetAllPatrons() throws Exception {
        PatronResponse patronResponse = new PatronResponse(1L, "John", "Doe", "1234567890", "123 Main St");
        Mockito.when(patronService.getAllPatrons()).thenReturn(List.of(patronResponse));

        mockMvc.perform(get("/api/patrons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].firstName").value("John"));
    }

    @Test
    void testGetPatronById() throws Exception {
        Patron patron = new Patron();
        patron.setId(1L);
        patron.setFirstName("John");
        patron.setLastName("Doe");

        Mockito.when(patronService.getPatronById(1L)).thenReturn(patron);

        mockMvc.perform(get("/api/patrons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName").value("John"));
    }

    @Test
    void testAddPatron() throws Exception {
        PatronDTO patronDTO = new PatronDTO();
        patronDTO.setFirstName("Jane");
        patronDTO.setLastName("Smith");
        patronDTO.setPhoneNumber("9876543210");
        patronDTO.setAddress("456 Elm St");

        PatronResponse patronResponse = new PatronResponse(2L, "Jane", "Smith", "9876543210", "456 Elm St");

        Mockito.when(patronService.addPatron(Mockito.any(PatronDTO.class))).thenReturn(patronResponse);

        mockMvc.perform(post("/api/patrons/add_patron")
                        .contentType("application/json")
                        .content("{\"firstName\":\"Jane\", \"lastName\":\"Smith\", \"phoneNumber\":\"9876543210\", \"address\":\"456 Elm St\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName").value("Jane"));
    }
}
