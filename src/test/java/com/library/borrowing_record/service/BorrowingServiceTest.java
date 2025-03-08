//package com.library.borrowing_record.service;
//
//import com.library.book.model.Book;
//import com.library.book.respository.BookRepository;
//import com.library.borrowing_record.model.BorrowingRecord;
//import com.library.borrowing_record.repository.BorrowingRecordRepository;
//import com.library.patron.model.Patron;
//import com.library.patron.repository.PatronRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class BorrowingServiceTest {
//
//    @Mock
//    private BookRepository bookRepository;
//
//    @Mock
//    private PatronRepository patronRepository;
//
//    @Mock
//    private BorrowingRecordRepository borrowingRecordRepository;
//
//    @InjectMocks
//    private BorrowingService borrowingService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testBorrowBook_Success() {
//        // 1️⃣ إعداد البيانات الوهمية
//        Book book = new Book();
//        book.setId(1L);
//        book.setAvailable(true);
//
//        Patron patron = new Patron();
//        patron.setId(1L);
//
//        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
//        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
//        when(borrowingRecordRepository.save(any(BorrowingRecord.class))).thenAnswer(i -> i.getArgument(0));
//
//        // 2️⃣ استدعاء الخدمة
//        BorrowingRecord result = borrowingService.borrowBook(1L, 1L);
//
//        // 3️⃣ التحقق من النتائج
//        assertNotNull(result);
//        assertEquals(book, result.getBook());
//        assertEquals(patron, result.getPatron());
//        assertFalse(book.isAvailable()); // يجب أن يصبح الكتاب غير متاح بعد الحجز
//
//        verify(borrowingRecordRepository, times(1)).save(any(BorrowingRecord.class));
//    }
//
//    @Test
//    void testBorrowBook_BookNotFound() {
//        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
//            borrowingService.borrowBook(1L, 1L);
//        });
//
//        assertEquals("الكتاب غير موجود", exception.getMessage());
//    }
//
//    @Test
//    void testBorrowBook_BookNotAvailable() {
//        Book book = new Book();
//        book.setId(1L);
//        book.setAvailable(false);
//
//        Patron patron = new Patron();
//        patron.setId(1L);
//
//        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
//        when(patronRepository.findById(1L)).thenReturn(Optional.of(patron));
//
//        Exception exception = assertThrows(IllegalStateException.class, () -> {
//            borrowingService.borrowBook(1L, 1L);
//        });
//
//        assertEquals("الكتاب غير متاح للحجز", exception.getMessage());
//    }
//}