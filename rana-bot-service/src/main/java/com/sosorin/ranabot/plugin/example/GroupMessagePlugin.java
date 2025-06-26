package com.sosorin.ranabot.plugin.example;

import cn.hutool.core.util.StrUtil;
import com.sosorin.ranabot.annotation.RanaPlugin;
import com.sosorin.ranabot.entity.event.message.GroupMessageEvent;
import com.sosorin.ranabot.entity.message.Message;
import com.sosorin.ranabot.model.EventBody;
import com.sosorin.ranabot.plugin.AbstractPlugin;
import com.sosorin.ranabot.service.IWebSocketService;
import com.sosorin.ranabot.util.EventParseUtil;
import com.sosorin.ranabot.util.MessageUtil;
import com.sosorin.ranabot.util.SendEntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 示例插件：群聊消息处理插件
 * 当接收到群聊消息事件时，根据关键词进行回复
 *
 * @author rana-bot
 * @since 2025/6/28
 */
@Slf4j
@RanaPlugin(value = "groupMessagePlugin")
public class GroupMessagePlugin extends AbstractPlugin {
    @Autowired
    private IWebSocketService webSocketService;

    public GroupMessagePlugin() {
        super("一个处理群聊消息的插件", "1.0.0", "rana-bot");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        log.info("插件 [{}] 已启用，将会处理群聊中的关键词", name);
    }

    @Override
    public String handleEvent(EventBody eventBody) {
        // 尝试将事件转换为群聊消息事件
        Optional<GroupMessageEvent> groupEvent = EventParseUtil.asGroupMessageEvent(eventBody);

        if (groupEvent.isPresent()) {
            GroupMessageEvent event = groupEvent.get();
            List<Message> messages = event.getMessage();
            MessageUtil.containsKeyword(messages, "关键词");
            Long groupId = event.getGroupId();
            Long userId = event.getUserId();
            Long messageId = event.getMessageId();
            Message replyMsg = MessageUtil.createReplyMessage(messageId.toString());

            log.info("收到群 {} 中用户 {} 的消息: {}", groupId, userId, messages);
            String text = "";
            // 根据关键词回复
            if (MessageUtil.containsKeyword(messages, "你好") || MessageUtil.containsKeyword(messages, "hello")) {
                text = "哔~卟~我是个机器人，很高兴为您服务！";

            } else if (MessageUtil.containsKeyword(messages, "时间") || MessageUtil.containsKeyword(messages, "几点")) {
                LocalDateTime now = LocalDateTime.now();
                // 转化为 xxxx年xx月xx日 xx时xx分xx秒
                String time = now.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒"));
                text = "现在的时间是: " + time;
            } else if (MessageUtil.containsKeyword(messages, "help")) {
                text = "我可以回答以下问题：\n" +
                        "1. 问候（你好/hello）\n" +
                        "2. 时间（时间/几点）\n" +
                        "3. 帮助（帮助/help）";
            }
            if (StrUtil.isNotBlank(text)) {
                String eventStr = SendEntityUtil.buildSendGroupMessageStr(groupId.toString(),
                        List.of(replyMsg,
                                MessageUtil.createTextMessage(text)));
                webSocketService.send(eventStr);
            }
            return text;
        }

        // 如果不满足任何条件，则返回null表示不处理
        return null;
    }

    @Override
    public boolean canHandle(EventBody eventBody) {
        // 只处理群聊消息事件
        return eventBody instanceof GroupMessageEvent;
    }
} 