package com.sosorin.ranabot.config;

import com.sosorin.ranabot.plugin.PluginManager;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 插件配置类
 * 用于注册和初始化插件
 *
 * @author rana-bot
 * @since 2025/6/28
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "bot")
public class PluginConfig {

    private final PluginManager pluginManager;

    @Autowired
    public PluginConfig(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Setter
    private List<String> enabledPlugins;

    /**
     * 初始化插件
     * 在Spring容器启动后自动执行
     */
    @PostConstruct
    public void init() {
        log.info("开始注册插件...");

        // 注册所有插件
        enabledPlugins.forEach(pluginManager::registerPlugin);

        log.info("插件注册完成，共注册 {} 个插件", pluginManager.getAllEnabledPlugins().size());
    }


} 