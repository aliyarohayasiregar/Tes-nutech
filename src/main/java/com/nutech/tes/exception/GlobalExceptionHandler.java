package com.nutech.tes.exception;

import com.nutech.tes.ApiRespons;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.add(fieldError.getDefaultMessage());
        });
        ApiRespons<Object> apiRespons = new ApiRespons<>();
        apiRespons.setStatus(400);
        apiRespons.setMessage("Inputan tidak sesuai");
        apiRespons.setData(errors);
        return ResponseEntity.badRequest().body(apiRespons);
    }
}
