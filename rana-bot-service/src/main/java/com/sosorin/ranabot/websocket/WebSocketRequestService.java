package com.sosorin.ranabot.websocket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.sosorin.ranabot.entity.send.ActionResponse;
import com.sosorin.ranabot.entity.send.WebSocketEntity;
import com.sosorin.ranabot.exception.WebSocketRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

/**
 * WebSocket请求服务
 * 提供统一的WebSocket请求处理机制
 *
 * @author rana-bot
 * @since 2025/6/27 15:30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketRequestService {

    private final WebSocketResponseHandler responseHandler;

    /**
     * 发送WebSocket请求并等待响应
     *
     * @param webSocket WebSocket客户端
     * @param action 动作名称
     * @param params 参数
     * @param typeReference 返回数据类型引用
     * @param <T> 返回数据类型
     * @return 处理后的响应数据
     * @throws WebSocketRequestException 当请求失败或返回错误码时抛出
     */
    public <T> T sendRequest(WebSocket webSocket, String action, Map<String, Object> params, TypeReference<T> typeReference) {
        // 生成唯一的echo标识
        String echo = action + "_" + UUID.randomUUID().toString().substring(0, 8);
        
        // 构建请求实体
        WebSocketEntity entity = WebSocketEntity.builder()
                .action(action)
                .params(params)
                .echo(echo)
                .build();
        
        // 发送请求
        String requestJson = JSON.toJSONString(entity);
        boolean sent = webSocket.send(requestJson);
        
        if (!sent) {
            throw new WebSocketRequestException("发送WebSocket消息失败");
        }
        
        log.debug("发送WebSocket请求: {}", requestJson);
        
        // 等待响应
        ActionResponse<?> response = responseHandler.waitForResponse(echo);
        
        // 检查响应状态
        if (!ActionResponse.OK_CODE.equals(response.getStatus())) {
            throw new WebSocketRequestException(
                    String.format("API请求失败: action=%s, retcode=%d, message=%s", 
                            action, response.getRetcode(), response.getMessage())
            );
        }
        
        // 数据转换
        if (response.getData() != null) {
            return JSON.parseObject(JSON.toJSONString(response.getData()), typeReference);
        }
        
        return null;
    }

    /**
     * 发送原始JSON请求并等待响应
     *
     * @param webSocket WebSocket客户端
     * @param rawJson 原始JSON消息
     * @return 响应中的data字段值
     * @throws WebSocketRequestException 当请求失败或返回错误码时抛出
     */
    public Object sendRawRequest(WebSocket webSocket, String rawJson) throws WebSocketRequestException {
        try {
            // 解析JSON消息，提取echo或者添加echo
            JSONObject jsonObject = JSON.parseObject(rawJson);
            String echo = jsonObject.getString("echo");
            
            // 如果没有echo，添加一个
            if (echo == null || echo.isEmpty()) {
                echo = "raw_" + UUID.randomUUID().toString().substring(0, 8);
                jsonObject.put("echo", echo);
                rawJson = jsonObject.toJSONString();
            }
            
            // 发送请求
            boolean sent = webSocket.send(rawJson);
            if (!sent) {
                throw new WebSocketRequestException("发送WebSocket消息失败");
            }
            
            log.debug("发送原始WebSocket请求: {}", rawJson);
            
            // 等待响应
            ActionResponse<?> response = responseHandler.waitForResponse(echo);
            
            // 检查响应状态
            if (!ActionResponse.OK_CODE.equals(response.getStatus())) {
                throw new WebSocketRequestException(
                        String.format("API请求失败: retcode=%d, message=%s", 
                                response.getRetcode(), response.getMessage())
                );
            }
            
            // 返回data字段
            return response.getData();
        } catch (Exception e) {
            if (e instanceof WebSocketRequestException) {
                throw e;
            }
            log.error("处理WebSocket请求失败", e);
            throw new WebSocketRequestException("处理WebSocket请求失败: " + e.getMessage(), e);
        }
    }
} 