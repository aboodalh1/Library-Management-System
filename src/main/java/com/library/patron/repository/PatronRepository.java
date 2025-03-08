package com.library.patron.repository;

import com.library.patron.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PatronRepository extends JpaRepository<Patron, Long> {

}