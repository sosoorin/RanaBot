package com.sosorin.ranabot.config;

import com.sosorin.ranabot.websocket.listener.NapCatWebSocketApiListener;
import com.sosorin.ranabot.websocket.listener.NapCatWebSocketEventListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rana-bot
 * @since 2025/6/26  15:09
 */
@Configuration
@Slf4j
@ConfigurationProperties(prefix = "nap-cat.ws")
@Data
public class NapCatWebSocketClientConfig {

    private String url = "ws://localhost:3001";

    private String token = "default_token";

    final OkHttpClient client = new OkHttpClient().newBuilder().build();

    @Autowired
    private ApplicationContext applicationContext;

    @Bean("napCatWebSocketEventClient")
    public WebSocket napCatWebSocketEventClient() {
        Request request = new Request.Builder().url(url + "/event").addHeader("Authorization", token).build();
        // 从ApplicationContext中获取NapCatWebSocketListener实例
        NapCatWebSocketEventListener listener = applicationContext.getBean(NapCatWebSocketEventListener.class);
        WebSocket webSocket = client.newWebSocket(request, listener);
        return webSocket;
    }

    @Bean("napCatWebSocketApiClient")
    public WebSocket napCatWebSocketClient() {
        Request request = new Request.Builder().url(url + "/api").addHeader("Authorization", token).build();
        // 从ApplicationContext中获取NapCatWebSocketListener实例
        NapCatWebSocketApiListener listener = applicationContext.getBean(NapCatWebSocketApiListener.class);
        WebSocket webSocket = client.newWebSocket(request, listener);
        return webSocket;
    }


}
