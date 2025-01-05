package com.ThreadLine.backend.exception.wrapper;

public class JsonProcessingExceptionWrapper extends RuntimeException {
    public JsonProcessingExceptionWrapper(String message, Throwable cause) {
        super(message, cause);
    }
}
