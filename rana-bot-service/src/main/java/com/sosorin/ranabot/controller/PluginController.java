package com.sosorin.ranabot.controller;

import com.sosorin.ranabot.ResponseModel;
import com.sosorin.ranabot.plugin.Plugin;
import com.sosorin.ranabot.plugin.PluginManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author rana-bot
 * @since 2025/6/26  23:48
 */
@RestController
@RequestMapping("/plugin")
@Tag(name = "插件管理接口")
@Slf4j
public class PluginController {
    @Autowired
    private PluginManager pluginManager;

    @GetMapping("/list")
    @Operation(summary = "获取插件列表")
    public ResponseModel<List<Plugin>> listPlugins(@RequestParam(defaultValue = "enabled") String option) {
        if ("enabled".equals(option)) {
            return ResponseModel.SUCCESS(pluginManager.getAllEnabledPlugins());
        } else if ("all".equals(option)) {
            return ResponseModel.SUCCESS(pluginManager.getAllPlugins());
        }
        return ResponseModel.PARAM_ERROR("option");
    }

    @PostMapping("/enable")
    @Operation(summary = "启用插件")
    public ResponseModel<Plugin> enablePlugin(@RequestParam String pluginName) {
        Plugin plugin = pluginManager.getPlugin(pluginName);
        if (plugin == null) {
            return ResponseModel.FAIL("插件不存在");
        }
        if (pluginManager.registerPlugin(pluginName)) {
            return ResponseModel.SUCCESS(plugin);
        }
        return ResponseModel.FAIL("插件已启用");
    }

    @PostMapping("/disable")
    @Operation(summary = "禁用插件")
    public ResponseModel<Plugin> disablePlugin(@RequestParam String pluginName) {
        return pluginManager.unregisterPlugin(pluginName) ? ResponseModel.SUCCESS() : ResponseModel.FAIL("插件不存在或已禁用");
    }


    @PostMapping("/params/set")
    @Operation(summary = "设置插件参数")
    public ResponseModel<?> setPluginParams(@RequestBody Map<String, Object> params, @RequestParam(name = "pluginName") String pluginName) {
        Plugin plugin = pluginManager.getEnabledPlugin(pluginName);
        if (plugin == null) {
            return ResponseModel.FAIL("插件不存在");
        }
        params.forEach((key, value) -> {
            log.info("设置插件参数: {}={}", key, value);
        });
        boolean success = plugin.setParams(params);
        return success ? ResponseModel.SUCCESS() : ResponseModel.FAIL("设置参数失败");
    }


}
