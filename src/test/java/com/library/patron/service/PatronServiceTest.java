package com.library.patron.service;
import com.library.patron.model.Patron;
import com.library.patron.repository.PatronRepository;
import com.library.patron.request.PatronDTO;
import com.library.patron.response.PatronResponse;
import com.library.utils.exceptions.NotFoundException;
import com.library.utils.exceptions.RequestNotValidException;
import com.library.utils.mapper.ClassMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatronServiceTest {

    @InjectMocks
    private PatronService patronService;

    @Mock
    private PatronRepository patronRepository;

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
    public void should_successfully_save_a_student(){
        //Given
        PatronDTO patronDTO = new PatronDTO(
                "AbdAllah",
                "Alh",
                "Damascus",
                "930704986"
        );

        Patron patron = new Patron(
                "AbdAllah",
                "Alh",
                "Damascus",
                "930704986"
        );
        //when
        PatronResponse patronResponse = patronService.addPatron(patronDTO);
        //then
        assertEquals(patronDTO.getFirstName(), patronResponse.getFirstName());
        assertEquals(patronDTO.getLastName(), patronResponse.getLastName());
        assertEquals(patronDTO.getAddress(), patronResponse.getAddress());
        assertEquals(patronDTO.getPhoneNumber(), patronResponse.getPhoneNumber());



    }

    @Test
    void testGetAllPatrons() {

        List <Patron> patrons = new ArrayList<>();
        patrons.add(
                  new Patron(
                        "AbdAllah",
                        "Alh",
                        "Damascus",
                        "930704986"
                )
        );patrons.add(
                  new Patron(
                        "AbdAllah 2",
                        "Alh",
                        "Damascus",
                        "930704986"
                )
        );
        patrons.get(0).setId(1L);
        when(patronRepository.findAll()).thenReturn(patrons);

        List<PatronResponse> patronResponses = patronService.getAllPatrons();
        assertNotNull(patronResponses);
        assertEquals(2, patronResponses.size());
        assertEquals("AbdAllah", patronResponses.get(0).getFirstName());

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
        PatronDTO patron = new PatronDTO();
        patron.setFirstName(mockPatron.getFirstName());
        patron.setLastName(mockPatron.getLastName());
        patron.setPhoneNumber(mockPatron.getPhoneNumber());
        patron.setAddress(mockPatron.getAddress());
        PatronResponse savedPatron = patronService.addPatron(patron);
        assertNotNull(savedPatron);
        assertEquals("AbdAllah", savedPatron.getFirstName());
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

        PatronResponse response = patronService.updatePatron(1L, updateDTO);

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
    void testDeletePatron_NotFound() {
        when(patronRepository.existsById(2L)).thenReturn(false);

        boolean result = patronService.deletePatron(2L);

        assertFalse(result);
        verify(patronRepository, never()).deleteById(anyLong());
    }
}
