package com.kost.kostapi.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        private ResponseEntity<ErrorResponse> build(
                        HttpStatus status,
                        String message,
                        HttpServletRequest request) {

                return ResponseEntity.status(status).body(
                                new ErrorResponse(
                                                LocalDateTime.now(),
                                                status.value(),
                                                status.getReasonPhrase(),
                                                message,
                                                request.getRequestURI()));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> validation(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request

        ) {
                FieldError field = ex.getBindingResult()
                                .getFieldErrors()
                                .getFirst();

                return build(
                                HttpStatus.BAD_REQUEST,
                                field == null
                                                ? "Validation failed"
                                                : field.getDefaultMessage(),
                                request);
        }

        @ExceptionHandler(ResponseStatusException.class)
        public ResponseEntity<ErrorResponse> responseStatus(
                        ResponseStatusException ex,
                        HttpServletRequest request) {

                return build(
                                HttpStatus.valueOf(ex.getStatusCode().value()),
                                ex.getReason(),
                                request);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ErrorResponse> constraint(
                        ConstraintViolationException ex,
                        HttpServletRequest request) {
                return build(
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage(),
                                request);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ErrorResponse> duplicate(
                        DataIntegrityViolationException ex,
                        HttpServletRequest request) {
                return build(
                                HttpStatus.CONFLICT,
                                "Resource already exists",
                                request);
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponse> badCredentials(
                        BadCredentialsException ex,
                        HttpServletRequest request) {
                return build(
                                HttpStatus.UNAUTHORIZED,
                                "Invalid email or password",
                                request);
        }

        @ExceptionHandler(UsernameNotFoundException.class)
        public ResponseEntity<ErrorResponse> userNotFound(
                        UsernameNotFoundException ex,
                        HttpServletRequest request) {
                return build(
                                HttpStatus.UNAUTHORIZED,
                                ex.getMessage(),
                                request);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorResponse> accessDenied(
                        AccessDeniedException ex,
                        HttpServletRequest request) {

                return build(
                                HttpStatus.FORBIDDEN,
                                ex.getMessage(),
                                request);
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ErrorResponse> entityNotFound(
                        EntityNotFoundException ex,
                        HttpServletRequest request) {

                return build(
                                HttpStatus.NOT_FOUND,
                                ex.getMessage(),
                                request);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> illegalArgument(
                        IllegalArgumentException ex,
                        HttpServletRequest request) {
                return build(

                                HttpStatus.BAD_REQUEST,
                                ex.getMessage(),
                                request);
        }

        @ExceptionHandler(IllegalStateException.class)
        public ResponseEntity<ErrorResponse> illegalState(
                        IllegalStateException ex,
                        HttpServletRequest request) {

                return build(
                                HttpStatus.BAD_REQUEST,
                                ex.getMessage(),
                                request);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> exception(
                        Exception ex,
                        HttpServletRequest request) {
                log.error("Unexpected error", ex);
                return build(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "Internal server error",
                                request);
        }
}