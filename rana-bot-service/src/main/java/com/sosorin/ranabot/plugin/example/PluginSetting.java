package com.sosorin.ranabot.plugin.example;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson2.JSON;
import com.sosorin.ranabot.entity.bot.IBot;
import com.sosorin.ranabot.annotation.RanaPlugin;
import com.sosorin.ranabot.entity.event.message.BaseMessageEvent;
import com.sosorin.ranabot.entity.event.message.GroupMessageEvent;
import com.sosorin.ranabot.entity.message.Message;
import com.sosorin.ranabot.model.EventBody;
import com.sosorin.ranabot.model.PluginResult;
import com.sosorin.ranabot.plugin.AbstractPlugin;
import com.sosorin.ranabot.plugin.Plugin;
import com.sosorin.ranabot.plugin.PluginManager;
import com.sosorin.ranabot.util.EventParseUtil;
import com.sosorin.ranabot.util.MessageUtil;
import com.sosorin.ranabot.util.SendEntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author rana-bot
 * @since 2025/6/27  14:52
 */
@RanaPlugin("插件设置")
@Slf4j
public class PluginSetting extends AbstractPlugin {

    @Autowired
    private PluginManager pluginManager;

    private static final AtomicLong SUPER_USER_ID = new AtomicLong(0);

    @Value("${plugin-config.super-user-id:0}")
    public void setSuperUserId(Long superUserId) {
        SUPER_USER_ID.set(superUserId);
    }

    public PluginSetting() {
        super("插件设置", "1.0.0", "rana-bot");
    }

    @Override
    public void onEnable() {
        log.info("插件 [{}] 已启用，当前超级用户QQ号是{}", name, SUPER_USER_ID);
        super.onEnable();
    }

    /**
     * 设置插件参数
     *
     * @param params 插件参数
     * @return 是否设置成功
     */
    @Override
    public boolean setParams(Map<String, Object> params) {
        Object o = params.get("superUserId");
        if (NumberUtil.isNumber(o.toString())) {
            SUPER_USER_ID.set(Long.parseLong(o.toString()));
        }
        return super.setParams(params);
    }

    /**
     * 获取插件参数
     *
     * @return 插件参数
     */
    @Override
    public Map<String, Object> getParams() {
        return Map.of("superUserId", SUPER_USER_ID.get());
    }

    /**
     * 处理事件
     *
     * @param eventBody 事件体
     * @return 处理结果，如果不需要处理则返回null
     */
    @Override
    public PluginResult handleEvent(IBot bot, EventBody eventBody) {
        Optional<BaseMessageEvent> messageEvent = EventParseUtil.asMessageEvent(eventBody);
        if (messageEvent.isPresent()) {
            BaseMessageEvent event = messageEvent.get();
            if (event.getUserId().equals(SUPER_USER_ID.get())) {
                List<Message> messages = event.getMessage();
                String text = MessageUtil.extractTextContent(messages);
                String resText = "";
                if (text.startsWith("/启用插件")) {
                    resText = enablePluginByName(text);
                }
                if (text.startsWith("/禁用插件")) {
                    resText = disablePluginByName(text);
                }
                if (text.startsWith("/插件列表")) {
                    resText = buildPluginList();
                }
                if (text.startsWith("/系统信息")) {
                    resText = SystemUtil.getOsInfo().toString() + "\n"
                            + SystemUtil.getRuntimeInfo().toString() + "\n"
                    ;
                }
                if (StrUtil.isNotEmpty(resText)) {
                    sendRes(bot, event, resText);
                    return PluginResult.RETURN(resText);
                }
            }
            // 示例：处理消息事件
            return PluginResult.CONTINUE("已处理消息：" + event.getMessage());
        }

        return PluginResult.CONTINUE();
    }

    @NotNull
    private String buildPluginList() {
        StringBuffer sb = new StringBuffer();
        pluginManager.getAllPlugins().stream().sorted(Plugin::getOrder).forEach(plugin -> {
            sb.append("插件名称: ").append(plugin.getName()).append("\n")
                    .append("插件描述: ").append(plugin.getDescription()).append("\n")
                    .append("插件版本: ").append(plugin.getVersion()).append("\n")
                    .append("插件作者: ").append(plugin.getAuthor()).append("\n")
                    .append("插件参数: ").append(JSON.toJSON(plugin.getParams())).append("\n")
                    .append("插件状态: ").append(plugin.isEnabled() ? "启用" : "禁用").append("\n")
                    .append("插件顺序: ").append(plugin.getOrder(plugin)).append("\n")
                    .append("---------------------------------------------------\n");
        });
        return sb.toString();
    }

    private void sendRes(IBot bot, BaseMessageEvent event, String resText) {
        Message replyMessage = MessageUtil.createReplyMessage(event.getMessageId().toString());
        Message textMessage = MessageUtil.createTextMessage(resText);
        List<Message> resMessages = List.of(replyMessage, textMessage);
        switch (event.getMessageType()) {
            case "private":
                bot.sendRawMessageStr(SendEntityUtil.buildSendPrivateMessageStr(event.getUserId().toString(),
                        resMessages));
                break;
            case "group":
                bot.sendRawMessageStr(SendEntityUtil.buildSendGroupMessageStr(((GroupMessageEvent) event).getGroupId().toString(),
                        resMessages));
                break;
        }
    }

    @NotNull
    private String disablePluginByName(String text) {
        String resText;
        String pluginName = text.split(" ")[1];
        Plugin plugin = pluginManager.getEnabledPlugin(pluginName);
        if (plugin == null) {
            resText = String.format("插件[%s]不存在或未启用！", pluginName);
        } else if (pluginManager.unregisterPlugin(pluginName)) {
            resText = String.format("已禁用插件[%s]", pluginName);
        } else {
            resText = String.format("禁用插件[%s]失败！", pluginName);
        }
        return resText;
    }

    @NotNull
    private String enablePluginByName(String text) {
        String resText;
        String pluginName = text.split(" ")[1];
        Plugin plugin = pluginManager.getEnabledPlugin(pluginName);
        if (plugin != null) {
            resText = String.format("已启用插件[%s]", pluginName);
        } else if (pluginManager.registerPluginByName(pluginName)) {
            resText = String.format("已启用插件[%s]", pluginName);
        } else {
            resText = String.format("启用插件[%s]失败！", pluginName);
        }
        return resText;
    }


    @Override
    public boolean canHandle(EventBody eventBody) {
        return eventBody instanceof BaseMessageEvent;
    }

    @Override
    public int getOrder(Plugin plugin) {
        // 保证插件优先级最高
        return Integer.MIN_VALUE;
    }
}
