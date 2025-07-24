package com.sosorin.ranabot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author rana-bot
 * @since 2025/7/24  22:58
 */
@Configuration
@ConfigurationProperties(prefix = "bot")
@Data
public class PluginConfigProperties {

    private List<String> enabledPlugins;

    private String pluginDir = "plugins";

    private String pluginDataDir = pluginDir + "/data";

}
