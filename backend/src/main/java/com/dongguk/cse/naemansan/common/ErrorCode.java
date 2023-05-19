package com.dongguk.cse.naemansan.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    NOT_FOUND_USER("404", HttpStatus.NOT_FOUND, "Not Exist User"),
    NOT_FOUND_COURSE("404", HttpStatus.NOT_FOUND, "Not Exist Course"),
    NOT_FOUND_COURSE_TAG("404", HttpStatus.NOT_FOUND, "Not Exist CourseTag"),


    DUPLICATION_COURSE_TITLE("4003", HttpStatus.BAD_REQUEST, "Duplication Course Title"),
    EXIST_LIKE("404", HttpStatus.BAD_REQUEST, "Exist Like"),
    NOT_EXIST_LIKE("404", HttpStatus.BAD_REQUEST, "Not Exist Like"),

    NOT_EQUAL("404", HttpStatus.BAD_REQUEST , "Not Equal Error"),
    DEFAULT_ERROR_CODE("5000", HttpStatus.INTERNAL_SERVER_ERROR, "기본 에러 메시지입니다."),

    // Access Denied Error
    ACCESS_DENIED_ERROR("401", HttpStatus.UNAUTHORIZED, "Access Denied Token Error"),

    // Token Error Set
    TOKEN_INVALID_ERROR("4010", HttpStatus.UNAUTHORIZED, "Invalid Token Error"),
    TOKEN_MALFORMED_ERROR("4011", HttpStatus.UNAUTHORIZED, "Malformed Token Error"),
    TOKEN_EXPIRED_ERROR("4013", HttpStatus.UNAUTHORIZED, "Expired Token Error"),
    TOKEN_TYPE_ERROR("4012", HttpStatus.UNAUTHORIZED, "Type Token Error"),
    TOKEN_UNSUPPORTED_ERROR("4014", HttpStatus.UNAUTHORIZED, "Unsupported Token Error"),
    TOKEN_UNKNOWN_ERROR("4015", HttpStatus.UNAUTHORIZED, "Unknown Error");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}