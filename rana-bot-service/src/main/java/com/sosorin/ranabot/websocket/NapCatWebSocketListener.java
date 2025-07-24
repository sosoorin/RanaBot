package com.sosorin.ranabot.websocket;

import com.sosorin.ranabot.model.EventBody;
import com.sosorin.ranabot.plugin.PluginManager;
import com.sosorin.ranabot.util.EventParseUtil;
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
 * NapCat WebSocket 监听器
 *
 * @author rana-bot
 * @since 2025/6/26  14:44
 */
@Slf4j
@Component
public class NapCatWebSocketListener extends WebSocketListener {

    private final PluginManager pluginManager;
    private final WebSocketResponseHandler responseHandler;

    @Autowired
    public NapCatWebSocketListener(PluginManager pluginManager, WebSocketResponseHandler responseHandler) {
        this.pluginManager = pluginManager;
        this.responseHandler = responseHandler;
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        ResponseBody body = response.body();
        try {
            log.info("WebSocket连接已建立: {}", body != null ? body.string() : "");
        } catch (IOException e) {
            log.error("读取响应内容失败: {}", e.getMessage());
        }
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        super.onMessage(webSocket, text);
        log.info("收到消息: {}", text);

        // 首先尝试处理API响应
        if (responseHandler.handleResponse(text)) {
            // 已处理为API响应，不再作为事件处理
            return;
        }
        
        // 如果不是API响应，则作为事件处理
        EventBody eventBody = EventParseUtil.parseEvent(text);
        if (eventBody != null) {
            log.info("解析事件: {}", eventBody);
            // 使用插件系统处理事件
            pluginManager.handleEventAsync(eventBody);
        } else {
            log.error("事件解析失败");
        }
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
        log.info("WebSocket已关闭: {}, 代码: {}", reason, code);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosing(webSocket, code, reason);
        log.info("WebSocket正在关闭: {}, 代码: {}", reason, code);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        log.error("WebSocket连接失败: {}", t.getMessage());
    }
}
