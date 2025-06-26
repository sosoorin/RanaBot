package com.sosorin.ranabot.util;

import com.alibaba.fastjson2.JSON;
import com.sosorin.ranabot.entity.message.*;
import com.sosorin.ranabot.enums.MessageDataType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息工具类
 * 提供处理消息相关的工具方法
 */
@Slf4j
public class MessageUtil {

    /**
     * 创建纯文本消息
     *
     * @param text 文本内容
     * @return 消息对象
     */
    public static Message createTextMessage(String text) {
        TextMessageData data = new TextMessageData();
        data.setText(text);
        return new Message(MessageDataType.TEXT.getType(), data);
    }

    /**
     * 创建图片消息
     *
     * @param file 图片文件路径或URL
     * @return 消息对象
     */
    public static Message createImageMessage(String file) {
        ImageMessageData data = new ImageMessageData();
        data.setFile(file);
        return new Message(MessageDataType.IMAGE.getType(), data);
    }

    /**
     * 创建At消息
     *
     * @param qq 要At的QQ号
     * @return 消息对象
     */
    public static Message createAtMessage(String qq) {
        AtMessageData data = new AtMessageData();
        data.setQq(qq);
        return new Message(MessageDataType.AT.getType(), data);
    }

    /**
     * 创建回复消息
     *
     * @param id 要回复的消息ID
     * @return 消息对象
     */
    public static Message createReplyMessage(String id) {
        ReplyMessageData data = new ReplyMessageData();
        data.setId(id);
        return new Message(MessageDataType.REPLY.getType(), data);
    }

    /**
     * 合并多个消息为JSON数组字符串
     *
     * @param messages 消息列表
     * @return JSON数组字符串
     */
    public static String messagesToJsonArray(List<Message> messages) {
        return JSON.toJSONString(messages);
    }

    /**
     * 合并多个消息为JSON数组字符串
     *
     * @param messages 消息数组
     * @return JSON数组字符串
     */
    public static String messagesToJsonArray(Message... messages) {
        return messagesToJsonArray(Arrays.asList(messages));
    }

    /**
     * 解析消息JSON数组字符串为消息列表
     *
     * @param jsonArrayStr 消息JSON数组字符串
     * @return 消息列表
     */
    public static List<Message> parseMessageArray(String jsonArrayStr) {
        try {
            return JSON.parseArray(jsonArrayStr, Message.class);
        } catch (Exception e) {
            log.error("解析消息数组失败: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 提取纯文本内容（去除所有非文本消息）
     *
     * @param messageList 消息列表
     * @return 纯文本内容
     */
    public static String extractTextContent(List<Message> messageList) {
        return messageList.stream()
                .filter(msg -> MessageDataType.TEXT.getType().equals(msg.getType()))
                .map(msg -> {
                    TextMessageData textData = (TextMessageData) msg.getData();
                    return textData.getText();
                })
                .collect(Collectors.joining(" "));
    }

    /**
     * 检测消息是否包含关键词
     *
     * @param messageList 消息列表
     * @param keyword     关键词
     * @return 是否包含关键词
     */
    public static boolean containsKeyword(List<Message> messageList, String keyword) {
        String text = extractTextContent(messageList);
        return text.contains(keyword);
    }

    /**
     * 构建复合消息（包含多种类型）
     *
     * @param elements 消息元素列表
     * @return 消息JSON字符串
     */
    public static String buildCompoundMessage(Message... elements) {
        return messagesToJsonArray(elements);
    }

    /**
     * 快速构建文本+图片的消息
     *
     * @param text     文本内容
     * @param imageUrl 图片URL
     * @return 消息JSON字符串
     */
    public static String buildTextWithImage(String text, String imageUrl) {
        Message textMsg = createTextMessage(text);
        Message imageMsg = createImageMessage(imageUrl);
        return buildCompoundMessage(textMsg, imageMsg);
    }

    /**
     * 快速构建回复+文本的消息
     *
     * @param replyMsgId 回复的消息ID
     * @param text       文本内容
     * @return 消息JSON字符串
     */
    public static String buildReplyWithText(String replyMsgId, String text) {
        Message replyMsg = createReplyMessage(replyMsgId);
        Message textMsg = createTextMessage(text);
        return buildCompoundMessage(replyMsg, textMsg);
    }
} 