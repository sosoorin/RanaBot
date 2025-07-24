package com.sosorin.ranabot.plugin.example;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson2.JSON;
import com.sosorin.ranabot.annotation.RanaPlugin;
import com.sosorin.ranabot.entity.bot.IBot;
import com.sosorin.ranabot.entity.event.message.BaseMessageEvent;
import com.sosorin.ranabot.entity.event.message.GroupMessageEvent;
import com.sosorin.ranabot.entity.message.Message;
import com.sosorin.ranabot.entity.message.MessageFactory;
import com.sosorin.ranabot.exception.WebSocketRequestException;
import com.sosorin.ranabot.model.EventBody;
import com.sosorin.ranabot.model.PluginResult;
import com.sosorin.ranabot.plugin.AbstractPlugin;
import com.sosorin.ranabot.plugin.Plugin;
import com.sosorin.ranabot.plugin.PluginManager;
import com.sosorin.ranabot.util.EventParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 插件设置插件
 * 可以控制其他插件的状态，包括获取插件列表、禁用/启用插件、获取/设置插件参数等
 *
 * @author rana-bot
 * @since 2025/6/28
 */
@Slf4j
@RanaPlugin("插件设置")
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

    @Override
    public void onDisable() {
        super.onDisable();
        log.info("插件设置插件已禁用");
    }

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


    @Override
    public PluginResult handleEvent(IBot bot, EventBody eventBody) {
        // 尝试将事件转换为消息事件
        Optional<BaseMessageEvent> messageEvent = EventParseUtil.asMessageEvent(eventBody);

        if (messageEvent.isPresent()) {
            BaseMessageEvent event = messageEvent.get();
            Long userId = event.getUserId();

            // 只允许超级用户操作
            if (!userId.equals(SUPER_USER_ID.get())) {
                log.info("非超级用户 {} 尝试使用插件设置，已拒绝", userId);
                return PluginResult.CONTINUE();
            }

            String rawMessage = event.getRawMessage();
            if (rawMessage.startsWith("/插件")) {
                try {
                    return handlePluginCommand(bot, event, rawMessage);
                } catch (Exception e) {
                    log.error("处理插件命令失败: {}", e.getMessage());
                    try {
                        Message errorMessage = MessageFactory.createTextMessage("处理命令失败: " + e.getMessage());
                        if (event instanceof GroupMessageEvent) {
                            bot.sendGroupMessage(((GroupMessageEvent) event).getGroupId().toString(), errorMessage);
                        } else {
                            bot.sendPrivateMessage(event.getUserId().toString(), errorMessage);
                        }
                    } catch (WebSocketRequestException sendEx) {
                        log.error("发送错误消息失败", sendEx);
                    }
                    return PluginResult.FAIL("处理命令失败: " + e.getMessage());
                }
            }
        }

        return PluginResult.CONTINUE();
    }

    @Override
    public int getOrder(Plugin plugin) {
        return Integer.MIN_VALUE;
    }

    private PluginResult handlePluginCommand(IBot bot, BaseMessageEvent event, String rawMessage) throws WebSocketRequestException {
        String[] commands = rawMessage.substring(3).split(" ");
        // 构建响应消息
        Message resMessage = MessageFactory.createReplyMessage(event.getMessageId().toString());
        Message resMessageText = switch (commands[0]) {
            case "列表" -> MessageFactory.createTextMessage(buildPluginList());
            case "启用" -> MessageFactory.createTextMessage(enablePluginByName(commands.length > 1 ? commands[1] : ""));
            case "禁用" -> MessageFactory.createTextMessage(disablePluginByName(commands.length > 1 ? commands[1] : ""));
            default -> MessageFactory.createTextMessage(String.format("不支持的插件命令: %s", rawMessage));
        };
        // 根据消息来源选择发送方式
        String messageType = event.getMessageType();
        Long messageId = null;
        switch (messageType) {
            case "private":
                messageId = bot.sendPrivateMessage(event.getUserId().toString(), resMessage, resMessageText);
                break;
            case "group":
                messageId = bot.sendGroupMessage(((GroupMessageEvent) event).getGroupId().toString(), resMessage, resMessageText);
                break;
            default:
                log.error("不支持的消息类型: {}", messageType);
                break;
        }

        log.info("发送响应成功，消息ID: {}", messageId);
        return PluginResult.RETURN("已处理插件命令");
    }

    @Override
    public boolean canHandle(EventBody eventBody) {
        // 只处理消息事件
        if (!(eventBody instanceof BaseMessageEvent)) {
            return false;
        }

        BaseMessageEvent event = (BaseMessageEvent) eventBody;
        String rawMessage = event.getRawMessage();
        return rawMessage != null && rawMessage.startsWith("/插件");
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

    @NotNull
    private String enablePluginByName(String pluginName) {
        String resText;
        if (pluginName == null || pluginName.isEmpty()) {
            return "请指定要启用的插件名称！";
        }
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


    @NotNull
    private String disablePluginByName(String pluginName) {
        String resText;
        if (pluginName == null || pluginName.isEmpty()) {
            return "请指定要禁用的插件名称！";
        }
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
}
