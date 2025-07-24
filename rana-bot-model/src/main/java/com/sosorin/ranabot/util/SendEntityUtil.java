package com.sosorin.ranabot.util;

import com.alibaba.fastjson2.JSON;
import com.sosorin.ranabot.entity.message.Message;
import com.sosorin.ranabot.entity.send.WebSocketEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author rana-bot
 * @since 2025/6/26  21:05
 */
public class SendEntityUtil {

    /**
     * 构建发送群消息的JSON字符串
     *
     * @param groupId  群号
     * @param messages 消息列表
     * @return JSON字符串
     */
    public static String buildSendGroupMessageStr(String groupId, List<Message> messages) {
        Map<String, Object> params = Map.of("group_id", groupId, "message", messages);
        WebSocketEntity msg = WebSocketEntity.builder().action("send_group_msg").params(params).build();
        return JSON.toJSONString(msg);
    }

    /**
     * 构建发送群消息的JSON字符串
     *
     * @param groupId  群号
     * @param messages 消息列表
     * @return JSON字符串
     */
    public static String buildSendGroupMessageStr(String groupId, Message... messages) {
        List<Message> list = Arrays.stream(messages).toList();
        return buildSendGroupMessageStr(groupId, list);
    }

    /**
     * 构建发送群消息的JSON字符串
     *
     * @param userId   QQ号
     * @param messages 消息列表
     * @return JSON字符串
     */
    public static String buildSendPrivateMessageStr(String userId, List<Message> messages) {
        Map<String, Object> params = Map.of("user_id", userId, "message", messages);
        WebSocketEntity msg = WebSocketEntity.builder().action("send_private_msg").params(params).build();
        return JSON.toJSONString(msg);
    }

    /**
     * 构建发送群消息的JSON字符串
     *
     * @param userId   QQ号
     * @param messages 消息列表
     * @return JSON字符串
     */
    public static String buildSendPrivateMessageStr(String userId, Message... messages) {
        List<Message> list = Arrays.stream(messages).toList();
        return buildSendPrivateMessageStr(userId, list);
    }
}
