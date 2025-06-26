package com.sosorin.ranabot.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.MessageData;
import lombok.Data;

/**
 * 音乐分享消息数据
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MusicMessageData implements MessageData {
    private String type; // qq, 163, kugou, migu, kuwo 或 custom
    private String id; // 音乐ID（非自定义音源）
    private String url; // 点击后跳转URL（自定义音源）
    private String audio; // 音乐URL（自定义音源）
    private String title; // 标题（自定义音源）
    private String image; // 封面图（自定义音源）
    private String singer; // 歌手（自定义音源）

    @JsonIgnore
    @Override
    public MessageDataType getType() {
        return MessageDataType.MUSIC;
    }
} 