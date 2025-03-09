package com.library.book.respository;



import com.library.book.model.Book;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Cacheable(value = "books", key = "#id")
    Optional<Book> findById(Long id);

    @Cacheable(value = "books", key = "'allBooks'")
    Page<Book> findAll(Pageable pageable);

    @CacheEvict(value = "books", key = "'allBooks'")
    <S extends Book> S save(S entity);

    @CachePut(value = "books", key = "#entity.id")
    <S extends Book> S saveAndFlush(S entity);

    @CacheEvict(value = "books", key = "#id")
    void deleteById(Long id);

}