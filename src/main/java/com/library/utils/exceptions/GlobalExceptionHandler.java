package com.library.utils.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentials(InvalidCredentialsException ex) {
        HttpStatus invalidCredentialsStatus = HttpStatus.UNAUTHORIZED;
        APIException errorResponse = new APIException(ex.getMessage(), invalidCredentialsStatus);
        return new ResponseEntity<>(errorResponse, invalidCredentialsStatus);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex) {
        HttpStatus expiredJwtStatus = HttpStatus.UNAUTHORIZED;
        APIException errorResponse = new APIException("JWT token has expired.", expiredJwtStatus);
        return new ResponseEntity<>(errorResponse, expiredJwtStatus);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<Object> handleUnsupportedJwtException(UnsupportedJwtException ex) {
        HttpStatus unsupportedJwtStatus = HttpStatus.UNAUTHORIZED;
        APIException errorResponse = new APIException("Unsupported token.", unsupportedJwtStatus);
        return new ResponseEntity<>(errorResponse, unsupportedJwtStatus);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<String> handleMalformedJwtException(MalformedJwtException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JWT token is invalid.");
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<String> handleSignatureException(SignatureException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT signature is not valid.");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        APIException errorResponse = new APIException(ex.getMessage(), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsernameNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Unauthorized", "message", ex.getMessage()));
    }

    @ExceptionHandler(value = {UnAuthorizedException.class})
    public ResponseEntity<Object> handleUnauthorizedException(UnAuthorizedException e) {
        HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;
        APIException apiException = new APIException(
                e.getMessage(),
                unauthorized
        );
        return new ResponseEntity<>(apiException, unauthorized);
    }

    @ExceptionHandler(value = {TooManyRequestException.class})
    public ResponseEntity<Object> handleTooManyRequestException(TooManyRequestException e) {
        HttpStatus tooManyRequests = HttpStatus.TOO_MANY_REQUESTS;
        APIException apiException = new APIException(
                e.getMessage(),
                tooManyRequests
        );
        return new ResponseEntity<>(apiException, tooManyRequests);
    }


    @ExceptionHandler(RequestNotValidException.class)
    public ResponseEntity<Object> handleRequestNotValidException(RequestNotValidException ex) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        APIException apiException = new APIException(
                ex.getMessage(),
                badRequest
        );
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }
}
