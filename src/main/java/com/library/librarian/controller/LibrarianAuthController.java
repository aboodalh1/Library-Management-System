package com.library.librarian.controller;

import com.library.librarian.request.AuthenticationRequest;
import com.library.librarian.request.ChangePasswordRequest;
import com.library.librarian.request.LibrarianRegisterRequest;
import com.library.librarian.response.LibrarianAuthResponse;
import com.library.librarian.service.LibrarianService;
import com.library.utils.response.MyAPIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/librarianAuth")
@Tag(name = "Librarian Authentication Controller", description = "Endpoints for librarian registration, login, and password management")
public class LibrarianAuthController {

    private final LibrarianService librarianService;

    public LibrarianAuthController(LibrarianService librarianService) {
        this.librarianService = librarianService;
    }

    @Operation(summary = "Librarian Register")
    @PostMapping("/register")
    public ResponseEntity<MyAPIResponse<?>> register(
            @Valid @RequestBody LibrarianRegisterRequest request
    ){
        return ResponseEntity.ok(new MyAPIResponse<>(true,200,librarianService.librarianRegister(request)));
    }
    @Operation(summary = "Librarian Login")
    @PostMapping("/login")
    public ResponseEntity<LibrarianAuthResponse> authenticate(
            @RequestBody AuthenticationRequest request , HttpServletRequest httpServletRequest
    ){
        return ResponseEntity.ok(librarianService.librarianLogin(request, httpServletRequest));
    }

    @Operation(summary = "Librarian change password")
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    )
    {
        System.out.println(request.getNewPassword());
        System.out.println(request.getConfirmPassword());
        System.out.println(request.getCurrentPassword());
        librarianService.changePassword(request , connectedUser);
        return ResponseEntity.accepted().body("Change password done successfully");
    }
}
