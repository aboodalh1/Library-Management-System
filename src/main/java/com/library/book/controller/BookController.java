package com.library.book.controller;

import com.library.book.model.Book;
import com.library.book.request.BookDTO;
import com.library.book.service.BookService;
import com.library.utils.response.MyAPIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book", description = "Manage books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
    @Operation(
            summary = "Get All Books",
            description = "Retrieve all books.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved books",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MyAPIResponse.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "Get all books",
                          "status": "OK",
                          "body": {
                              "books": [
                                  {
                                      "id": "e8d4eeb8-1234-4a56-b57a-76abf6d55555",
                                      "title": "Spring in Action",
                                      "author": "Craig Walls",
                                      "isbn": "9781617294945",
                                      "Available":true
                                      "publicationYear": "2020-06-01",
                                  }
                              ],
                          }
                        }
                        """)
                            )
                    )
            }
    )
    @GetMapping
    public MyAPIResponse<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        System.out.println("Books retrieved: " + books);  // Check the list here
        boolean success = books != null && !books.isEmpty();
        int statusCode = success ? 200 : 404;
        return new MyAPIResponse<>(success, statusCode, books);
    }

    @Operation(summary = "Get a specific book",
        description = "Get a specific book by its Id"
    )
    @GetMapping("/{id}")
    public MyAPIResponse<?> getBookById(@PathVariable Long id) {
        try {
        return new MyAPIResponse<>(true,200,bookService.getBookById(id));
        }catch (RuntimeException e){
            return new MyAPIResponse<>(false,404,"There is no book with id: "+id);
        }
    }
    @Operation(summary = "Add new book")
    @PostMapping("/add_book")
    public MyAPIResponse<?> addBook(@Valid @RequestBody BookDTO bookDTO) {
        System.out.println("Received Book DTO: " + bookDTO);

        Book book = new Book();

        if(bookDTO.getPublicationYear() == null){
            return new MyAPIResponse<>(false,400,"Publication year is required");
        }
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setPublicationYear(bookDTO.getPublicationYear());
        book.setIsbn(bookDTO.getIsbn());
        book.setAvailable(bookDTO.isAvailable());

        Book savedBook = bookService.addBook(book);
        return new MyAPIResponse<>(true, 200, savedBook);
    }

    @Operation(summary = "Edit a book",description = "Edit a book by its Id")
    @PutMapping("/{id}")
    public MyAPIResponse updateBook(@PathVariable Long id, @RequestBody Book book) {
        try{
        return new MyAPIResponse<>(true,200,bookService.updateBook(id, book));
        }catch (RuntimeException e){
            return new MyAPIResponse<>(false,404,"There is no book with id: "+id);
        }
    }

    @Operation(summary = "Delete a book",description = "Dlete a book by its Id")
    @DeleteMapping("/{id}")
    public MyAPIResponse<?> deleteBook(@PathVariable Long id) {
        boolean isDeleted = bookService.deleteBook(id);
if(isDeleted){
        return new MyAPIResponse<>(true, 200, "Book deleted successfully");
    }
    else{
    return new MyAPIResponse<>(false,404,"There is no book with id: "+id);
    }
}}