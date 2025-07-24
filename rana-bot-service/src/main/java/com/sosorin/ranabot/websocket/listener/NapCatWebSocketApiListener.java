package com.sosorin.ranabot.websocket.listener;

import com.sosorin.ranabot.websocket.WebSocketResponseHandler;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * NapCat WebSocket API监听器
 * 专门用于处理API调用的响应
 *
 * @author rana-bot
 * @since 2025/6/27 14:44
 */
@Slf4j
@Component
public class NapCatWebSocketApiListener extends WebSocketListener {

    private final WebSocketResponseHandler responseHandler;

    @Autowired
    public NapCatWebSocketApiListener(WebSocketResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        ResponseBody body = response.body();
        try {
            log.info("API WebSocket连接已建立: {}", body != null ? body.string() : "");
        } catch (IOException e) {
            log.error("读取响应内容失败: {}", e.getMessage());
        }
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        super.onMessage(webSocket, text);
        log.info("收到API响应: {}", text);

        // 使用响应处理器处理API响应
        if (!responseHandler.handleResponse(text)) {
            log.warn("未找到匹配的请求处理器，可能是未注册的API响应: {}", text);
        }
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
        log.info("API WebSocket已关闭: {}, 代码: {}", reason, code);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosing(webSocket, code, reason);
        log.info("API WebSocket正在关闭: {}, 代码: {}", reason, code);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        log.error("API WebSocket连接失败: {}", t.getMessage());
    }
}
