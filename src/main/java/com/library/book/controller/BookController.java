package com.library.book.controller;

import com.library.book.model.Book;
import com.library.book.request.BookDTO;
import com.library.book.response.BooksResponse;
import com.library.book.service.BookService;
import com.library.utils.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ApiResponse<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        System.out.println("Books retrieved: " + books);  // Check the list here
        boolean success = books != null && !books.isEmpty();
        int statusCode = success ? 200 : 404;
        return new ApiResponse<>(success, statusCode, books);
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getBookById(@PathVariable Long id) {
        try {
        return new ApiResponse<>(true,200,bookService.getBookById(id));
        }catch (RuntimeException e){
            return new ApiResponse<>(false,404,"There is no book with id: "+id);
        }
    }

    @PostMapping("/add_book")
    public ApiResponse<?> addBook(@Valid @RequestBody BookDTO bookDTO) {
        System.out.println("Received Book DTO: " + bookDTO);

        Book book = new Book();

        if(bookDTO.getPublicationYear() == null){
            return new ApiResponse<>(false,400,"Publication year is required");
        }
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPublicationYear(bookDTO.getPublicationYear());
        book.setIsbn(bookDTO.getIsbn());
        book.setAvailable(bookDTO.isAvailable());

        Book savedBook = bookService.addBook(book);
        return new ApiResponse<>(true, 200, savedBook);
    }

    @PutMapping("/{id}")
    public ApiResponse updateBook(@PathVariable Long id, @RequestBody Book book) {
        try{
        return new ApiResponse<>(true,200,bookService.updateBook(id, book));
        }catch (RuntimeException e){
            return new ApiResponse<>(false,404,"There is no book with id: "+id);
        }
    }


    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteBook(@PathVariable Long id) {
        boolean isDeleted = bookService.deleteBook(id);
if(isDeleted){
        return new ApiResponse<>(true, 200, "Book deleted successfully");
    }
    else{
    return new ApiResponse<>(false,404,"There is no book with id: "+id);
    }
}}