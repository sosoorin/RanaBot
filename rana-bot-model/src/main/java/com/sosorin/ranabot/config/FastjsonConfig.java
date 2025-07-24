package com.sosorin.ranabot.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.sosorin.ranabot.entity.event.EventBodyDeserializer;
import com.sosorin.ranabot.entity.message.Message;
import com.sosorin.ranabot.entity.message.MessageDeserializer;
import com.sosorin.ranabot.model.EventBody;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

/**
 * Fastjson2配置类
 * 用于注册自定义反序列化器
 */
@Configuration
public class FastjsonConfig {

    @PostConstruct
    public void init() {
        // 配置Fastjson2的全局设置
        JSON.config(JSONReader.Feature.SupportSmartMatch);  // 支持松散匹配字段
        JSON.config(JSONReader.Feature.ErrorOnNotSupportAutoType);    // 支持自动类型
        JSON.config(JSONReader.Feature.IgnoreSetNullValue); // 忽略null值的设置

        // 注册反序列化器
        JSON.register(Message.class, new MessageDeserializer());
        JSON.register(EventBody.class, new EventBodyDeserializer());

        // 设置自动类型处理
        System.setProperty("fastjson2.parser.autoTypeAccept", "com.sosorin.ranabot.entity.");
    }
} 