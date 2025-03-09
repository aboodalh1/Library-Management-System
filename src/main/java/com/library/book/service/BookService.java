package com.library.book.service;


import com.library.book.model.Book;
import com.library.book.response.BooksResponse;
import com.library.book.respository.BookRepository;
import com.library.utils.exceptions.NotFoundException;
import com.library.utils.exceptions.RequestNotValidException;
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

    }





    public Book getBookById(Long id) {
        if(!bookRepository.existsById(id)) throw new NotFoundException("Book not found");
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
            throw new NotFoundException("Book not found");
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