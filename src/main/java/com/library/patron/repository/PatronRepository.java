package com.library.patron.repository;

import com.library.patron.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface PatronRepository extends JpaRepository<Patron, Long> {
    Optional<Patron> findByPhoneNumberAndIdIsNot(String phone, Long uuid);

}