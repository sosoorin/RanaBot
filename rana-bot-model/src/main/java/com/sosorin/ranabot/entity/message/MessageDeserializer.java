package com.sosorin.ranabot.entity.message;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.MessageData;

import java.lang.reflect.Type;

/**
 * Message反序列化器
 * 负责将JSON对象转换为Message实例，并正确处理data字段的类型
 */
public class MessageDeserializer implements ObjectReader<Message> {

    @Override
    public Message readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        if (jsonReader.nextIfNull()) {
            return null;
        }

        JSONObject jsonObject = jsonReader.readJSONObject();
        if (jsonObject == null) {
            return null;
        }

        // 读取type字段
        String type = jsonObject.getString("type");
        if (type == null) {
            throw new RuntimeException("消息缺少type字段");
        }

        // 读取data字段
        JSONObject dataObject = jsonObject.getJSONObject("data");
        if (dataObject == null) {
            throw new RuntimeException("消息缺少data字段");
        }

        // 创建Message实例
        Message message = new Message();
        message.setType(type);

        // 根据type确定data的具体类型并设置
        MessageDataType dataType = MessageDataType.fromString(type);
        if (dataType == null) {
            throw new RuntimeException("未知的消息类型: " + type);
        }

        // 将data字段转换为对应的MessageData子类
        MessageData data = null;
        switch (dataType) {
            case TEXT:
                data = dataObject.to(TextMessageData.class);
                break;
            case FACE:
                data = dataObject.to(FaceMessageData.class);
                break;
            case IMAGE:
                data = dataObject.to(ImageMessageData.class);
                break;
            case RECORD:
                data = dataObject.to(RecordMessageData.class);
                break;
            case VIDEO:
                data = dataObject.to(VideoMessageData.class);
                break;
            case AT:
                data = dataObject.to(AtMessageData.class);
                break;
            case RPS:
                data = dataObject.to(RpsMessageData.class);
                break;
            case DICE:
                data = dataObject.to(DiceMessageData.class);
                break;
            case CONTACT:
                data = dataObject.to(ContactMessageData.class);
                break;
            case MUSIC:
                data = dataObject.to(MusicMessageData.class);
                break;
            case REPLY:
                data = dataObject.to(ReplyMessageData.class);
                break;
            case FORWARD:
                data = dataObject.to(ForwardMessageData.class);
                break;
            case NODE:
                data = dataObject.to(NodeMessageData.class);
                break;
            case JSON:
                data = dataObject.to(JsonMessageData.class);
                break;
            case MFACE:
                data = dataObject.to(MfaceMessageData.class);
                break;
            case FILE:
                data = dataObject.to(FileMessageData.class);
                break;
            case MARKDOWN:
                data = dataObject.to(MarkdownMessageData.class);
                break;
            case LIGHTAPP:
                data = dataObject.to(LightappMessageData.class);
                break;
            default:
                throw new RuntimeException("不支持的消息类型: " + type);
        }

        message.setData(data);
        return message;
    }
} 