package com.sosorin.ranabot.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.MessageData;
import lombok.Data;

/**
 * @消息数据
 */
@Data
public class AtMessageData implements MessageData {
    private String qq; // 可以是具体QQ号或"all"表示@全体

    @JsonIgnore
    @Override
    public MessageDataType getType() {
        return MessageDataType.AT;
    }
} 