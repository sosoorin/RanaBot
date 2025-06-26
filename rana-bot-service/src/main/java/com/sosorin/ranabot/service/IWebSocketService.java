package com.sosorin.ranabot.service;

import okhttp3.WebSocket;

/**
 * @author rana-bot
 * @since 2025/6/26  14:58
 */
public interface IWebSocketService {
    public WebSocket getWebSocketClient();

    boolean send(String message);

    boolean close();

}
