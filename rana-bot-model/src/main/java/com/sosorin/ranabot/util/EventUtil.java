package com.sosorin.ranabot.util;

import com.sosorin.ranabot.entity.event.MetaEventBody;
import com.sosorin.ranabot.entity.event.NoticeEventBody;
import com.sosorin.ranabot.entity.event.RequestEventBody;
import com.sosorin.ranabot.entity.event.message.BaseMessageEvent;
import com.sosorin.ranabot.entity.event.message.GroupMessageEvent;
import com.sosorin.ranabot.entity.event.message.PrivateMessageEvent;
import com.sosorin.ranabot.model.EventBody;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;

/**
 * 事件工具类
 * 提供各类事件处理的工具方法和示例
 */
@Slf4j
public class EventUtil {

    /**
     * 根据事件类型分发到不同的处理方法
     *
     * @param eventBody 事件体
     */
    public static void handleEvent(EventBody eventBody) {
        Objects.requireNonNull(eventBody, "事件不能为空");

        // 方式一：使用instanceof判断类型
        if (eventBody instanceof PrivateMessageEvent) {
            handlePrivateMessage((PrivateMessageEvent) eventBody);
        } else if (eventBody instanceof GroupMessageEvent) {
            handleGroupMessage((GroupMessageEvent) eventBody);
        } else if (eventBody instanceof NoticeEventBody) {
            handleNotice((NoticeEventBody) eventBody);
        } else if (eventBody instanceof RequestEventBody) {
            handleRequest((RequestEventBody) eventBody);
        } else {
            log.warn("未知的事件类型: {}", eventBody.getClass().getSimpleName());
        }
    }

    /**
     * 使用工具类方法处理事件（推荐）
     *
     * @param eventBody 事件体
     */
    public static void handleEventSafely(EventBody eventBody) {
        Objects.requireNonNull(eventBody, "事件不能为空");

        // 方式二：使用Optional安全转换
        EventParseUtil.asPrivateMessageEvent(eventBody).ifPresent(EventUtil::handlePrivateMessage);
        EventParseUtil.asGroupMessageEvent(eventBody).ifPresent(EventUtil::handleGroupMessage);
        EventParseUtil.asNoticeEvent(eventBody).ifPresent(EventUtil::handleNotice);
        EventParseUtil.asRequestEvent(eventBody).ifPresent(EventUtil::handleRequest);

        // 或者使用泛型方法
        Optional<PrivateMessageEvent> privateEvent = EventParseUtil.as(eventBody, PrivateMessageEvent.class);
        privateEvent.ifPresent(EventUtil::handlePrivateMessage);
    }

    /**
     * 使用强制转换工具方法处理事件
     *
     * @param eventBody 事件体
     */
    public static void handleEventWithCast(EventBody eventBody) {
        Objects.requireNonNull(eventBody, "事件不能为空");

        // 方式三：使用cast方法直接转换（如果类型不匹配返回null）
        PrivateMessageEvent privateEvent = EventParseUtil.cast(eventBody, PrivateMessageEvent.class);
        if (privateEvent != null) {
            handlePrivateMessage(privateEvent);
            return;
        }

        GroupMessageEvent groupEvent = EventParseUtil.cast(eventBody, GroupMessageEvent.class);
        if (groupEvent != null) {
            handleGroupMessage(groupEvent);
            return;
        }

        NoticeEventBody noticeEvent = EventParseUtil.cast(eventBody, NoticeEventBody.class);
        if (noticeEvent != null) {
            handleNotice(noticeEvent);
            return;
        }

        RequestEventBody requestEvent = EventParseUtil.cast(eventBody, RequestEventBody.class);
        if (requestEvent != null) {
            handleRequest(requestEvent);
        }
    }

    /**
     * 处理事件并根据类型返回不同结果
     *
     * @param eventBody 事件体
     * @return 处理结果
     */
    public static String processEvent(EventBody eventBody) {
        Objects.requireNonNull(eventBody, "事件不能为空");

        // 示例：使用Optional方式处理并返回结果
        Optional<BaseMessageEvent> messageEvent = EventParseUtil.asMessageEvent(eventBody);
        if (messageEvent.isPresent()) {
            BaseMessageEvent event = messageEvent.get();
            return "已处理消息：" + event.getMessage();
        }

        Optional<NoticeEventBody> noticeEvent = EventParseUtil.asNoticeEvent(eventBody);
        if (noticeEvent.isPresent()) {
            NoticeEventBody event = noticeEvent.get();
            return "已处理通知：" + event.getNoticeType();
        }

        Optional<RequestEventBody> requestEvent = EventParseUtil.asRequestEvent(eventBody);
        if (requestEvent.isPresent()) {
            RequestEventBody event = requestEvent.get();
            return "已处理请求：" + event.getRequestType();
        }

        Optional<MetaEventBody> metaEvent = EventParseUtil.asMetaEvent(eventBody);
        if (metaEvent.isPresent()) {
            MetaEventBody event = metaEvent.get();
            return "已处理元事件：" + event.getMetaEventType();
        }

        return "未知事件类型";
    }

    // 具体的事件处理方法

    private static void handlePrivateMessage(PrivateMessageEvent event) {
        log.info("处理私聊消息: {}", event.getMessage());
        // 处理私聊消息的逻辑
    }

    private static void handleGroupMessage(GroupMessageEvent event) {
        log.info("处理群聊消息: {}, 群号: {}", event.getMessage(), event.getGroupId());
        // 处理群聊消息的逻辑
    }

    private static void handleNotice(NoticeEventBody event) {
        log.info("处理通知事件: {}", event.getNoticeType());
        // 处理通知事件的逻辑
    }

    private static void handleRequest(RequestEventBody event) {
        log.info("处理请求事件: {}", event.getRequestType());
        // 处理请求事件的逻辑
    }
}
