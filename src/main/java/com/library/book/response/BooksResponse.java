package com.library.book.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@Schema
public class BooksResponse {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private boolean available;
    private int publicationYear;

    // Constructor
    public BooksResponse(Long id, String title, String author, String isbn, boolean available, int publicationYear) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.available = available;
        this.publicationYear = publicationYear;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getPublicationYear() {
        return publicationYear;
    }
}
