package com.sosorin.ranabot.plugin.example;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.sosorin.ranabot.annotation.RanaPlugin;
import com.sosorin.ranabot.entity.event.message.BaseMessageEvent;
import com.sosorin.ranabot.entity.event.message.GroupMessageEvent;
import com.sosorin.ranabot.entity.event.message.PrivateMessageEvent;
import com.sosorin.ranabot.entity.message.Message;
import com.sosorin.ranabot.model.EventBody;
import com.sosorin.ranabot.model.PluginResult;
import com.sosorin.ranabot.plugin.AbstractPlugin;
import com.sosorin.ranabot.entity.bot.IBot;
import com.sosorin.ranabot.util.EventParseUtil;
import com.sosorin.ranabot.util.MessageUtil;
import com.sosorin.ranabot.util.SendEntityUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author rana-bot
 * @since 2025/6/27  16:29
 */
@RanaPlugin("谐音梗生成器")
@Slf4j
public class ColdLaughPlugin extends AbstractPlugin {

    private final Map<String, Object> PARAMS = new ConcurrentHashMap<>();

    public ColdLaughPlugin() {
        super("一个简单的谐音梗插件", "1.0.0", "rana-bot");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        // 这里只是示例，实际开发中可以从文件中读取关键词，并实现动态替换
        List<String> keywords = List.of("抹茶芭菲~", "星巴克", "有趣的女人", "米村拌饭");
        PARAMS.put("keywords", keywords);
        log.info("谐音梗插件已启用，将会回复所有收到的包含 [{}] 的消息", keywords);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        log.info("谐音梗插件已禁用");
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

        // 如果是消息事件，则返回对应冷笑话
        if (messageEvent.isPresent()) {
            BaseMessageEvent event = messageEvent.get();
            List<Message> messages = event.getMessage();

            List<String> keywordsList = new ArrayList<>();
            Object keywords = PARAMS.get("keywords");
            if (ObjectUtil.isNotNull(keywords) && keywords instanceof List) {
                keywordsList = (List<String>) keywords;
            }
            String recText = MessageUtil.extractTextContent(messages);
            String recFirstLetter = PinyinUtil.getFirstLetter(recText, "");
            Map<String, List<String>> keywordsMap = new HashMap<>();
            keywordsList.stream().forEach(keyword -> {
                String firstLetter = PinyinUtil.getFirstLetter(keyword, "");
                // 去掉除了大小写英文字母外的其他字符
                firstLetter = firstLetter.replaceAll("[^a-zA-Z]", "");
                if (keywordsMap.containsKey(firstLetter)) {
                    keywordsMap.get(firstLetter).add(keyword);
                } else {
                    keywordsMap.put(firstLetter, new ArrayList<>(Collections.singletonList(keyword)));
                }
            });
            List<Message> coldMsg = new ArrayList<>();
            keywordsMap.forEach((k, v) -> {
                if (recFirstLetter.contains(k)) {
                    v.forEach(keyword -> {
                        // 如果和原消息相同，则不回复
                        if (!keyword.equals(recText)) {
                            coldMsg.add(MessageUtil.createTextMessage(keyword));
                        }
                    });
                }
            });
            if (!coldMsg.isEmpty()) {
                Message replyTextMessage = MessageUtil.createReplyMessage(event.getMessageId().toString());
                // 从coldMsg的list中随机取一条消息
                Message simpleMessage = coldMsg.get(new Random().nextInt(coldMsg.size()));
                List<Message> replyMessages = List.of(replyTextMessage, simpleMessage);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // 创建回复消息
                if (event instanceof PrivateMessageEvent) {
                    bot.sendPrivateMessage(event.getUserId().toString(), replyMessages);
                } else if (event instanceof GroupMessageEvent) {
                    bot.sendGroupMessage(((GroupMessageEvent) event).getGroupId().toString(), replyMessages);
                }
                return PluginResult.RETURN();
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
