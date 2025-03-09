package com.library.borrowing_record.service;

import com.library.book.model.Book;
import com.library.book.respository.BookRepository;
import com.library.borrowing_record.Enum.BorrowingStatus;
import com.library.borrowing_record.model.BorrowingRecord;
import com.library.borrowing_record.repository.BorrowingRecordRepository;
import com.library.borrowing_record.request.EndBorrowinDTO;
import com.library.borrowing_record.request.NewBorrowingDTO;
import com.library.borrowing_record.response.BorrowingRecordResponse;
import com.library.patron.model.Patron;
import com.library.patron.repository.PatronRepository;
import com.library.utils.exceptions.RequestNotValidException;
import com.library.utils.mapper.ClassMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BorrowingService {

    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;

    @Autowired
    public BorrowingService(BorrowingRecordRepository borrowingRecordRepository, BookRepository bookRepository, PatronRepository patronRepository) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.bookRepository = bookRepository;
        this.patronRepository = patronRepository;
    }
    @Transactional
    public BorrowingRecordResponse borrowBook(Long bookId, Long patronId, NewBorrowingDTO request) {
        Book demandedBook = bookRepository.findById(bookId).orElseThrow(
                ()-> new RequestNotValidException("There is no book with this id: " + bookId)
        );
        Patron patron = patronRepository.findById(patronId).orElseThrow(
                ()-> new RequestNotValidException("There is no patron with id: " + patronId)
        );
        if(!demandedBook.isAvailable()){
            throw new RequestNotValidException("This book is not available");
        }
        if(request.getDueDate().isBefore(request.getBorrowDate())){
            throw new RequestNotValidException("The borrow date must be after due date !");
        }
        BorrowingRecord borrowingRecord = ClassMapper.INSTANCE.borrowingRecordDtoToEntity(request);
        borrowingRecord.setStatus(BorrowingStatus.Borrowed);
        borrowingRecord.setPatron(patron);
        borrowingRecord.setBook(demandedBook);
        borrowingRecord.getBook().setAvailable(false);
        borrowingRecordRepository.save(borrowingRecord);
        BorrowingRecordResponse response = ClassMapper.INSTANCE.entityToDto(borrowingRecord);
        response.setBookId(borrowingRecord.getBook().getId());
        response.setPatronId(borrowingRecord.getPatron().getId());
        return response;
    }
    @Transactional
    public BorrowingRecordResponse returnBook(Long bookId, Long patronId, EndBorrowinDTO endBorrowinDTO) {
        Book demandedBook = bookRepository.findById(bookId).orElseThrow(
                ()-> new RequestNotValidException("There is no book with this id: " + bookId)
        );
        Patron patron = patronRepository.findById(patronId).orElseThrow(
                ()-> new RequestNotValidException("There is no patron with id: " + patronId)
        );
        BorrowingRecord borrowingRecord = borrowingRecordRepository.findBorrowingRecordByBookAndPatronAndReturnDateIsNullAndStatusIs(demandedBook,patron,BorrowingStatus.Borrowed).orElseThrow(
                ()-> new RequestNotValidException("There is no active borrowing record for book id: " + demandedBook.getId() +" from patron: " + patronId)
        );
        if(demandedBook.isAvailable()){
            throw new RequestNotValidException("This book is already available");
        }
        if(endBorrowinDTO.getReturnDate().isBefore(borrowingRecord.getBorrowDate())){
            throw new RequestNotValidException("Return date must be after borrow date!");
        }
        if (borrowingRecord.getDueDate().isBefore(endBorrowinDTO.getReturnDate())) {
            borrowingRecord.setStatus(BorrowingStatus.Overdue);
        }
        else {
            borrowingRecord.setStatus(BorrowingStatus.Returned);
        }
        borrowingRecord.setReturnDate(endBorrowinDTO.getReturnDate());
        demandedBook.setAvailable(true);
        borrowingRecordRepository.save(borrowingRecord);
        BorrowingRecordResponse response = ClassMapper.INSTANCE.entityToDto(borrowingRecord);
        response.setBookId(borrowingRecord.getBook().getId());
        response.setPatronId(borrowingRecord.getPatron().getId());
        return response;
    }



}
