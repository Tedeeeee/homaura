package com.example.wishlistservice.global.api;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private T result;
    private int resultCode;
    private String resultMsg;

    @Builder
    public ApiResponse(final T result,final int resultCode,final String resultMsg) {
        this.result = result;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}
