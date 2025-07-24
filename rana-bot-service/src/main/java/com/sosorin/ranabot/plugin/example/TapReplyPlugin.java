package com.sosorin.ranabot.plugin.example;

import cn.hutool.core.util.ObjectUtil;
import com.sosorin.ranabot.annotation.RanaPlugin;
import com.sosorin.ranabot.entity.event.NoticeEventBody;
import com.sosorin.ranabot.entity.message.Message;
import com.sosorin.ranabot.model.EventBody;
import com.sosorin.ranabot.model.PluginResult;
import com.sosorin.ranabot.plugin.AbstractPlugin;
import com.sosorin.ranabot.bot.IBot;
import com.sosorin.ranabot.util.EventParseUtil;
import com.sosorin.ranabot.util.MessageUtil;
import com.sosorin.ranabot.util.SendEntityUtil;

import java.util.List;
import java.util.Optional;

/**
 * @author rana-bot
 * @since 2025/6/27  13:54
 */
@RanaPlugin("TapReply")
public class TapReplyPlugin extends AbstractPlugin {

    public TapReplyPlugin() {
        super("回复拍一拍消息", "1.0.0", "rana-bot");
    }
    /**
     * 处理事件
     *
     * @param eventBody 事件体
     * @return 处理结果，如果不需要处理则返回null
     */
    @Override
    public PluginResult handleEvent(IBot bot, EventBody eventBody) {
        Optional<NoticeEventBody> noticeEvent = EventParseUtil.asNoticeEvent(eventBody);
        if (noticeEvent.isPresent()) {
            NoticeEventBody event = noticeEvent.get();
            String noticeType = event.getNoticeType();
            String subType = event.getSubType();
            Long groupId = event.getGroupId();
            Long selfId = event.getSelfId();
            Long targetId = event.getTargetId();
            if ("notify".equals(noticeType) && "poke".equals(subType) && ObjectUtil.isNotNull(groupId) && selfId.equals(targetId)) {
                Message atMessage = MessageUtil.createAtMessage(event.getUserId().toString());
                Message textMessage = MessageUtil.createTextMessage(" 请我吃抹茶芭菲！");
                List<Message> messages = List.of(atMessage, textMessage);
                String msgStr = SendEntityUtil.buildSendGroupMessageStr(groupId.toString(), messages);
                bot.sendRawMessageStr(msgStr);
                return PluginResult.RETURN("请我吃抹茶芭菲！");
            }
        }
        return PluginResult.CONTINUE();
    }

    @Override
    public boolean canHandle(EventBody eventBody) {
        return eventBody instanceof NoticeEventBody;
    }
}
