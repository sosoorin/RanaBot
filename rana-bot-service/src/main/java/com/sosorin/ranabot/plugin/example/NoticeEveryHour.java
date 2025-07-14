package com.sosorin.ranabot.plugin.example;

import cn.hutool.core.util.RandomUtil;
import com.sosorin.ranabot.annotation.RanaPlugin;
import com.sosorin.ranabot.model.EventBody;
import com.sosorin.ranabot.model.PluginResult;
import com.sosorin.ranabot.plugin.AbstractPlugin;
import com.sosorin.ranabot.service.IWebSocketService;
import com.sosorin.ranabot.util.MessageUtil;
import com.sosorin.ranabot.util.SendEntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author rana-bot
 * @since 2025/6/27  14:26
 */
@RanaPlugin("整点报时")
@Lazy
public class NoticeEveryHour extends AbstractPlugin {

    private static final Set<String> GROUP_ID_SET = new CopyOnWriteArraySet<>();

    @Autowired
    private IWebSocketService webSocketService;

    public NoticeEveryHour() {
        super("整点报时", "1.0.0", "rana-bot");
    }

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒");

    /**
     * 设置插件参数
     *
     * @param params 插件参数
     * @return 是否设置成功
     */
    @Override
    public boolean setParams(Map<String, Object> params) {
        Object set = params.get("groupIdSet");
        if (set instanceof List<?>) {
            GROUP_ID_SET.clear();
            GROUP_ID_SET.addAll((List<String>) set);
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
        return Map.of("groupIdSet", GROUP_ID_SET);
    }


    @Scheduled(cron = "0 0 * * * ?")
    public void run() {
        if (this.isEnabled()) {
            //随机休眠0.5-1.5秒
            long sleepTime = RandomUtil.randomLong(500, 1500);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LocalDateTime now = LocalDateTime.now();
            String text = "现在时间是：" + now.format(dateTimeFormatter);
            MessageUtil.createTextMessage(text);
            GROUP_ID_SET.forEach(groupId -> {
                String eventStr = SendEntityUtil.buildSendGroupMessageStr(groupId,
                        List.of(MessageUtil.createTextMessage(text)));
                webSocketService.send(eventStr);
            });
        }
    }

    /**
     * 处理事件
     *
     * @param eventBody 事件体
     * @return 处理结果，如果不需要处理则返回null
     */
    @Override
    public PluginResult handleEvent(EventBody eventBody) {
        return PluginResult.SUCCESS();
    }

    @Override
    public boolean canHandle(EventBody eventBody) {
        return false;
    }
}
