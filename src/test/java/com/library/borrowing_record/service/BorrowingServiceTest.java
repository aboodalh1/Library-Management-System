package com.library.borrowing_record.service;

import com.library.book.model.Book;
import com.library.book.respository.BookRepository;
import com.library.borrowing_record.Enum.BorrowingStatus;
import com.library.borrowing_record.model.BorrowingRecord;
import com.library.borrowing_record.repository.BorrowingRecordRepository;
import com.library.borrowing_record.request.EndBorrowinDTO;
import com.library.borrowing_record.request.NewBorrowingDTO;
import com.library.borrowing_record.response.BorrowingRecordResponse;
import com.library.borrowing_record.response.EndBorrowingRecordResponse;
import com.library.patron.model.Patron;
import com.library.patron.repository.PatronRepository;
import com.library.utils.exceptions.RequestNotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowingServiceTest {

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private BorrowingService borrowingService;

    private Book book;
    private Patron patron;
    private BorrowingRecord borrowingRecord;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setAvailable(true);

        patron = new Patron();
        patron.setId(1L);

        borrowingRecord = new BorrowingRecord();
        borrowingRecord.setBook(book);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setStatus(BorrowingStatus.Borrowed);
    }

    @Test
    void borrowBook_Successful() {
        NewBorrowingDTO request = new NewBorrowingDTO();
        request.setBorrowDate(LocalDate.of(2024, 3, 1));
        request.setDueDate(LocalDate.of(2024, 4, 1));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenReturn(borrowingRecord);

        BorrowingRecordResponse response = borrowingService.borrowBook(1L, 1L, request);

        assertNotNull(response);
        assertEquals(1L, response.getBookId());
        assertEquals(1L, response.getPatronId());
        assertEquals(BorrowingStatus.Borrowed, borrowingRecord.getStatus());
        assertFalse(book.isAvailable());
    }

    @Test
    void borrowBook_BookNotAvailable() {
        book.setAvailable(false);

        NewBorrowingDTO request = new NewBorrowingDTO();
        request.setBorrowDate(LocalDate.of(2024, 3, 1));
        request.setDueDate(LocalDate.of(2024, 4, 1));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        RequestNotValidException exception = assertThrows(RequestNotValidException.class, () ->
                borrowingService.borrowBook(1L, 1L, request));

        assertEquals("This book is not available", exception.getMessage());
    }

    @Test
    void borrowBook_InvalidDate() {
        NewBorrowingDTO request = new NewBorrowingDTO();
        request.setBorrowDate(LocalDate.of(2024, 4, 1));
        request.setDueDate(LocalDate.of(2024, 3, 1));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));

        RequestNotValidException exception = assertThrows(RequestNotValidException.class, () ->
                borrowingService.borrowBook(1L, 1L, request));

        assertEquals("The borrow date must be after due date !", exception.getMessage());
    }

    @Test
    void returnBook_Successful() {
        EndBorrowinDTO request = new EndBorrowinDTO();
        request.setReturnDate(LocalDate.of(2024, 3, 10));

        borrowingRecord.setBorrowDate(LocalDate.of(2024, 3, 1));
        borrowingRecord.setDueDate(LocalDate.of(2024, 3, 15));
        book.setAvailable(false);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findBorrowingRecordByBookAndPatronAndReturnDateIsNullAndStatusIs(book, patron, BorrowingStatus.Borrowed))
                .thenReturn(Optional.of(borrowingRecord));

        EndBorrowingRecordResponse response = borrowingService.returnBook(1L, 1L, request);

        assertNotNull(response);
        assertEquals(BorrowingStatus.Returned, borrowingRecord.getStatus());
        assertTrue(book.isAvailable());
    }

    @Test
    void returnBook_AlreadyAvailable() {
        book.setAvailable(true);

        EndBorrowinDTO request = new EndBorrowinDTO();
        request.setReturnDate(LocalDate.of(2024, 3, 10));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        RequestNotValidException exception = assertThrows(RequestNotValidException.class, () ->
                borrowingService.returnBook(1L, 1L, request));

        assertEquals("This book is already available", exception.getMessage());
    }

    @Test
    void returnBook_ReturnDateBeforeBorrowDate() {
        EndBorrowinDTO request = new EndBorrowinDTO();
        request.setReturnDate(LocalDate.of(2024, 2, 25));

        borrowingRecord.setBorrowDate(LocalDate.of(2024, 3, 1));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findBorrowingRecordByBookAndPatronAndReturnDateIsNullAndStatusIs(book, patron, BorrowingStatus.Borrowed))
                .thenReturn(Optional.of(borrowingRecord));

        RequestNotValidException exception = assertThrows(RequestNotValidException.class, () ->
                borrowingService.returnBook(1L, 1L, request));

        assertEquals("Return date must be after borrow date!", exception.getMessage());
    }

}
