package com.sosorin.ranabot.plugin.example;

import cn.hutool.core.util.ObjectUtil;
import com.sosorin.ranabot.annotation.RanaPlugin;
import com.sosorin.ranabot.entity.event.message.BaseMessageEvent;
import com.sosorin.ranabot.entity.event.message.GroupMessageEvent;
import com.sosorin.ranabot.entity.event.message.PrivateMessageEvent;
import com.sosorin.ranabot.entity.message.Message;
import com.sosorin.ranabot.model.EventBody;
import com.sosorin.ranabot.model.PluginResult;
import com.sosorin.ranabot.plugin.AbstractPlugin;
import com.sosorin.bot.IBot;
import com.sosorin.ranabot.util.EventParseUtil;
import com.sosorin.ranabot.util.MessageUtil;
import com.sosorin.ranabot.util.SendEntityUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 示例插件：回声插件
 * 当接收到私聊消息事件时，回复相同的消息内容
 *
 * @author rana-bot
 * @since 2025/6/28
 */
@Slf4j
@RanaPlugin("抹茶芭菲复读机")
public class SimpleEchoPlugin extends AbstractPlugin {

    private final Map<String, Object> PARAMS = new ConcurrentHashMap<>();

    public SimpleEchoPlugin() {
        super("一个简单的回声插件，返回接收到的消息", "1.0.0", "rana-bot");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        // 这里只是示例，实际开发中可以从文件中读取关键词，并实现动态替换
        List<String> keywords = List.of("抹茶芭菲", "复读", "有趣的女人");
        PARAMS.put("keywords", keywords);
        log.info("回声插件已启用，将会回复所有收到的包含 [{}] 的消息", keywords);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        log.info("回声插件已禁用");
    }

    /**
     * 获取插件参数
     *
     * @return 插件参数
     */
    @Override
    public Map<String, Object> getParams() {
        return PARAMS;
    }

    /**
     * 设置插件参数
     *
     * @param params 插件参数
     * @return 是否设置成功
     */
    @Override
    public boolean setParams(Map<String, Object> params) {
        // 这里只是示例，实际开发中可以将关键词写入数据库中，并读取
        PARAMS.putAll(params);
        log.info("[{}]插件参数已设置: {}", name, PARAMS);
        return true;
    }

    @Override
    public PluginResult handleEvent(IBot bot, EventBody eventBody) {
        // 尝试将事件转换为消息事件
        Optional<BaseMessageEvent> messageEvent = EventParseUtil.asMessageEvent(eventBody);

        // 如果是私聊消息事件，则返回相同的消息内容
        if (messageEvent.isPresent()) {
            BaseMessageEvent event = messageEvent.get();
            List<Message> messages = event.getMessage();

            List<String> keywordsList = new ArrayList<>();
            Object keywords = PARAMS.get("keywords");
            if (ObjectUtil.isNotNull(keywords) && keywords instanceof List) {
                keywordsList = (List<String>) keywords;
            }
            if (keywordsList.stream().anyMatch(keyword -> MessageUtil.containsKeyword(messages, keyword))) {
                log.info("收到消息: {}, 将回复相同内容", messages);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // 创建回复消息
                String bodyStr = "";
                if (event instanceof PrivateMessageEvent) {
                    bodyStr = SendEntityUtil.buildSendPrivateMessageStr(event.getUserId().toString(), messages);
                } else if (event instanceof GroupMessageEvent) {
                    bodyStr = SendEntityUtil.buildSendGroupMessageStr(((GroupMessageEvent) event).getGroupId().toString(), messages);
                }
                log.info("发送消息: {}", bodyStr);
                bot.send(bodyStr);
                return PluginResult.RETURN("Echo: " + messages);
            }
        }

        // 如果不是消息事件，则返回null表示不处理
        return PluginResult.CONTINUE();
    }

    @Override
    public boolean canHandle(EventBody eventBody) {
        // 只处理私聊消息事件
        return eventBody instanceof BaseMessageEvent;
    }
}