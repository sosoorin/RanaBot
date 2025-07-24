package com.sosorin.ranabot.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.IMessageData;
import lombok.Data;

/**
 * 文本消息数据
 */
@Data
public class TextMessageData implements IMessageData {
    private String text;

    @JsonIgnore
    @Override
    public MessageDataType getType() {
        return MessageDataType.TEXT;
    }
} 