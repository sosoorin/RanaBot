package com.sosorin.ranabot.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.MessageData;
import lombok.Data;

/**
 * 文件消息数据
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileMessageData implements MessageData {
    private String name;
    private String file;
    private String path;
    private String url;
    private String fileId;
    private String fileSize;
    private String fileUnique;

    @JsonIgnore
    @Override
    public MessageDataType getType() {
        return MessageDataType.FILE;
    }
} 