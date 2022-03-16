package me.kafein.lumberjack;

import me.kafein.lumberjack.config.ConfigStore;
import me.kafein.lumberjack.lumberjack.LumberjackStore;
import me.kafein.lumberjack.npc.NPCManager;
import me.kafein.lumberjack.storage.StorageFactory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LumberjackPluginAPI {

    /**
     * @return the api
     */
    @Nullable
    static LumberjackPluginAPI get() {
        return LumberjackPlugin.getInstance();
    }

    /**
     * @return the plugin
     */
    @NotNull
    Plugin getPlugin();

    /**
     * @return the config store
     */
    @NotNull
    ConfigStore getConfigStore();

    /**
     * @return npc manager (store,loader)
     */
    @NotNull
    NPCManager getNpcManager();

    /**
     * @return the storage factory
     */
    @NotNull
    StorageFactory getStorageFactory();

    /**
     * @return the lumberjack store
     */
    @NotNull
    LumberjackStore getLumberjackStore();

}
