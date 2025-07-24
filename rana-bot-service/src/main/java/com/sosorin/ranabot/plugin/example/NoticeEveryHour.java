package com.sosorin.ranabot.plugin.example;

import com.sosorin.ranabot.annotation.RanaPlugin;
import com.sosorin.ranabot.entity.bot.IBot;
import com.sosorin.ranabot.entity.event.message.BaseMessageEvent;
import com.sosorin.ranabot.entity.message.Message;
import com.sosorin.ranabot.exception.WebSocketRequestException;
import com.sosorin.ranabot.model.EventBody;
import com.sosorin.ranabot.model.PluginResult;
import com.sosorin.ranabot.plugin.AbstractPlugin;
import com.sosorin.ranabot.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 整点报时插件
 * 每小时整点在指定的群发送报时消息
 *
 * @author rana-bot
 * @since 2025/6/28
 */
@Slf4j
@RanaPlugin("整点报时")
public class NoticeEveryHour extends AbstractPlugin {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒");

    // 需要通知的群号
    private static final CopyOnWriteArraySet<String> GROUP_ID_SET = new CopyOnWriteArraySet<>();

    private static IBot bot;

    public NoticeEveryHour() {
        super("每整点报时插件", "1.0.0", "rana-bot");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        log.info("整点报时插件已启用");
    }

    @Override
    public boolean setParams(Map<String, Object> params) {
        Object set = params.get("groupIdSet");
        if (set != null) {
            GROUP_ID_SET.clear();
            GROUP_ID_SET.addAll((List<String>) set);
        }
        return true;
    }

    @Override
    public Map<String, Object> getParams() {
        return Map.of("groupIdSet", GROUP_ID_SET);
    }

    @Override
    public PluginResult handleEvent(IBot bot, EventBody eventBody) {
        // 保存bot实例
        NoticeEveryHour.bot = bot;
        
        // 此插件不处理事件，只在定时任务中发送消息
        return PluginResult.CONTINUE();
    }

    /**
     * 整点报时
     * 每小时整点执行
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void noticeEveryHour() {
        if (bot == null) {
            log.warn("整点报时：bot实例为null，无法发送消息");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        String text = "现在时间是: " + now.format(FORMATTER);
        log.info("整点报时：{}", text);

        GROUP_ID_SET.forEach(groupId -> {
            try {
                Message message = MessageUtil.createTextMessage(text);
                Long messageId = bot.sendGroupMessage(groupId, message);
                log.info("整点报时消息已发送到群 {}，消息ID: {}", groupId, messageId);
            } catch (WebSocketRequestException e) {
                log.error("整点报时发送消息到群 {} 失败: {}", groupId, e.getMessage());
            }
        });
    }

    @Override
    public boolean canHandle(EventBody eventBody) {
        return eventBody instanceof BaseMessageEvent;
    }
}
