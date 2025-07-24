package com.sosorin.ranabot.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.IMessageData;
import lombok.Data;

/**
 * 小程序卡片消息数据
 */
@Data
public class LightappMessageData implements IMessageData {
    private String content;

    @JsonIgnore
    @Override
    public MessageDataType getType() {
        return MessageDataType.LIGHTAPP;
    }
} 