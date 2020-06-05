package io.zhile.research.intellij.helper;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.cl.PluginClassLoader;
import com.intellij.openapi.extensions.PluginId;

public class Constants {
    public static final PluginClassLoader CLASS_LOADER = (PluginClassLoader) Constants.class.getClassLoader();
    public static final IdeaPluginDescriptor PLUGIN_DESCRIPTOR = CLASS_LOADER.getPluginDescriptor();
    public static final PluginId PLUGIN_ID = CLASS_LOADER.getPluginId();
    public static final String PLUGIN_NAME = PLUGIN_DESCRIPTOR == null ? "ide-eval-resetter" : PLUGIN_DESCRIPTOR.getName();

    public static final String RESET_TIME_KEY = "trail_reset_time";
}
