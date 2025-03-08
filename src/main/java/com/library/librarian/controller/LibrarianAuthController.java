package com.library.librarian.controller;

import com.library.librarian.request.AuthenticationRequest;
import com.library.librarian.request.ChangePasswordRequest;
import com.library.librarian.request.LibrarianRegisterRequest;
import com.library.librarian.response.LibrarianAuthResponse;
import com.library.librarian.service.LibrarianService;
import com.library.utils.response.ApiResponse;
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

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(
            @Valid @RequestBody LibrarianRegisterRequest request
    ){
        return ResponseEntity.ok(new ApiResponse<>(true,200,librarianService.librarianRegister(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<LibrarianAuthResponse> authenticate(
            @RequestBody AuthenticationRequest request , HttpServletRequest httpServletRequest
    ){
        return ResponseEntity.ok(librarianService.librarianLogin(request, httpServletRequest));
    }

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
