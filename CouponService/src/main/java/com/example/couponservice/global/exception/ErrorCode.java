package com.example.couponservice.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INSERT_ERROR(200,  "Insert 쿼리 문제 발생"),
    UPDATE_ERROR(200,  "Update 쿼리 문제 발생"),
    DELETE_ERROR(200,  "Delete 쿼리 문제 발생"),

    NOT_VALID_ERROR(404, "validation 오류"),
    NOT_VALID_HEADER_ERROR(404, "Header에 데이터가 존재하지 않습니다 오류"),
    INTERNAL_SERVER_ERROR(500, "서버 처리 불가"),

    BAD_REQUEST_ERROR(400, "Request 요청 문제"),
    INVALID_TYPE_VALUE(400, "Invalid 문제 발생"),
    MISSING_REQUEST_PARAMETER_ERROR(400, "Request Parameter가 존재하지 않습니다"),
    IO_ERROR(400, "I/O 문제"),
    NOT_FOUND_ERROR(404, "Not Found Exception"),
    NULL_POINT_ERROR(404, "Null Pointer Exception"),

    BUSINESS_EXCEPTION_ERROR(500, "서비스에 문제 발생")
    ;

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
