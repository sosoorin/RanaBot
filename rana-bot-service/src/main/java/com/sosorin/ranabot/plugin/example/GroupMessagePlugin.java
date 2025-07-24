package com.sosorin.ranabot.plugin.example;

import cn.hutool.core.util.StrUtil;
import com.sosorin.ranabot.annotation.RanaPlugin;
import com.sosorin.ranabot.entity.bot.IBot;
import com.sosorin.ranabot.entity.event.message.GroupMessageEvent;
import com.sosorin.ranabot.entity.message.Message;
import com.sosorin.ranabot.entity.message.MessageFactory;
import com.sosorin.ranabot.model.EventBody;
import com.sosorin.ranabot.model.PluginResult;
import com.sosorin.ranabot.plugin.AbstractPlugin;
import com.sosorin.ranabot.util.EventParseUtil;
import com.sosorin.ranabot.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * 群消息处理插件
 * 处理来自QQ群的消息
 * 
 * @author rana-bot
 * @since 2025/6/28
 */
@Slf4j
@RanaPlugin(value = "groupMessagePlugin")
public class GroupMessagePlugin extends AbstractPlugin {

    public GroupMessagePlugin() {
        super("群消息处理插件", "1.0.0", "rana-bot");
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒");

    @Override
    public PluginResult handleEvent(IBot bot, EventBody eventBody) {
        // 尝试将事件转换为群消息事件
        Optional<GroupMessageEvent> groupEvent = EventParseUtil.asGroupMessageEvent(eventBody);
        
        if (groupEvent.isPresent()) {
            GroupMessageEvent event = groupEvent.get();
            
            // 获取群聊ID和发送者ID
            Long groupId = event.getGroupId();
            Long userId = event.getUserId();
            
            // 获取消息内容
            List<Message> messages = event.getMessage();

            log.info("收到群 {} 中用户 {} 的消息: {}", groupId, userId, messages);

            String text = "";
            // 根据关键词回复
            if (MessageUtil.containsKeyword(messages, "你好") || MessageUtil.containsKeyword(messages, "hello")) {
                text = "我是个机器人喵，很高兴为您服务喵！";

            } else if (MessageUtil.containsKeyword(messages, "时间") || MessageUtil.containsKeyword(messages, "几点")) {
                LocalDateTime now = LocalDateTime.now();
                // 转化为 xxxx年xx月xx日 xx时xx分xx秒
                String time = now.format(DATE_TIME_FORMATTER);
                text = "现在的时间是: " + time;
            } else if (MessageUtil.containsKeyword(messages, "help")) {
                text = "我可以回答以下问题：\n" +
                        "1. 问候（你好/hello）\n" +
                        "2. 时间（时间/几点）\n" +
                        "3. 帮助（帮助/help）";
            }
            if (StrUtil.isNotBlank(text)) {
                Message replyMessage = MessageFactory.createReplyMessage(event.getMessageId().toString());
                Long messageId = bot.sendGroupMessage(groupId.toString(), replyMessage, MessageUtil.createTextMessage(text));
                return PluginResult.SUCCESS(String.format("已回复群消息，消息ID: %s", messageId));
            }
        }
        
        return PluginResult.CONTINUE();
    }

    @Override
    public boolean canHandle(EventBody eventBody) {
        return eventBody instanceof GroupMessageEvent;
    }
} 