package com.sosorin.ranabot.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.sosorin.ranabot.entity.event.MetaEventBody;
import com.sosorin.ranabot.entity.event.NoticeEventBody;
import com.sosorin.ranabot.entity.event.RequestEventBody;
import com.sosorin.ranabot.entity.event.message.BaseMessageEvent;
import com.sosorin.ranabot.entity.event.message.GroupMessageEvent;
import com.sosorin.ranabot.entity.event.message.PrivateMessageEvent;
import com.sosorin.ranabot.enums.PostType;
import com.sosorin.ranabot.model.EventBody;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 事件解析工具类
 */
@Slf4j
public class EventParseUtil {

    /**
     * 将JSON字符串解析为相应的事件对象
     *
     * @param jsonStr JSON字符串
     * @return 对应的事件对象，如果解析失败则返回null
     */
    public static EventBody parseEvent(String jsonStr) {
        try {
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            if (jsonObject == null) {
                log.error("解析JSON失败: {}", jsonStr);
                return null;
            }

            String postTypeStr = jsonObject.getString("post_type");
            if (postTypeStr == null) {
                log.error("事件缺少post_type字段: {}", jsonStr);
                return null;
            }

            PostType postType = PostType.fromString(postTypeStr);
            if (postType == null) {
                log.error("未知的post_type: {}", postTypeStr);
                return null;
            }

            switch (postType) {
                case MESSAGE:
                    return parseMessageEvent(jsonObject);
                case NOTICE:
                    return JSON.to(NoticeEventBody.class, jsonObject);
                case REQUEST:
                    return JSON.to(RequestEventBody.class, jsonObject);
                case META_EVENT:
                    return JSON.to(MetaEventBody.class, jsonObject);
                default:
                    log.error("不支持的事件类型: {}", postType);
                    return null;
            }
        } catch (JSONException e) {
            log.error("JSON解析异常: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("事件解析异常: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 解析消息事件
     *
     * @param jsonObject JSON对象
     * @return 消息事件对象
     */
    private static BaseMessageEvent parseMessageEvent(JSONObject jsonObject) {
        String messageType = jsonObject.getString("message_type");
        if (messageType == null) {
            log.error("消息事件缺少message_type字段: {}", jsonObject);
            return null;
        }

        String jsonStr = jsonObject.toString();

        if ("private".equals(messageType)) {
            return JSON.parseObject(jsonStr, PrivateMessageEvent.class);
        } else if ("group".equals(messageType)) {
            return JSON.parseObject(jsonStr, GroupMessageEvent.class);
        } else {
            log.error("未知的消息类型: {}", messageType);
            return null;
        }
    }

    /**
     * 创建响应消息
     *
     * @param eventId 事件ID
     * @param data    响应数据
     * @return 响应JSON字符串
     */
    public static String createResponse(String eventId, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("retcode", 0);
        response.put("data", data);
        response.put("echo", eventId);

        return JSON.toJSONString(response);
    }

    /**
     * 从事件JSON字符串创建响应消息
     *
     * @param eventJson 事件JSON字符串
     * @param data      响应数据
     * @return 响应JSON字符串
     */
    public static String createResponseFromEvent(String eventJson, Object data) {
        try {
            JSONObject jsonObject = JSON.parseObject(eventJson);
            if (jsonObject == null) {
                return createResponse("unknown", data);
            }

            // 尝试获取事件ID
            String eventId = jsonObject.getString("message_id");
            if (eventId == null) {
                eventId = jsonObject.getString("time");
            }

            return createResponse(eventId != null ? eventId : "unknown", data);
        } catch (Exception e) {
            return createResponse("unknown", data);
        }
    }

    /**
     * 安全地将事件接口转换为私聊消息事件
     *
     * @param event 事件接口
     * @return 包含私聊消息事件的Optional
     */
    public static Optional<PrivateMessageEvent> asPrivateMessageEvent(EventBody event) {
        if (event instanceof PrivateMessageEvent) {
            return Optional.of((PrivateMessageEvent) event);
        }
        return Optional.empty();
    }

    /**
     * 安全地将事件接口转换为群聊消息事件
     *
     * @param event 事件接口
     * @return 包含群聊消息事件的Optional
     */
    public static Optional<GroupMessageEvent> asGroupMessageEvent(EventBody event) {
        if (event instanceof GroupMessageEvent) {
            return Optional.of((GroupMessageEvent) event);
        }
        return Optional.empty();
    }

    /**
     * 安全地将事件接口转换为任意消息事件基类
     *
     * @param event 事件接口
     * @return 包含消息事件基类的Optional
     */
    public static Optional<BaseMessageEvent> asMessageEvent(EventBody event) {
        if (event instanceof BaseMessageEvent) {
            return Optional.of((BaseMessageEvent) event);
        }
        return Optional.empty();
    }

    /**
     * 安全地将事件接口转换为通知事件
     *
     * @param event 事件接口
     * @return 包含通知事件的Optional
     */
    public static Optional<NoticeEventBody> asNoticeEvent(EventBody event) {
        if (event instanceof NoticeEventBody) {
            return Optional.of((NoticeEventBody) event);
        }
        return Optional.empty();
    }

    /**
     * 安全地将事件接口转换为请求事件
     *
     * @param event 事件接口
     * @return 包含请求事件的Optional
     */
    public static Optional<RequestEventBody> asRequestEvent(EventBody event) {
        if (event instanceof RequestEventBody) {
            return Optional.of((RequestEventBody) event);
        }
        return Optional.empty();
    }

    /**
     * 安全地将事件接口转换为元事件
     *
     * @param event 事件接口
     * @return 包含元事件的Optional
     */
    public static Optional<MetaEventBody> asMetaEvent(EventBody event) {
        if (event instanceof MetaEventBody) {
            return Optional.of((MetaEventBody) event);
        }
        return Optional.empty();
    }

    /**
     * 安全地将事件接口转换为指定类型
     *
     * @param event 事件接口
     * @param clazz 目标类型的Class对象
     * @param <T>   目标类型
     * @return 包含目标类型对象的Optional
     */
    @SuppressWarnings("unchecked")
    public static <T extends EventBody> Optional<T> as(EventBody event, Class<T> clazz) {
        if (clazz.isInstance(event)) {
            return Optional.of((T) event);
        }
        return Optional.empty();
    }

    /**
     * 尝试将事件接口对象转换为指定类型，如果转换失败则返回null
     *
     * @param event 事件接口
     * @param clazz 目标类型的Class对象
     * @param <T>   目标类型
     * @return 转换后的对象或null
     */
    @SuppressWarnings("unchecked")
    public static <T extends EventBody> T cast(EventBody event, Class<T> clazz) {
        return clazz.isInstance(event) ? (T) event : null;
    }
} 