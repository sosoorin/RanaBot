package com.sosorin.ranabot.plugin.example;

import cn.hutool.core.util.StrUtil;
import com.sosorin.ranabot.annotation.RanaPlugin;
import com.sosorin.ranabot.entity.event.message.BaseMessageEvent;
import com.sosorin.ranabot.entity.event.message.GroupMessageEvent;
import com.sosorin.ranabot.entity.message.Message;
import com.sosorin.ranabot.model.EventBody;
import com.sosorin.ranabot.plugin.AbstractPlugin;
import com.sosorin.ranabot.plugin.Plugin;
import com.sosorin.ranabot.plugin.PluginManager;
import com.sosorin.ranabot.service.IWebSocketService;
import com.sosorin.ranabot.util.EventParseUtil;
import com.sosorin.ranabot.util.MessageUtil;
import com.sosorin.ranabot.util.SendEntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

/**
 * @author rana-bot
 * @since 2025/6/27  14:52
 */
@RanaPlugin("插件设置")
@Slf4j
public class PluginSetting extends AbstractPlugin {

    @Autowired
    private PluginManager pluginManager;
    @Autowired
    private IWebSocketService webSocketService;

    private static final Long SUPER_USER_ID = 0L;

    public PluginSetting() {
        super("插件设置", "1.0.0", "rana-bot");
    }

    @Override
    public void onEnable() {
        log.info("插件 [{}] 已启用，当前超级用户QQ号是{}", name, SUPER_USER_ID);
        super.onEnable();
    }

    /**
     * 处理事件
     *
     * @param eventBody 事件体
     * @return 处理结果，如果不需要处理则返回null
     */
    @Override
    public String handleEvent(EventBody eventBody) {
        Optional<BaseMessageEvent> messageEvent = EventParseUtil.asMessageEvent(eventBody);
        if (messageEvent.isPresent()) {
            BaseMessageEvent event = messageEvent.get();
            if (event.getUserId().equals(SUPER_USER_ID)) {
                List<Message> messages = event.getMessage();
                String text = MessageUtil.extractTextContent(messages);
                String resText = "";
                if (text.startsWith("/启用插件")) {
                    String pluginName = text.split(" ")[1];
                    Plugin plugin = pluginManager.getEnabledPlugin(pluginName);
                    if (plugin != null) {
                        resText = String.format("已启用插件[%s]", pluginName);
                    } else if (pluginManager.registerPlugin(pluginName)) {
                        resText = String.format("已启用插件[%s]", pluginName);
                    } else {
                        resText = String.format("启用插件[%s]失败！", pluginName);
                    }
                }
                if (text.startsWith("/禁用插件")) {
                    String pluginName = text.split(" ")[1];
                    Plugin plugin = pluginManager.getEnabledPlugin(pluginName);
                    if (plugin == null) {
                        resText = String.format("插件[%s]不存在或未启用！", pluginName);
                    } else if (pluginManager.unregisterPlugin(pluginName)) {
                        resText = String.format("已禁用插件[%s]", pluginName);
                    } else {
                        resText = String.format("禁用插件[%s]失败！", pluginName);
                    }
                }
                if (StrUtil.isNotEmpty(resText)) {
                    Message replyMessage = MessageUtil.createReplyMessage(event.getMessageId().toString());
                    Message textMessage = MessageUtil.createTextMessage(resText);
                    List<Message> resMessages = List.of(replyMessage, textMessage);
                    switch (event.getMessageType()) {
                        case "private":
                            webSocketService.send(SendEntityUtil.buildSendPrivateMessageStr(event.getUserId().toString(),
                                    resMessages));
                            break;
                        case "group":
                            webSocketService.send(SendEntityUtil.buildSendGroupMessageStr(((GroupMessageEvent) event).getGroupId().toString(),
                                    resMessages));
                            break;
                    }
                }
                return resText;
            }
            // 示例：处理消息事件
            return "已处理消息：" + event.getMessage();
        }

        return null;
    }


    @Override
    public boolean canHandle(EventBody eventBody) {
        return eventBody instanceof BaseMessageEvent;
    }
}
