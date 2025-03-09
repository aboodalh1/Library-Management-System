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

    Optional<Book> findById(Long id);

    Page<Book> findAll(Pageable pageable);

    <S extends Book> S save(S entity);

    <S extends Book> S saveAndFlush(S entity);


    void deleteById(Long id);

}