package com.sosorin.ranabot.bot;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.sosorin.ranabot.entity.bot.GroupInfo;
import com.sosorin.ranabot.entity.bot.IBot;
import com.sosorin.ranabot.entity.message.Message;
import com.sosorin.ranabot.entity.send.MessageSendResponse;
import com.sosorin.ranabot.exception.WebSocketRequestException;
import com.sosorin.ranabot.websocket.WebSocketRequestService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rana-bot
 * @since 2025/6/26  14:58
 */
@Service
@Slf4j
public class NapCatWebSocketBotImpl implements IBot {

    @Resource(name = "napCatWebSocketApiClient")
    private WebSocket napCatWebSocketApiClient;
    
    private final WebSocketRequestService webSocketRequestService;
    
    @Autowired
    public NapCatWebSocketBotImpl(WebSocketRequestService webSocketRequestService) {
        this.webSocketRequestService = webSocketRequestService;
    }

    @Override
    public WebSocket getWebSocketClient() {
        return napCatWebSocketApiClient;
    }

    /**
     * 直接发送string消息
     *
     * @param message 字符串JSON消息
     * @return 返回响应中的data字段值，可能是任意JSON对象
     * @throws WebSocketRequestException 当API调用失败时抛出异常
     */
    @Override
    public Object sendRawMessageStr(String message) throws WebSocketRequestException {
        log.debug("发送消息：{}", message);
        if (StrUtil.isEmpty(message)) {
            log.warn("消息为空");
            throw new WebSocketRequestException("消息为空");
        }
        
        try {
            // 解析JSON消息，提取action和echo
            JSONObject jsonObject = JSON.parseObject(message);
            String action = jsonObject.getString("action");
            
            if (StrUtil.isEmpty(action)) {
                throw new WebSocketRequestException("消息缺少action字段");
            }
            
            // 使用WebSocketRequestService发送请求并获取响应
            return webSocketRequestService.sendRawRequest(napCatWebSocketApiClient, message);
        } catch (Exception e) {
            if (e instanceof WebSocketRequestException) {
                throw e;
            }
            log.error("发送消息失败", e);
            throw new WebSocketRequestException("发送消息失败: " + e.getMessage(), e);
        }
    }

    /**
     * 发送私聊消息
     *
     * @param userId QQ号
     * @param messages 消息
     * @return 消息ID
     * @throws WebSocketRequestException 当API调用失败时抛出异常
     */
    @Override
    public Long sendPrivateMessage(String userId, Message... messages) throws WebSocketRequestException {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", userId);
            params.put("message", Arrays.asList(messages));
            
            MessageSendResponse response = webSocketRequestService.sendRequest(
                    napCatWebSocketApiClient,
                    "send_private_msg",
                    params,
                    new TypeReference<MessageSendResponse>() {}
            );
            
            return response != null ? response.getMessageId() : null;
        } catch (Exception e) {
            log.error("发送私聊消息失败", e);
            throw new WebSocketRequestException("发送私聊消息失败: " + e.getMessage(), e);
        }
    }

    /**
     * 发送群消息
     *
     * @param groupId 群号
     * @param messages 消息
     * @return 消息ID
     * @throws WebSocketRequestException 当API调用失败时抛出异常
     */
    @Override
    public Long sendGroupMessage(String groupId, Message... messages) throws WebSocketRequestException {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("group_id", groupId);
            params.put("message", Arrays.asList(messages));
            
            MessageSendResponse response = webSocketRequestService.sendRequest(
                    napCatWebSocketApiClient,
                    "send_group_msg",
                    params,
                    new TypeReference<MessageSendResponse>() {}
            );
            
            return response != null ? response.getMessageId() : null;
        } catch (Exception e) {
            log.error("发送群消息失败", e);
            throw new WebSocketRequestException("发送群消息失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取QQ群列表
     *
     * @return 群信息列表
     * @throws WebSocketRequestException 当API调用失败时抛出异常
     */
    @Override
    public List<GroupInfo> getGroupList() throws WebSocketRequestException {
        try {
            // 使用通用请求服务发送请求并获取结果
            return webSocketRequestService.sendRequest(
                    napCatWebSocketApiClient,
                    "get_group_list",
                    new HashMap<>(),
                    new TypeReference<List<GroupInfo>>() {}
            );
        } catch (Exception e) {
            log.error("获取群列表失败", e);
            throw new WebSocketRequestException("获取群列表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean close() {
        return napCatWebSocketApiClient.close(1000, "");
    }
}
