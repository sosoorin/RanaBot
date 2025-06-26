package com.sosorin.ranabot.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.MessageData;
import lombok.Data;

/**
 * QQ表情消息数据
 */
@Data
public class FaceMessageData implements MessageData {
    private String id;

    @JsonIgnore
    @Override
    public MessageDataType getType() {
        return MessageDataType.FACE;
    }
} 