package io.zhile.research.intellij.helper;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.plugins.cl.PluginClassLoader;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.extensions.PluginId;

public class Constants {
    public static final PluginClassLoader CLASS_LOADER = (PluginClassLoader) Constants.class.getClassLoader();
    public static final PluginId PLUGIN_ID = CLASS_LOADER.getPluginId();
    public static final String PLUGIN_NAME = PluginManager.getPlugin(PLUGIN_ID).getName();
    public static final String PRODUCT_NAME = ApplicationNamesInfo.getInstance().getFullProductName();
    public static final String PRODUCT_HASH = Integer.toString(PathManager.getConfigPath().hashCode());
}
