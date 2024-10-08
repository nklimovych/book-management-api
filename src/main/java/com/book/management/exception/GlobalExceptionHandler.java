package com.book.management.exception;

import com.book.management.dto.ErrorResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                                .map(this::getErrorMessage)
                                .toList();
        return getResponseEntity(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(DuplicateIsbnException.class)
    protected ResponseEntity<Object> handleMethodRepository(DuplicateIsbnException ex) {
        return getResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleMethodNotFound(EntityNotFoundException ex) {
        return getResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    private ResponseEntity<Object> getResponseEntity(HttpStatus status, Object message) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                status.value(),
                message
        );
        return new ResponseEntity<>(errorResponse, status);
    }

    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            String message = e.getDefaultMessage();
            return message != null ? message : "Validation error";
        }
        return e.getDefaultMessage() != null ? e.getDefaultMessage() : "Validation error";
    }
}
