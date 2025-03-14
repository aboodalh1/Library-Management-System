package com.library.librarian.controller;

import com.library.librarian.request.AuthenticationRequest;
import com.library.librarian.request.ChangePasswordRequest;
import com.library.librarian.request.LibrarianRegisterRequest;
import com.library.librarian.response.LibrarianAuthResponse;
import com.library.librarian.service.LibrarianService;
import com.library.utils.exceptions.APIException;
import com.library.utils.response.MyAPIResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Librarian Register",
            description = "Register Librarian by first name, last name, email and password",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LibrarianRegisterRequest.class),
                            examples = @ExampleObject(
                                    value = "{ \"firstName\": \"abdAllah\", \"lastName\": \"Alharisi\", \"email\": \"abd@gmail.com\", \"password\": \"Abood@123\" }"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Registration successful",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LibrarianAuthResponse.class),
                                    examples = @ExampleObject("""
                        {
                          "id": "1",
                          "firstName": "AbdAllah",
                          "lastName": "Alharisi",
                          "email": "abd@gmail.com",
                          "token": "teFFetgwQQRrfswadfeSKIg..."
                        }
                        """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Email already exists",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = APIException.class),
                                    examples = @ExampleObject("""
                        {
                          "message": "Email already exists",
                          "status": "BAD_REQUEST",
                        }
                        """)
                            )
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<MyAPIResponse<?>> register(
            @Valid @RequestBody LibrarianRegisterRequest request
    ){
        return ResponseEntity.ok(new MyAPIResponse<>(true,200,librarianService.librarianRegister(request)));
    }
    @Operation(summary = "Librarian Login",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LibrarianRegisterRequest.class),
                            examples = @ExampleObject(
                                    value = "{ \"email\": \"abd@gmail.com\", \"password\": \"Abood@123\" }"
                            )
                    )))
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
        librarianService.changePassword(request , connectedUser);
        return ResponseEntity.ok(new MyAPIResponse<>(true,200,"Change password done successfully"));
    }
}
