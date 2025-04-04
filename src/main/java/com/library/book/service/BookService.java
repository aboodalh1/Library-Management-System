package com.library.book.service;

import com.library.book.model.Book;
import com.library.book.request.BookDTO;
import com.library.book.response.BooksResponse;
import com.library.book.respository.BookRepository;
import com.library.utils.exceptions.NotFoundException;
import com.library.utils.mapper.ClassMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;


    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<Book> getAllBooks(int page, int size) {
        bookRepository.findAll();
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findAll(pageable);
    }

    public List<Book> searchBookByTitle(String title){
        System.out.println(title);
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }


    public Book getBookById(Long id) {
        if (!bookRepository.existsById(id)) throw new NotFoundException("Book not found");
        return bookRepository.findById(id).orElse(null);
    }

    public BooksResponse addBook(BookDTO requestBook) {

        Book book = ClassMapper.INSTANCE.bookDtoToEntity(requestBook);
        bookRepository.save(book);
        return ClassMapper.INSTANCE.entityToDto(book);
    }

    @Transactional
    public BooksResponse updateBook(Long id, BookDTO updatedBook) {
        if (bookRepository.existsById(id)) {
            Book book = bookRepository.findById(id).orElse(null);
            book.setAuthor(updatedBook.getAuthor());
            book.setAvailable(updatedBook.isAvailable());
            book.setIsbn(updatedBook.getIsbn());
            book.setPublicationYear(updatedBook.getPublicationYear());
            book.setTitle(updatedBook.getTitle());
            bookRepository.saveAndFlush(book);
            return ClassMapper.INSTANCE.entityToDto(book);
        } else {
            throw new NotFoundException("Book not found");
        }
    }

    public boolean deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return true;
        } else {
            throw new NotFoundException("Not found book with id = " + id);
        }
    }
}