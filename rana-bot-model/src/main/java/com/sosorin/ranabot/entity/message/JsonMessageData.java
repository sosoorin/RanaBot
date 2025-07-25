package com.sosorin.ranabot.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.IMessageData;
import lombok.Data;

/**
 * JSON消息数据
 */
@Data
public class JsonMessageData implements IMessageData {
    private String data;

    @JsonIgnore
    @Override
    public MessageDataType getType() {
        return MessageDataType.JSON;
    }
} 