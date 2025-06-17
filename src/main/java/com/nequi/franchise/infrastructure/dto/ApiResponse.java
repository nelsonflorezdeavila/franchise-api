package com.nequi.franchise.infrastructure.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private Object error;
    private LocalDateTime timestamp;
    
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static <T> ApiResponse<T> error(String message, Object error) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .error(error)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    public static <T> ApiResponse<T> success(T data) {
        return success(data, null);
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return error(message, null);
    }
}
