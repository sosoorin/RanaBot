package com.sosorin.ranabot.config;

import cn.hutool.core.io.FileUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author rana-bot
 * @since 2025/7/24  22:58
 */
@Configuration
@ConfigurationProperties(prefix = "bot")
@Data
@Slf4j
public class PluginConfigProperties {

    private List<String> enabledPlugins;

    private String pluginDir = "plugins";
    
    private String pluginDataDir;

    public String getPluginDir() {
        return FileUtil.getAbsolutePath(pluginDir);
    }

    public String getPluginDataDir() {
        String dataDir = StringUtils.hasText(pluginDataDir) ? pluginDataDir : pluginDir + "/data";
        return FileUtil.getAbsolutePath(dataDir);
    }

}
