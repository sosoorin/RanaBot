package com.sosorin.ranabot.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.IMessageData;
import lombok.Data;

/**
 * 语音消息数据
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecordMessageData implements IMessageData {
    private String file;
    private String name;
    private String url;
    private String path;
    private String fileId;
    private String fileSize;
    private String fileUnique;

    @JsonIgnore
    @Override
    public MessageDataType getType() {
        return MessageDataType.RECORD;
    }
} 