package com.sosorin.ranabot.plugin;

import com.sosorin.ranabot.model.EventBody;

import java.util.Map;

/**
 * 插件接口
 * 所有插件必须实现此接口
 *
 * @author rana-bot
 * @since 2025/6/28
 */
public interface Plugin {

    /**
     * 获取插件名称
     *
     * @return 插件名称
     */
    String getName();

    /**
     * 获取插件描述
     *
     * @return 插件描述
     */
    String getDescription();

    /**
     * 获取插件版本
     *
     * @return 插件版本
     */
    String getVersion();

    /**
     * 获取插件作者
     *
     * @return 插件作者
     */
    String getAuthor();

    /**
     * 插件初始化方法
     * 在插件加载时调用
     */
    void onEnable();

    /**
     * 插件卸载方法
     * 在插件卸载时调用
     */
    void onDisable();

    /**
     * 处理事件
     *
     * @param eventBody 事件体
     * @return 处理结果，如果不需要处理则返回null
     */
    String handleEvent(EventBody eventBody);

    /**
     * 检查插件是否支持处理此类事件
     *
     * @param eventBody 事件体
     * @return 是否支持处理
     */
    boolean canHandle(EventBody eventBody);

    /**
     * 获取插件参数
     *
     * @return 插件参数
     */
    Map<String, Object> getParams();

    /**
     * 设置插件参数
     *
     * @param params
     * @return
     */
    boolean setParams(Map<String, Object> params);

    /**
     * 插件的运行优先级 越小优先级越高 默认为0
     * @param plugin
     * @return
     */
    int getOrder(Plugin plugin);

    /**
     * 为了兼容【插件管理】插件加入的字段
     * todo: 如果有更优雅的方式就好了
     * @return
     */
    boolean isEnabled();
}