package com.library.patron.controller;

import com.library.patron.model.Patron;
import com.library.patron.request.PatronDTO;
import com.library.patron.response.PatronInfoResponse;
import com.library.patron.service.PatronService;
import com.library.utils.response.MyAPIResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

class PatronControllerTest {

    @InjectMocks
    private PatronController patronController;

    @Mock
    private PatronService patronService;

    private Patron patron;
    private PatronDTO patronDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock data setup
        patron = new Patron();
        patron.setId(1L);
        patron.setFirstName("Abd");
        patron.setLastName("Alharisi");
        patron.setAddress("Damascus");
        patron.setPhoneNumber("930704986");

        patronDTO = new PatronDTO();
        patronDTO.setFirstName("Abd");
        patronDTO.setLastName("Alharisi");
        patronDTO.setAddress("Damascus");
        patronDTO.setPhoneNumber("930704986");
    }

    @Test
    void testGetAllPatrons() {
        // Setup
        when(patronService.getAllPatrons()).thenReturn(Collections.singletonList(patron));

        // Execute
        MyAPIResponse<List<Patron>> response = patronController.getAllPatrons();

        // Verify
        assertNotNull(response);
        assertTrue(response.getStatusCode()==200);
        assertEquals(200, response.getStatusCode());
        assertEquals(1, response.getData().size());
        assertEquals("Abd", response.getData().get(0).getFirstName());
    }

    @Test
    void testAddPatron() {
        // Setup
        when(patronService.addPatron(any(Patron.class))).thenReturn(patron);

        // Execute
        ResponseEntity<MyAPIResponse<Patron>> responseEntity = patronController.addPatron(patronDTO);

        // Verify
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.getBody().getStatusCode()==200);
        assertEquals("Abd", responseEntity.getBody().getData().getFirstName());
    }

    @Test
    void testUpdatePatron() {
        // Setup
        PatronInfoResponse patronInfoResponse = new PatronInfoResponse();
        patronInfoResponse.setFirstName("Abd");
        patronInfoResponse.setLastName("Alharisi");

        when(patronService.updatePatron(eq(1L), any(PatronDTO.class))).thenReturn(patronInfoResponse);

        // Execute
        MyAPIResponse<PatronInfoResponse> response = patronController.updatePatron(1L, patronDTO);

        // Verify
        assertNotNull(response);
        assertTrue(response.getStatusCode()==200);
        assertEquals(200, response.getStatusCode());
        assertEquals("Abd", response.getData().getFirstName());
    }

    @Test
    void testDeletePatronSuccess() {
        // Setup
        when(patronService.deletePatron(1L)).thenReturn(true);

        // Execute
        MyAPIResponse<String> response = patronController.deletePatron(1L);

        // Verify
        assertNotNull(response);
        assertTrue(response.getStatusCode()==200);
        assertEquals(200, response.getStatusCode());
        assertEquals("Patron deleted Successfully", response.getData());
    }

    @Test
    void testDeletePatronNotFound() {
        // Setup
        when(patronService.deletePatron(1L)).thenReturn(false);

        // Execute
        MyAPIResponse<String> response = patronController.deletePatron(1L);

        // Verify
        assertNotNull(response);
        assertFalse(response.getStatusCode()==200);
        assertEquals(404, response.getStatusCode());
        assertEquals("There is no patron with id: 1", response.getData());
    }
}
