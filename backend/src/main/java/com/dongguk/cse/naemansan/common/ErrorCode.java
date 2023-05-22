package com.dongguk.cse.naemansan.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    DEFAULT_ERROR_CODE("5000", HttpStatus.INTERNAL_SERVER_ERROR, "기본 에러 메시지입니다."),
    INVALID_TOWN_LINK("4000", HttpStatus.NOT_FOUND, "유효하지 않은 마을 링크입니다."),
    NOT_FOUND_SNOWMAN("4001", HttpStatus.NOT_FOUND, "찾을 수 없는 눈사람입니다."),
    ALREADY_EXIST_LETTER("4002", HttpStatus.BAD_REQUEST, "해당 눈사람에는 이미 편지가 존재합니다."),
    NOT_FOUND_USER("4003", HttpStatus.NOT_FOUND, "찾을 수 없는 회원입니다."),
    NOT_FOUND_TOWN("4004", HttpStatus.NOT_FOUND, "찾을 수 없는 마을입니다."),
    UNAUTHORIZED("4005", HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    REQUIRED_LOGIN("4006", HttpStatus.BAD_REQUEST, "로그인이 필요합니다."),
    DELETED_USER_TOWN("4007", HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 계정의 마을입니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}

