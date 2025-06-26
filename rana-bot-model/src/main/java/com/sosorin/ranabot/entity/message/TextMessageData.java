package com.sosorin.ranabot.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.MessageData;
import lombok.Data;

/**
 * 文本消息数据
 */
@Data
public class TextMessageData implements MessageData {
    private String text;

    @JsonIgnore
    @Override
    public MessageDataType getType() {
        return MessageDataType.TEXT;
    }
} 