//package com.library.book.controller;
//
//import com.library.book.model.Book;
//import com.library.book.request.BookDTO;
//import com.library.book.service.BookService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@ExtendWith(MockitoExtension.class)
//class BookControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private BookService bookService;
//
//    @InjectMocks
//    private BookController bookController;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
//    }
//
//    @Test
//    void getAllBooks_ShouldReturnBooks() throws Exception {
//        Book book = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", "9780743273565", true, 2020);
//        when(bookService.getAllBooks()).thenReturn(List.of(book));
//
//        mockMvc.perform(get("/api/books"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.body[0].title").value("The Great Gatsby"));
//    }
//
//    @Test
//    void getAllBooks_ShouldReturnNotFound() throws Exception {
//        when(bookService.getAllBooks()).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/api/books"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void getBookById_ShouldReturnBook() throws Exception {
//        Book book = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", "9780743273565", true, 2020);
//        when(bookService.getBookById(1L)).thenReturn(book);
//
//        mockMvc.perform(get("/api/books/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.body.title").value("The Great Gatsby"));
//    }
//
//    @Test
//    void addBook_ShouldReturnCreatedBook() throws Exception {
//        BookDTO bookDTO = new BookDTO("The Great Gatsby", "F. Scott Fitzgerald", 2025, "8124232723125", true);
//        Book book = new Book(1L, bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getIsbn(), bookDTO.isAvailable(), bookDTO.getPublicationYear());
//        when(bookService.addBook(any(Book.class))).thenReturn(book);
//
//        mockMvc.perform(post("/api/books/add_book")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(bookDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.body.title").value("The Great Gatsby"));
//
//    }
//
//    @Test
//    void updateBook_ShouldReturnUpdatedBook() throws Exception {
//        BookDTO bookDTO = new BookDTO("Updated Title", "Updated Author", 2025, "8124232723125", true);
//        Book book = new Book(1L, bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getIsbn(), bookDTO.isAvailable(), bookDTO.getPublicationYear());
//        Book bookToUpdate = new Book(1L, "The Great Gatsby", "F. Scott Fitzgerald", "9780743273565", true, 2020);
//
//        when(bookService.getBookById(eq(1L))).thenReturn(bookToUpdate);
//        mockMvc.perform(put("/api/books/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(bookDTO)))
//                .andExpect(status().isOk());
//
////        verify(bookService).updateBook(eq(1L), any(Book.class));
//    }
//
//    @Test
//    void deleteBook_ShouldReturnSuccess() throws Exception {
//        when(bookService.deleteBook(1L)).thenReturn(true);
//
//        mockMvc.perform(delete("/api/books/1"))
//                .andExpect(status().isOk());
//        verify(bookService).deleteBook(1L);
//    }
//}
