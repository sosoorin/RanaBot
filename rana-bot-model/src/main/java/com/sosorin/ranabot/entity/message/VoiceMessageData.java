package com.sosorin.ranabot.entity.message;

import com.alibaba.fastjson2.annotation.JSONField;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.IMessageData;
import lombok.Data;

/**
 * 语音消息数据
 * NapCat扩展的消息类型，用于发送语音
 *
 * @author rana-bot
 */
@Data
public class VoiceMessageData implements IMessageData {
    /**
     * 语音文件名
     */
    @JSONField(name = "file")
    private String file;
    
    /**
     * 语音URL
     */
    @JSONField(name = "url")
    private String url;
    
    /**
     * 是否作为语音通话发送
     */
    @JSONField(name = "voice_call")
    private Boolean voiceCall;
    
    /**
     * 语音时长
     */
    @JSONField(name = "duration")
    private Integer duration;

    @Override
    public MessageDataType getType() {
        return MessageDataType.VOICE;
    }
} 