package com.sosorin.ranabot.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.MessageData;
import lombok.Data;

/**
 * 回复消息数据
 */
@Data
public class ReplyMessageData implements MessageData {
    private String id;

    @JsonIgnore
    @Override
    public MessageDataType getType() {
        return MessageDataType.REPLY;
    }
} 