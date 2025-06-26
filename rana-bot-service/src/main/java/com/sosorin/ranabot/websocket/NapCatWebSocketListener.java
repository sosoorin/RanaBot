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
import java.util.List;

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


    @Autowired
    public NapCatWebSocketListener(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
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

        EventBody eventBody = EventParseUtil.parseEvent(text);
        if (eventBody != null) {
            log.info("解析事件: {}", eventBody);

            // 使用插件系统处理事件
            List<PluginManager.PluginResult> results = pluginManager.handleEvent(eventBody);

            // 输出插件处理结果
            if (!results.isEmpty()) {
                log.info("插件处理结果: {}", results);

                // 如果有需要，可以根据插件处理结果发送响应
                // 例如，找到第一个成功处理的结果
                results.stream()
                        .filter(PluginManager.PluginResult::isSuccess)
                        .filter(result -> result.getResult() != null)
                        .findFirst()
                        .ifPresent(result -> {
                            /*
                            String response = EventParseUtil.createResponseFromEvent(text, result.getResult());
                            webSocket.send(response);
                            log.info("已发送响应: {}", response);
                             */
                        });
            } else {
                log.info("没有插件处理此事件");
            }
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
