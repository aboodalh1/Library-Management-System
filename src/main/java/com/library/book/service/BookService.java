package com.library.book.service;


import com.library.book.model.Book;
import com.library.book.response.BooksResponse;
import com.library.book.respository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;


    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
//                .stream()
//                .map(book -> new BooksResponse(
//                        book.getId(),
//                        book.getTitle(),
//                        book.getAuthor(),
//                        book.getIsbn(),
//                        book.isAvailable(),  // Assuming `isAvailable()` method exists in `Book`
//                        book.getPublicationYear()
//                ))
//                .collect(Collectors.toList());
    }





    public Book getBookById(Long id) {
        if(!bookRepository.existsById(id)) throw new RuntimeException("Book not found");
        return bookRepository.findById(id).orElse(null);
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBook(Long id, Book updatedBook) {
        if (bookRepository.existsById(id)) {
            updatedBook.setId(id);
            return bookRepository.save(updatedBook);
        }
        else{
            throw new RuntimeException("Book not found");
        }
    }

    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}