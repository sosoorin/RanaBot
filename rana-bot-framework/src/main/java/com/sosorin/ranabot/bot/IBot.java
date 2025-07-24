package com.sosorin.ranabot.bot;

import okhttp3.WebSocket;

/**
 * @author rana-bot
 * @since 2025/6/26  14:58
 */
public interface IBot {

    public WebSocket getWebSocketClient();

    /**
     * 直接发送string消息
     * @param message 字符串
     * @return 发送结果
     */
    boolean sendRawMessageStr(String message);

    boolean close();


}
