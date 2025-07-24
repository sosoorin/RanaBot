package com.sosorin.ranabot.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.IMessageData;
import lombok.Data;

/**
 * 骰子消息数据
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiceMessageData implements IMessageData {
    private String result;

    @JsonIgnore
    @Override
    public MessageDataType getType() {
        return MessageDataType.DICE;
    }
} 