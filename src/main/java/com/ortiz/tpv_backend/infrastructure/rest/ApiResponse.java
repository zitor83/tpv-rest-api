package com.ortiz.tpv_backend.infrastructure.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final Object meta;
    @JsonSerialize(using = InstantSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private final Instant timestamp;

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .meta(null)
            .timestamp(Instant.now())
            .build();
    }

    public static <T> ApiResponse<T> success(String message, T data, Object meta) {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .meta(meta)
            .timestamp(Instant.now())
            .build();
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return ApiResponse.<T>builder()
            .success(false)
            .message(message)
            .data(data)
            .meta(null)
            .timestamp(Instant.now())
            .build();
    }

    public static <T> ApiResponse<T> error(String message, T data, Object meta) {
        return ApiResponse.<T>builder()
            .success(false)
            .message(message)
            .data(data)
            .meta(meta)
            .timestamp(Instant.now())
            .build();
    }
}

