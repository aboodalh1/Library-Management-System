package com.library.book.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class BookDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(min = 3,max = 80,message = "Author name must be at least 3 and at most 80 character")
    private String author;

    @NotNull(message = "Publication year is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate publicationYear;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    private boolean available = true;



    public @NotBlank(message = "Title is required") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title is required") String title) {
        this.title = title;
    }

    public @NotBlank(message = "Author is required") String getAuthor() {
        return author;
    }

    public void setAuthor(@NotBlank(message = "Author is required") String author) {
        this.author = author;
    }

    public LocalDate getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(@NotNull(message = "Publication year is required") LocalDate publicationYear) {
        this.publicationYear = publicationYear;
    }

    public @NotBlank(message = "ISBN is required") String getIsbn() {
        return isbn;
    }

    public void setIsbn(@NotBlank(message = "ISBN is required") String isbn) {
        this.isbn = isbn;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}