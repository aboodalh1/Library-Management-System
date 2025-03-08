package com.library.utils.mapper;

import com.library.book.model.Book;
import com.library.book.request.BookDTO;
import com.library.book.response.BooksResponse;
import com.library.borrowing_record.model.BorrowingRecord;
import com.library.borrowing_record.request.NewBorrowingDTO;
import com.library.borrowing_record.response.BorrowingRecordResponse;
import com.library.librarian.model.Librarian;
import com.library.librarian.request.LibrarianRegisterRequest;
import com.library.librarian.response.LibrarianAuthResponse;
import com.library.patron.model.Patron;
import com.library.patron.request.PatronDTO;
import com.library.patron.response.PatronInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClassMapper {


    ClassMapper INSTANCE = Mappers.getMapper( ClassMapper.class );


    BooksResponse entityToDto(Book book);
    PatronInfoResponse entityToDto(Patron patron);

    @Mapping(source = "firstName" , target = "firstName")
    LibrarianAuthResponse entityToDto(Librarian librarian);

    BorrowingRecordResponse entityToDto(BorrowingRecord borrowingRecord);

    Book bookDtoToEntity(BookDTO request);
    Patron patronDtoToEntity(PatronDTO request);
    @Mapping(source = "firstName" , target = "firstName")
    @Mapping(source = "password" , target = "password")
    Librarian librarianDtoToEntity(LibrarianRegisterRequest request);
    //    @Mapping(source = "borrowDate" , target = "borrowDate")
    BorrowingRecord borrowingRecordDtoToEntity(NewBorrowingDTO request);


}
