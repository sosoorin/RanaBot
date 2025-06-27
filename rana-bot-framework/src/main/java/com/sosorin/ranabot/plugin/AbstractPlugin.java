package com.sosorin.ranabot.plugin;

import com.sosorin.ranabot.annotation.RanaPlugin;
import com.sosorin.ranabot.model.EventBody;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 抽象插件类
 * 实现了Plugin接口的基本方法，插件开发者可以继承此类
 *
 * @author rana-bot
 * @since 2025/6/28
 */
@Slf4j
@Getter
@RanaPlugin
public abstract class AbstractPlugin implements Plugin {

    protected final String name = getClass().getAnnotation(RanaPlugin.class).value();
    protected final String description;
    protected final String version;
    protected final String author;
    protected final AtomicBoolean enabled = new AtomicBoolean(false);

    /**
     * 构造方法
     *
     * @param description 插件描述
     * @param version     插件版本
     * @param author      插件作者
     */
    public AbstractPlugin(String description, String version, String author) {
        this.description = description;
        this.version = version;
        this.author = author;
    }

    @Override
    public void onEnable() {
        enabled.set(true);
        log.info("插件 [{}] 已启用", name);
    }

    @Override
    public void onDisable() {
        enabled.set(false);
        log.info("插件 [{}] 已禁用", name);
    }

    @Override
    public boolean canHandle(EventBody eventBody) {
        // 默认实现，子类可以覆盖此方法提供更精确的判断
        return true;
    }

    /**
     * 获取插件参数
     *
     * @return 插件参数
     */
    @Override
    public Map<String, Object> getParams() {
        // 默认实现，子类可以覆盖此方法
        return new HashMap<>();
    }

    /**
     * 设置插件参数
     *
     * @param params 插件参数
     * @return 是否设置成功
     */
    @Override
    public boolean setParams(Map<String, Object> params) {
        // 默认实现，子类可以覆盖此方法
        return true;
    }

    @Override
    public int getOrder(Plugin plugin) {
        // 默认实现，子类可以覆盖此方法
        return 0;
    }

    @Override
    public boolean isEnabled() {
        return enabled.get();
    }
}