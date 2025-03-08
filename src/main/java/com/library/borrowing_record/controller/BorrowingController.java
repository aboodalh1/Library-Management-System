package com.library.borrowing_record.controller;

import com.library.borrowing_record.request.EndBorrowinDTO;
import com.library.borrowing_record.request.NewBorrowingDTO;
import com.library.borrowing_record.service.BorrowingService;
import com.library.utils.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrow")
public class BorrowingController {


    private final BorrowingService borrowingService;
    @Autowired
    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PostMapping("/{bookId}/patron/{patronId}")
    public ResponseEntity<ApiResponse<?>> borrowBook(@PathVariable Long bookId, @PathVariable Long patronId, @Valid @RequestBody NewBorrowingDTO borrowingRecord) {
        try {
        return ResponseEntity.ok(new ApiResponse<>(true,200,borrowingService.borrowBook(bookId, patronId,borrowingRecord)));
        }catch (RuntimeException e){
            return ResponseEntity.ok(new ApiResponse<>(false,404,e.toString()));
        }
    }

    @PutMapping("/return/{bookId}/patron/{patronId}")
    public ApiResponse<?> returnBook(@PathVariable Long bookId, @PathVariable Long patronId,@Valid @RequestBody EndBorrowinDTO endBorrowinDTO) {
        try{
            return new ApiResponse<>(true,200,borrowingService.returnBook(bookId, patronId,endBorrowinDTO));
        }catch (RuntimeException e){
            return new ApiResponse<>(false,400,e.toString());
        }
    }
}

