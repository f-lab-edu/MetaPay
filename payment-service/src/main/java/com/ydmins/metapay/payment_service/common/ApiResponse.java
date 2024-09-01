package com.ydmins.metapay.payment_service.common;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final ApiResult result;
    private final T data;

    public ApiResponse(ApiResult result, T data){
        this.result = result;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(int httpStatusCode, String message, T data) {
        return new ApiResponse<>(ApiResult.success(httpStatusCode, message), data);
    }

    public static <T> ApiResponse<T> unauthorized(int httpStatusCode, String message, T data) {
        return new ApiResponse<>(ApiResult.unauthorized(httpStatusCode, message), data);
    }

    public static <T> ApiResponse<T> forbidden(int httpStatusCode, String message, T data) {
        return new ApiResponse<>(ApiResult.forbidden(httpStatusCode, message), data);
    }

    public static <T> ApiResponse<T> badRequest(int httpStatusCode, String message, T data) {
        return new ApiResponse<>(ApiResult.badRequest(httpStatusCode, message), data);
    }

    public static <T> ApiResponse<T> error(int httpStatusCode, String message, T data) {
        return new ApiResponse<>(ApiResult.error(httpStatusCode, message), data);
    }

    public static <T> ApiResponse<T> notFound(int httpStatusCode, String message, T data) {
        return new ApiResponse<>(ApiResult.notFound(httpStatusCode, message), data);
    }

    public static <T> ApiResponse<T> conflict(int httpStatusCode, String message, T data) {
        return new ApiResponse<>(ApiResult.conflict(httpStatusCode, message), data);
    }
}
