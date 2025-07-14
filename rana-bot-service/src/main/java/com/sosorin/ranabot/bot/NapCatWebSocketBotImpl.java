package com.sosorin.ranabot.bot;

import cn.hutool.core.util.StrUtil;
import com.sosorin.bot.IBot;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import org.springframework.stereotype.Service;

/**
 * @author rana-bot
 * @since 2025/6/26  14:58
 */
@Service
@Slf4j
public class NapCatWebSocketBotImpl implements IBot {

    @Resource(name = "napCatWebSocketClient")
    private WebSocket napCatWebSocketClient;

    @Override
    public WebSocket getWebSocketClient() {
        return napCatWebSocketClient;
    }

    @Override
    public boolean send(String message) {
        log.info("发送消息：{}", message);
        if (StrUtil.isEmpty(message)) {
            log.warn("消息为空");
            return false;
        }
        /*
        //随机延迟0.5 ~ 2s
        long delay = ThreadLocalRandom.current().nextLong(500, 2000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
         */
        return napCatWebSocketClient.send(message);
    }

    @Override
    public boolean close() {
        return napCatWebSocketClient.close(1000, "");
    }


}
