package com.sosorin.ranabot.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.IMessageData;
import lombok.Data;

/**
 * 图片消息数据
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageMessageData implements IMessageData {
    private String name;
    private String summary;
    private String file;
    private String subType;
    private String fileId;
    private String url;
    private String path;
    private String fileSize;
    private String fileUnique;

    @JsonIgnore
    @Override
    public MessageDataType getType() {
        return MessageDataType.IMAGE;
    }
} 