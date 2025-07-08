package com.portal.bid.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API response format for errors and success messages
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;
    private List<String> errors;
    
    public static ApiResponse of(int status, String message, String path) {
        return ApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .message(message)
                .path(path)
                .build();
    }
    
    public static ApiResponse of(int status, String message, String path, List<String> errors) {
        return ApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .message(message)
                .path(path)
                .errors(errors)
                .build();
    }
}