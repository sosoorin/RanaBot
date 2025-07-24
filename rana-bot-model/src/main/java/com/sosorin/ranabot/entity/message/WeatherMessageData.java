package com.sosorin.ranabot.entity.message;

import com.alibaba.fastjson2.annotation.JSONField;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.IMessageData;
import lombok.Data;

/**
 * 天气消息数据
 * NapCat扩展的消息类型，用于发送天气信息
 *
 * @author rana-bot
 */
@Data
public class WeatherMessageData implements IMessageData {
    /**
     * 城市ID
     */
    @JSONField(name = "city")
    private String city;
    
    /**
     * 城市代码
     */
    @JSONField(name = "code")
    private String code;
    
    /**
     * 天气信息，具体格式由NapCat定义
     */
    @JSONField(name = "data")
    private Object data;

    @Override
    public MessageDataType getType() {
        return MessageDataType.WEATHER;
    }
} 