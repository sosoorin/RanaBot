package com.sosorin.ranabot.plugin;

import cn.hutool.extra.spring.SpringUtil;
import com.sosorin.ranabot.model.EventBody;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 插件管理器
 * 负责插件的注册、卸载和事件分发
 *
 * @author rana-bot
 * @since 2025/6/28
 */
@Slf4j
@Component
public class PluginManager {

    /**
     * 插件列表，使用ConcurrentHashMap保证线程安全
     */
    private final Map<String, Plugin> plugins = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("插件管理器初始化完成");
    }

    /**
     * 注册插件
     *
     * @param plugin 插件实例
     * @return 是否注册成功
     */
    public boolean registerPlugin(Plugin plugin) {
        if (plugin == null) {
            log.error("插件不能为空");
            return false;
        }

        String pluginName = plugin.getName();
        if (plugins.containsKey(pluginName)) {
            log.error("插件 [{}] 已存在，无法重复注册", pluginName);
            return false;
        }

        plugins.put(pluginName, plugin);
        try {
            plugin.onEnable();
            log.info("插件 [{}] 注册成功", pluginName);
            return true;
        } catch (Exception e) {
            plugins.remove(pluginName);
            log.error("插件 [{}] 启用失败: {}", pluginName, e.getMessage());
            return false;
        }
    }

    public boolean registerPlugin(String pluginName) {
        try {
            Object bean = SpringUtil.getBean(pluginName);
            if (bean instanceof Plugin) {
                return registerPlugin((Plugin) bean);
            } else {
                log.error("{} 不是一个有效的插件", pluginName);
                return false;
            }
        } catch (Exception e) {
            log.error("{} 注册失败: {}", pluginName, e.getMessage());
            return false;
        }
    }

    /**
     * 卸载插件
     *
     * @param pluginName 插件名称
     * @return 是否卸载成功
     */
    public boolean unregisterPlugin(String pluginName) {
        Plugin plugin = plugins.get(pluginName);
        if (plugin == null) {
            log.error("插件 [{}] 不存在，无法卸载", pluginName);
            return false;
        }

        try {
            plugin.onDisable();
            plugins.remove(pluginName);
            log.info("插件 [{}] 卸载成功", pluginName);
            return true;
        } catch (Exception e) {
            log.error("插件 [{}] 卸载失败: {}", pluginName, e.getMessage());
            return false;
        }
    }

    /**
     * 获取所有插件
     *
     * @return 插件列表
     */
    public List<Plugin> getAllPlugins() {
        Map<String, Plugin> beansOfType = SpringUtil.getBeansOfType(Plugin.class);
        return new ArrayList<>(beansOfType.values());
    }

    /**
     * 获取所有已注册的插件
     *
     * @return 插件列表
     */
    public List<Plugin> getAllEnabledPlugins() {
        return new ArrayList<>(plugins.values());
    }

    /**
     * 获取插件实例
     *
     * @param pluginName 插件名称
     * @return 插件实例，如果不存在则返回null
     */
    public Plugin getEnabledPlugin(String pluginName) {
        return plugins.get(pluginName);
    }

    public Plugin getPlugin(String pluginName) {
        try {
            Object bean = SpringUtil.getBean(pluginName);
            if (bean instanceof Plugin) {
                return (Plugin) bean;
            } else {
                log.error("{} 插件不存在", pluginName);
                return null;
            }
        } catch (Exception e) {
            log.error("{} 插件不存在: {}", pluginName, e.getMessage());
            return null;
        }
    }

    /**
     * 处理事件
     * 将事件分发给所有支持处理该事件的插件
     *
     * @param eventBody 事件体
     * @return 处理结果列表
     */
    public List<PluginResult> handleEvent(EventBody eventBody) {
        if (eventBody == null) {
            log.error("事件不能为空");
            return new ArrayList<>();
        }

        log.debug("开始处理事件: {}", eventBody);

        // 筛选出能处理此事件的插件
        List<Plugin> handlers = plugins.values().stream()
                .filter(plugin -> {
                    try {
                        return plugin.canHandle(eventBody);
                    } catch (Exception e) {
                        log.error("插件 [{}] 判断是否能处理事件时发生异常: {}", plugin.getName(), e.getMessage());
                        return false;
                    }
                })
                .collect(Collectors.toList());

        log.debug("找到 {} 个插件可以处理此事件", handlers.size());

        // 调用每个插件的处理方法
        List<PluginResult> results = new ArrayList<>();
        for (Plugin plugin : handlers) {
            try {
                String result = plugin.handleEvent(eventBody);
                results.add(new PluginResult(plugin.getName(), result, true));
                log.debug("插件 [{}] 处理事件成功: {}", plugin.getName(), result);
            } catch (Exception e) {
                results.add(new PluginResult(plugin.getName(), e.getMessage(), false));
                log.error("插件 [{}] 处理事件时发生异常: {}", plugin.getName(), e.getMessage());
            }
        }

        return results;
    }

    /**
     * 插件处理结果
     */
    public static class PluginResult {
        /**
         * 插件名称
         */
        private final String pluginName;

        /**
         * 处理结果
         */
        private final String result;

        /**
         * 是否处理成功
         */
        private final boolean success;

        public PluginResult(String pluginName, String result, boolean success) {
            this.pluginName = pluginName;
            this.result = result;
            this.success = success;
        }

        public String getPluginName() {
            return pluginName;
        }

        public String getResult() {
            return result;
        }

        public boolean isSuccess() {
            return success;
        }

        @Override
        public String toString() {
            return "PluginResult{" +
                    "pluginName='" + pluginName + '\'' +
                    ", result='" + result + '\'' +
                    ", success=" + success +
                    '}';
        }
    }
} 