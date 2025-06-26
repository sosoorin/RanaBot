package com.sosorin.ranabot.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.MessageData;
import lombok.Data;

import java.util.List;

/**
 * 转发消息节点数据
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeMessageData implements MessageData {
    private String id;
    private List<Message> content;
    private String userId;
    private String nickname;

    @JsonIgnore
    @Override
    public MessageDataType getType() {
        return MessageDataType.NODE;
    }
} 