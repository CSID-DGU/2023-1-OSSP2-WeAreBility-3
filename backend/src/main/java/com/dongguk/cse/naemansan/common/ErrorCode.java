package com.dongguk.cse.naemansan.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Not Found Error
    NOT_FOUND_USER("4040", HttpStatus.NOT_FOUND, "Not Exist User"),
    NOT_FOUND_ENROLLMENT_COURSE("4041", HttpStatus.NOT_FOUND, "Not Exist Enrollment Course"),
    NOT_FOUND_INDIVIDUAL_COURSE("4042", HttpStatus.NOT_FOUND, "Not Exist Individual Course"),
    NOT_FOUND_USING_COURSE("4043", HttpStatus.NOT_FOUND, "Not Exist Using Course"),
    NOT_FOUND_COURSE_TAG("4044", HttpStatus.NOT_FOUND, "Not Exist CourseTag"),
    NOT_FOUND_COMMENT("4045", HttpStatus.NOT_FOUND, "Not Exist Comment"),
    NOT_FOUND_NOTIFICATION("4046", HttpStatus.NOT_FOUND, "Not Exist Notification"),
    NOT_FOUND_SHOP("4047", HttpStatus.NOT_FOUND, "Not Exist Shop"),
    NOT_FOUND_ADVERTISEMENT("4048", HttpStatus.NOT_FOUND, "Not Exist Advertisement"),

    // Bad Request Error
    NOT_EQUAL("4000", HttpStatus.BAD_REQUEST , "Not Equal Error"),
    DUPLICATION_LOCATIONS("4001", HttpStatus.BAD_REQUEST, "Duplication Enrollment Course Locations"),
    DUPLICATION_TITLE("4002", HttpStatus.BAD_REQUEST, "Duplication Course Title"),
    DUPLICATION_NAME("4003", HttpStatus.BAD_REQUEST, "Duplication Shop/Advertisement Name"),
    EXIST_ENTITY_REQUEST("4004", HttpStatus.BAD_REQUEST, "Exist Entity Request"),
    NOT_EXIST_ENTITY_REQUEST("4005", HttpStatus.BAD_REQUEST, "Not Exist Entity Request"),

    // Server, File Up/DownLoad Error
    SERVER_ERROR("5000", HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    FILE_UPLOAD("5001", HttpStatus.INTERNAL_SERVER_ERROR, "File Upload Fail"),
    FILE_DOWNLOAD("5002", HttpStatus.INTERNAL_SERVER_ERROR, "File Download Fail"),

    // Access Denied Error
    ACCESS_DENIED_ERROR("4010", HttpStatus.UNAUTHORIZED, "Access Denied Token Error"),

    // Token Error Set
    TOKEN_INVALID_ERROR("4011", HttpStatus.UNAUTHORIZED, "Invalid Token Error"),
    TOKEN_MALFORMED_ERROR("4012", HttpStatus.UNAUTHORIZED, "Malformed Token Error"),
    TOKEN_EXPIRED_ERROR("4013", HttpStatus.UNAUTHORIZED, "Expired Token Error"),
    TOKEN_TYPE_ERROR("4014", HttpStatus.UNAUTHORIZED, "Type Token Error"),
    TOKEN_UNSUPPORTED_ERROR("4015", HttpStatus.UNAUTHORIZED, "Unsupported Token Error"),
    TOKEN_UNKNOWN_ERROR("4016", HttpStatus.UNAUTHORIZED, "Unknown Error");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}