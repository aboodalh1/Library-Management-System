package com.library.librarian.repository;

import com.library.librarian.model.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LibrarianRepository extends JpaRepository<Librarian,Integer> {
    Optional<Librarian> findByEmail(String email);
}
