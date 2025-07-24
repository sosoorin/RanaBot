package com.sosorin.ranabot.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.PathUtil;
import com.sosorin.ranabot.plugin.DynamicPluginLoader;
import com.sosorin.ranabot.plugin.PluginManager;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.file.Path;
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
public class PluginConfig {

    private final PluginManager pluginManager;

    @Autowired
    public PluginConfig(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Autowired
    private DynamicPluginLoader dynamicPluginLoader;
    @Autowired
    private PluginConfigProperties pluginConfigProperties;

    /**
     * 初始化插件
     * 在Spring容器启动后自动执行
     */
    @PostConstruct
    public void init() {
        log.info("开始注册插件...");

        String pluginDir = pluginConfigProperties.getPluginDir();
        String pathStr = FileUtil.getAbsolutePath(pluginDir);
        log.info("插件目录: {}", pathStr);

        Path path = Path.of(pathStr);
        // 从目录中加载插件
        List<File> jars = PathUtil.loopFiles(path, file -> {
            if (file.isFile() && file.getName().endsWith(FileUtil.JAR_FILE_EXT)) {
                log.info("扫描到jar包: {}", file.getName());
                try {
                    dynamicPluginLoader.loadJar(file.getParent(), file.getName());
                } catch (Exception e) {
                    log.error("加载插件失败: {}", e.getMessage());
                    return false;
                }
                return true;
            }
            return false;
        });

        // 注册所有插件
        List<String> enabledPlugins = pluginConfigProperties.getEnabledPlugins();
        enabledPlugins.forEach(pluginManager::registerPluginByName);

        log.info("插件注册完成，共注册 {} 个插件", pluginManager.getAllEnabledPlugins().size());
    }


} 