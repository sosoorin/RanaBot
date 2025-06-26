package com.sosorin.ranabot.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.MessageData;
import lombok.Data;

/**
 * Markdown消息数据
 */
@Data
public class MarkdownMessageData implements MessageData {
    private String content;

    @JsonIgnore
    @Override
    public MessageDataType getType() {
        return MessageDataType.MARKDOWN;
    }
} 