package com.sosorin.ranabot.plugin.example;

import cn.hutool.core.util.ObjectUtil;
import com.sosorin.ranabot.annotation.RanaPlugin;
import com.sosorin.ranabot.entity.bot.IBot;
import com.sosorin.ranabot.entity.event.NoticeEventBody;
import com.sosorin.ranabot.entity.message.Message;
import com.sosorin.ranabot.entity.message.MessageFactory;
import com.sosorin.ranabot.exception.WebSocketRequestException;
import com.sosorin.ranabot.model.EventBody;
import com.sosorin.ranabot.model.PluginResult;
import com.sosorin.ranabot.plugin.AbstractPlugin;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * 戳一戳回复插件
 * 当机器人被戳一戳时，会随机回复一条消息
 *
 * @author rana-bot
 * @since 2025/6/27
 */
@Slf4j
@RanaPlugin("TapReply")
public class TapReplyPlugin extends AbstractPlugin {

    private static final String[] REPLIES = {
            "喵...被拍到了...",
            "抹茶巴菲...分你一半？",
            "好麻烦...别戳我...",
            "唔...想睡觉...",
            "吉他？...现在不想弹...",
            "嗯？...有事？...",
            "走了...去屋顶晒太阳...",
            "喵呜~...（翻个身）",
            "抹茶...不够甜...",
            "人类的规则...好复杂...",
            "嗯...随便吧...",
            "这个和弦...刚刚想到的...",
            "累了...（缩成一团）",
            "喵...？...（装没听见）",
            "创作...不需要理由...",
            "抹茶巴菲...融化了...（盯）"
    };

    private final Random random = new Random();

    public TapReplyPlugin() {
        super("戳一戳回复插件", "1.0.0", "rana-bot");
    }

    @Override
    public PluginResult handleEvent(IBot bot, EventBody eventBody) {
        if (eventBody instanceof NoticeEventBody) {
            NoticeEventBody event = (NoticeEventBody) eventBody;
            String noticeType = event.getNoticeType();
            String subType = event.getSubType();
            Long selfId = event.getSelfId();
            Long targetId = event.getTargetId();
            Long groupId = event.getGroupId();

            // 检查是否是戳一戳事件，并且目标是机器人自己
            if ("notify".equals(noticeType) && "poke".equals(subType) && ObjectUtil.isNotNull(groupId) && selfId.equals(targetId)) {
                try {
                    // 随机选择一条回复
                    String replyText = REPLIES[random.nextInt(REPLIES.length)];
                    Message atMessage = MessageFactory.createAtMessage(event.getUserId().toString());
                    Message message = MessageFactory.createTextMessage(" " + replyText);
                    
                    // 发送消息
                    Long messageId = bot.sendGroupMessage(groupId.toString(), atMessage, message);
                    log.info("戳一戳回复成功，消息ID: {}", messageId);
                    
                    return PluginResult.RETURN("戳一戳回复成功");
                } catch (WebSocketRequestException e) {
                    log.error("戳一戳回复失败", e);
                    return PluginResult.FAIL("戳一戳回复失败: " + e.getMessage());
                }
            }
        }
        return PluginResult.CONTINUE();
    }

    @Override
    public boolean canHandle(EventBody eventBody) {
        if (eventBody instanceof NoticeEventBody) {
            NoticeEventBody event = (NoticeEventBody) eventBody;
            String noticeType = event.getNoticeType();
            String subType = event.getSubType();
            return "notify".equals(noticeType) && "poke".equals(subType);
        }
        return false;
    }
}
