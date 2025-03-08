package com.library.borrowing_record.repository;


import com.library.book.model.Book;
import com.library.borrowing_record.Enum.BorrowingStatus;
import com.library.borrowing_record.model.BorrowingRecord;
import com.library.patron.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {

    Optional<BorrowingRecord> findBorrowingRecordByBookAndPatronAndReturnDateIsNullAndStatusIs(Book book, Patron patron, BorrowingStatus status);


}