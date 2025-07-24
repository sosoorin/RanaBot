package com.sosorin.ranabot.exception;

/**
 * WebSocket请求异常
 */
public class WebSocketRequestException extends RuntimeException {
    public WebSocketRequestException(String message) {
        super(message);
    }

    public WebSocketRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}