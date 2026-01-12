package com.example.finalproject.event.exception;

import com.example.finalproject.event.dto.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        String message = ex.getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        return ResponseEntity.badRequest().body(
                new BaseResponse<>(
                        false,
                        message,
                        "01",
                        null
                )
        );
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<Object>> handleIllegalArgument(
            IllegalArgumentException ex
    ) {
        return ResponseEntity.badRequest().body(
                new BaseResponse<>(
                        false,
                        ex.getMessage(),
                        "02",
                        null
                )
        );
    }


}

