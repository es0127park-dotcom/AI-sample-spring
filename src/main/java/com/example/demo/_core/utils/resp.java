package com.example.demo._core.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class Resp<T> {
    private final Integer status; // 1 for success, -1 for failure
    private final String msg;
    private final T body;

    public static <T> Resp<T> ok(T body) {
        return new Resp<>(1, "성공", body);
    }

    public static <T> Resp<T> fail(String msg) {
        return new Resp<>(-1, msg, null);
    }

    public static <T> Resp<T> fail(HttpStatus httpStatus, String msg) {
        return new Resp<>(httpStatus.value(), msg, null);
    }
}
