package com.example.wishlistservice.global.exception;

import lombok.Getter;

@Getter
public enum SuccessCode {


    SELECT_SUCCESS(200, "200", "SELECT SUCCESS"),
    DELETE_SUCCESS(200, "200", "DELETE SUCCESS"),
    INSERT_SUCCESS(200, "200", "INSERT SUCCESS"),
    UPDATE_SUCCESS(200, "200", "UPDATE SUCCESS")
    ;

    private final int status;
    private final String code;
    private final String message;

    SuccessCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
