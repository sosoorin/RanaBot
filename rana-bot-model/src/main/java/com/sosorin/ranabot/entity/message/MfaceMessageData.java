package com.sosorin.ranabot.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.MessageData;
import lombok.Data;

/**
 * QQ表情包消息数据
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MfaceMessageData implements MessageData {
    private String emojiId;
    private String emojiPackageId;
    private String key;
    private String summary;

    @JsonIgnore
    @Override
    public MessageDataType getType() {
        return MessageDataType.MFACE;
    }
} 