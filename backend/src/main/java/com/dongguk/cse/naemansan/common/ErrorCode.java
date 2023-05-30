package com.dongguk.cse.naemansan.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    NOT_FOUND_USER("404", HttpStatus.NOT_FOUND, "Not Exist User"),
    NOT_FOUND_ENROLLMENT_COURSE("404", HttpStatus.NOT_FOUND, "Not Exist Enrollment Course"),
    NOT_FOUND_INDIVIDUAL_COURSE("404", HttpStatus.NOT_FOUND, "Not Exist Individual Course"),
    NOT_FOUND_USING_COURSE("404", HttpStatus.NOT_FOUND, "Not Exist Using Course"),
    NOT_FOUND_COURSE_TAG("404", HttpStatus.NOT_FOUND, "Not Exist CourseTag"),
    NOT_FOUND_COMMENT("404", HttpStatus.NOT_FOUND, "Not Exist Comment"),
    NOT_FOUND_NOTIFICATION("404", HttpStatus.NOT_FOUND, "Not Exist Notification"),
    NOT_FOUND_SHOP("404", HttpStatus.NOT_FOUND, "Not Exist Shop"),
    NOT_FOUND_ADVERTISEMENT("404", HttpStatus.NOT_FOUND, "Not Exist Advertisement"),

    DUPLICATION_LOCATIONS("400", HttpStatus.BAD_REQUEST, "Duplication Enrollment Course Locations"),
    DUPLICATION_TITLE("400", HttpStatus.BAD_REQUEST, "Duplication Course Title"),
    DUPLICATION_NAME("400", HttpStatus.BAD_REQUEST, "Duplication Shop/Advertisement Name"),
    EXIST_ENTITY_REQUEST("400", HttpStatus.BAD_REQUEST, "Exist Entity Request"),
    NOT_EXIST_ENTITY_REQUEST("400", HttpStatus.BAD_REQUEST, "Not Exist Entity Request"),

    FILE_UPLOAD("500", HttpStatus.INTERNAL_SERVER_ERROR, "File Upload Fail"),
    FILE_DOWNLOAD("500", HttpStatus.INTERNAL_SERVER_ERROR, "File Upload Fail"),

    NOT_EQUAL("400", HttpStatus.BAD_REQUEST , "Not Equal Error"),

    DEFAULT_ERROR_CODE("500", HttpStatus.INTERNAL_SERVER_ERROR, "기본 에러 메시지입니다."),

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