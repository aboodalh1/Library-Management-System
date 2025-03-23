package com.library.borrowing_record.controller;

import com.library.borrowing_record.request.EndBorrowinDTO;
import com.library.borrowing_record.request.NewBorrowingDTO;
import com.library.borrowing_record.service.BorrowingService;
import com.library.utils.response.MyAPIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrow")
@Tag(name = "Borrowing ", description = "Manage borrowing books")
public class BorrowingController {


    private final BorrowingService borrowingService;

    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }



    @GetMapping("/getByPatronId/{id}")
    public ResponseEntity<MyAPIResponse<?>>getBorrowingRecordByPatronId(@PathVariable Long id) {

        return ResponseEntity.ok(new MyAPIResponse<>(true,200,borrowingService.getBorrowingRecordByPatronId(id)));
    }

    @Operation(summary = "Borrow a book",
                description = "Borrow a book via patron id and book id"

    )
    @PostMapping("/{bookId}/patron/{patronId}")
    public ResponseEntity<MyAPIResponse<?>> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId, @Valid @RequestBody NewBorrowingDTO borrowingRecord) {

        return ResponseEntity.ok(new MyAPIResponse<>(true,200,borrowingService.borrowBook(bookId, patronId,borrowingRecord)));



    }








    @Operation(summary = "Return a book",
            description = "Rorrow a book via patron id and book id"

    )
    @PutMapping("/return/{bookId}/patron/{patronId}")
    public MyAPIResponse<?> returnBook(@PathVariable Long bookId, @PathVariable Long patronId, @Valid @RequestBody EndBorrowinDTO endBorrowinDTO) {

            return new MyAPIResponse<>(true,200,borrowingService.returnBook(bookId, patronId,endBorrowinDTO));

    }
}

