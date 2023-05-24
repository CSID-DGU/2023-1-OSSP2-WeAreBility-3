package com.dongguk.cse.naemansan.common;

import io.micrometer.common.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@AllArgsConstructor
public class ResponseDto<T> {
    private final Boolean success;
    private final T data;
    private ExceptionDto error;


    public ResponseDto(@Nullable T data) {
        this.success = true;
        this.data = data;
    }

    // 사용자 정의 Exception 에 따른 ExceptionDto 리턴
    public static ResponseEntity<Object> toResponseEntity(RestApiException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ResponseDto.builder()
                        .success(false)
                        .data(null)
                        .error(new ExceptionDto(e.getErrorCode())).build());
    }

    // 그 외 Exception 에 따른 ExceptionDto 리턴
    public static ResponseEntity<Object> toResponseEntity(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.builder()
                        .success(false)
                        .data(null)
                        .error(new ExceptionDto(e)).build());
    }
}
