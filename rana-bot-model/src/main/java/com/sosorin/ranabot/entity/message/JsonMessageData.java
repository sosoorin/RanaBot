package com.sosorin.ranabot.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.MessageData;
import lombok.Data;

/**
 * JSON消息数据
 */
@Data
public class JsonMessageData implements MessageData {
    private String data;

    @JsonIgnore
    @Override
    public MessageDataType getType() {
        return MessageDataType.JSON;
    }
} 