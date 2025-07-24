package com.sosorin.ranabot.websocket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.sosorin.ranabot.entity.send.ActionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
     * 默认超时时间（毫秒）
     */
    private static final long DEFAULT_TIMEOUT_MS = 5000;

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
     * 同步等待响应
     *
     * @param echo 请求的echo字段
     * @param timeout 超时时间（毫秒）
     * @param <T> 响应数据类型
     * @return 响应对象
     */
    public <T> ActionResponse<T> waitForResponse(String echo, long timeout) {
        CompletableFuture<ActionResponse<T>> future = registerRequest(echo);
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("等待响应被中断: {}", echo);
            return ActionResponse.failed(500, "等待响应被中断", echo);
        } catch (ExecutionException e) {
            log.error("等待响应时发生错误: {}", e.getMessage());
            return ActionResponse.failed(500, "等待响应时发生错误: " + e.getMessage(), echo);
        } catch (TimeoutException e) {
            pendingRequests.remove(echo);
            log.error("等待响应超时: {}", echo);
            return ActionResponse.failed(408, "等待响应超时", echo);
        }
    }

    /**
     * 同步等待响应，使用默认超时时间
     *
     * @param echo 请求的echo字段
     * @param <T> 响应数据类型
     * @return 响应对象
     */
    public <T> ActionResponse<T> waitForResponse(String echo) {
        return waitForResponse(echo, DEFAULT_TIMEOUT_MS);
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