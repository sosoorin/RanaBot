package com.sosorin.ranabot.entity.message;

import com.alibaba.fastjson2.annotation.JSONField;
import com.sosorin.ranabot.model.MessageData;
import lombok.Data;

/**
 * 消息封装类
 *
 * @author rana-bot
 * @since 2025/6/26  16:38
 */
@Data
public class Message {
    @JSONField(name = "type")
    private String type;

    @JSONField(name = "data")
    private MessageData data;

    public Message() {
    }

    public Message(String type, MessageData data) {
        this.type = type;
        this.data = data;
    }
} 