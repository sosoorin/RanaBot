package com.sosorin.ranabot.websocket.listener;

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
 * NapCat WebSocket 事件监听器
 * 用于接收和处理机器人事件
 *
 * @author rana-bot
 * @since 2025/6/26  14:44
 */
@Slf4j
@Component
public class NapCatWebSocketEventListener extends WebSocketListener {

    private final PluginManager pluginManager;

    @Autowired
    public NapCatWebSocketEventListener(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        super.onOpen(webSocket, response);
        ResponseBody body = response.body();
        try {
            log.info("Event WebSocket连接已建立: {}", body != null ? body.string() : "");
        } catch (IOException e) {
            log.error("读取响应内容失败: {}", e.getMessage());
        }
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        super.onMessage(webSocket, text);
        log.info("收到事件消息: {}", text);

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
        log.info("Event WebSocket已关闭: {}, 代码: {}", reason, code);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosing(webSocket, code, reason);
        log.info("Event WebSocket正在关闭: {}, 代码: {}", reason, code);
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        super.onFailure(webSocket, t, response);
        log.error("Event WebSocket连接失败: {}", t.getMessage());
    }
}
