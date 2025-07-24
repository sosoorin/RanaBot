package com.sosorin.ranabot.websocket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.sosorin.ranabot.entity.send.ActionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket响应处理器
 * 用于处理API请求的响应，通过echo字段匹配请求和响应
 *
 * @author rana-bot
 * @since 2025/6/27 10:30
 */
@Slf4j
@Component
public class WebSocketResponseHandler {

    /**
     * 请求回调映射，以echo字段为键，存储等待响应的Future
     */
    private final Map<String, CompletableFuture<ActionResponse<?>>> pendingRequests = new ConcurrentHashMap<>();

    /**
     * 注册一个请求，返回一个用于接收响应的Future
     *
     * @param echo 请求的echo字段
     * @param <T> 响应数据类型
     * @return 用于接收响应的Future
     */
    @SuppressWarnings("unchecked")
    public <T> CompletableFuture<ActionResponse<T>> registerRequest(String echo) {
        CompletableFuture<ActionResponse<T>> future = new CompletableFuture<>();
        pendingRequests.put(echo, (CompletableFuture) future);
        return future;
    }

    /**
     * 处理收到的WebSocket消息
     * 如果消息包含echo字段且有对应的pending request，则完成相应的Future
     *
     * @param message WebSocket消息内容
     * @return 是否处理了该消息
     */
    public boolean handleResponse(String message) {
        try {
            JSONObject jsonObject = JSON.parseObject(message);
            String echo = jsonObject.getString("echo");
            
            if (echo != null && pendingRequests.containsKey(echo)) {
                CompletableFuture<ActionResponse<?>> future = pendingRequests.remove(echo);
                if (future != null) {
                    ActionResponse<?> response = JSON.parseObject(message, ActionResponse.class);
                    future.complete(response);
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("处理WebSocket响应失败", e);
        }
        return false;
    }

    /**
     * 取消一个pending request
     *
     * @param echo 请求的echo字段
     */
    public void cancelRequest(String echo) {
        CompletableFuture<ActionResponse<?>> future = pendingRequests.remove(echo);
        if (future != null) {
            future.cancel(true);
        }
    }
} 