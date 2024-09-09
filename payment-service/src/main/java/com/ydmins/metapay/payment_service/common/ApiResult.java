package com.ydmins.metapay.payment_service.common;

import lombok.Getter;

@Getter
public class ApiResult {
    private final int httpStatusCode;
    private final String status;
    private final String message;

    private ApiResult(int httpStatusCode, String status, String message) {
        this.httpStatusCode = httpStatusCode;
        this.status = status;
        this.message = message;
    }

    public static ApiResult success(int httpStatusCode, String message) {
        return new ApiResult(httpStatusCode, "SUCCESS", message);
    }

    public static ApiResult unauthorized(int httpStatusCode, String message) {
        return new ApiResult(httpStatusCode,"UNAUTHORIZED", message);
    }

    public static ApiResult forbidden(int httpStatusCode, String message) {
        return new ApiResult(httpStatusCode,"FORBIDDEN", message);
    }

    public static ApiResult badRequest(int httpStatusCode, String message) {
        return new ApiResult(httpStatusCode,"BAD_REQUEST", message);
    }

    public static ApiResult error(int httpStatusCode, String message) {
        return new ApiResult(httpStatusCode,"ERROR", message);
    }

    public static ApiResult notFound(int httpStatusCode, String message) {
        return new ApiResult(httpStatusCode,"NOT_FOUND", message);
    }

    public static ApiResult conflict(int httpStatusCode, String message) {
        return new ApiResult(httpStatusCode,"CONFLICT", message);
    }
}
