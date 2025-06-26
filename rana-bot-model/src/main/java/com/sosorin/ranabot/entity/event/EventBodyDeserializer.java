package com.sosorin.ranabot.entity.event;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.sosorin.ranabot.entity.event.message.GroupMessageEvent;
import com.sosorin.ranabot.entity.event.message.PrivateMessageEvent;
import com.sosorin.ranabot.enums.PostType;
import com.sosorin.ranabot.model.EventBody;

import java.lang.reflect.Type;

/**
 * 事件反序列化器
 * 根据post_type字段自动选择正确的事件类型
 *
 * @author rana-bot
 * @since 2025/6/26  20:15
 */
public class EventBodyDeserializer implements ObjectReader<EventBody> {

    @Override
    public EventBody readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        if (jsonReader.nextIfNull()) {
            return null;
        }

        JSONObject jsonObject = jsonReader.readJSONObject();
        if (jsonObject == null) {
            return null;
        }

        // 读取post_type字段
        String postType = jsonObject.getString("post_type");
        if (postType == null) {
            throw new RuntimeException("事件缺少post_type字段");
        }

        // 根据post_type确定具体事件类型并解析
        PostType eventType = PostType.fromString(postType);
        if (eventType == null) {
            throw new RuntimeException("未知的事件类型: " + postType);
        }

        String jsonString = jsonObject.toString();

        // 将JSON对象转换为对应的事件类型
        switch (eventType) {
            case MESSAGE:
                // 根据消息类型细分处理
                String messageType = jsonObject.getString("message_type");
                if (messageType == null) {
                    throw new RuntimeException("消息事件缺少message_type字段");
                }

                switch (messageType) {
                    case "private":
                        return JSON.parseObject(jsonString, PrivateMessageEvent.class);
                    case "group":
                        return JSON.parseObject(jsonString, GroupMessageEvent.class);
                    default:
                        throw new RuntimeException("未支持的消息类型: " + messageType);
                }
            case NOTICE:
                return JSON.parseObject(jsonString, NoticeEventBody.class);
            case REQUEST:
                return JSON.parseObject(jsonString, RequestEventBody.class);
            case META_EVENT:
                return JSON.parseObject(jsonString, MetaEventBody.class);
            default:
                throw new RuntimeException("不支持的事件类型: " + postType);
        }
    }
} 