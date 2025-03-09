package com.library.patron.service;

import com.library.patron.model.Patron;
import com.library.patron.repository.PatronRepository;
import com.library.patron.request.PatronDTO;
import com.library.patron.response.PatronInfoResponse;
import com.library.utils.exceptions.NotFoundException;
import com.library.utils.exceptions.RequestNotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatronServiceTest {

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private PatronService patronService;

    private Patron mockPatron;

    @BeforeEach
    void setUp() {
        mockPatron = new Patron();
        mockPatron.setId(1L);
        mockPatron.setFirstName("AbdAllah");
        mockPatron.setLastName("Alh");
        mockPatron.setAddress("Damascus");
        mockPatron.setPhoneNumber("930704986");
    }

    @Test
    void testGetAllPatrons() {
        when(patronRepository.findAll()).thenReturn(List.of(mockPatron));

        List<Patron> patrons = patronService.getAllPatrons();

        assertFalse(patrons.isEmpty());
        assertEquals(1, patrons.size());
        verify(patronRepository, times(1)).findAll();
    }

    @Test
    void testGetPatronById_Found() {
        when(patronRepository.existsById(1L)).thenReturn(true);
        when(patronRepository.findById(1L)).thenReturn(Optional.of(mockPatron));

        Patron patron = patronService.getPatronById(1L);

        assertNotNull(patron);
        assertEquals("AbdAllah", patron.getFirstName());
    }

    @Test
    void testGetPatronById_NotFound() {
        when(patronRepository.existsById(2L)).thenReturn(false);

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            patronService.getPatronById(2L);
        });

        assertEquals("There is no patron with id = 2", thrown.getMessage());
    }

    @Test
    void testAddPatron() {
        when(patronRepository.save(any(Patron.class))).thenReturn(mockPatron);

        Patron savedPatron = patronService.addPatron(mockPatron);

        assertNotNull(savedPatron);
        assertEquals("AbdAllah", savedPatron.getFirstName());
        verify(patronRepository, times(1)).save(mockPatron);
    }

    @Test
    void testUpdatePatron_Success() {
        PatronDTO updateDTO = new PatronDTO();
        updateDTO.setFirstName("Updated");
        updateDTO.setLastName("Patron");
        updateDTO.setAddress("New Address");
        updateDTO.setPhoneNumber("930704986");

        when(patronRepository.existsById(1L)).thenReturn(true);
        when(patronRepository.findById(1L)).thenReturn(Optional.of(mockPatron));
        when(patronRepository.findByPhoneNumberAndIdIsNot("930704986", 1L)).thenReturn(Optional.empty());

        PatronInfoResponse response = patronService.updatePatron(1L, updateDTO);

        assertNotNull(response);
        assertEquals("Updated", response.getFirstName());
        verify(patronRepository, times(1)).saveAndFlush(any(Patron.class));
    }

    @Test
    void testUpdatePatron_NotFound() {
        PatronDTO updateDTO = new PatronDTO();
        when(patronRepository.existsById(2L)).thenReturn(false);

        RequestNotValidException thrown = assertThrows(RequestNotValidException.class, () -> {
            patronService.updatePatron(2L, updateDTO);
        });

        assertEquals("There is no patron with id = 2", thrown.getMessage());
    }

    @Test
    void testDeletePatron_Success() {
        when(patronRepository.existsById(1L)).thenReturn(true);

        boolean result = patronService.deletePatron(1L);

        assertTrue(result);
        verify(patronRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePatron_NotFound() {
        when(patronRepository.existsById(2L)).thenReturn(false);

        boolean result = patronService.deletePatron(2L);

        assertFalse(result);
        verify(patronRepository, never()).deleteById(anyLong());
    }
}
