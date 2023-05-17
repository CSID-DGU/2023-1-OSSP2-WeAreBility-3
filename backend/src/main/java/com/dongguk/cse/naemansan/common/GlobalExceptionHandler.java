package com.dongguk.cse.naemansan.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {RestApiException.class})
    public ResponseEntity<Object> handleApiException(RestApiException e) {
        log.error("HandleApiException throw RestApiException : {}", e.getErrorCode());
        return ResponseDto.toResponseEntity(e);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception e) {
        log.error("HandleException throw Exception : {}", e.getMessage());
        return ResponseDto.toResponseEntity(e);
    }
}
