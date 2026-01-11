package com.maguaJun.magua.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    // 成功响应
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, "success", data);
    }

    // 成功响应(无数据)
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(0, message, null);
    }

    // 失败响应
    public static <T> ApiResponse<T> fail(String message, int code) {
        return new ApiResponse<>(code, message, null);
    }

    // 失败响应(默认错误码)
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(500, message, null);
    }
}