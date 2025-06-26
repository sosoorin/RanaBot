package com.sosorin.ranabot.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.MessageData;
import lombok.Data;

/**
 * 猜拳消息数据
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RpsMessageData implements MessageData {
    private String result;

    @JsonIgnore
    @Override
    public MessageDataType getType() {
        return MessageDataType.RPS;
    }
} 