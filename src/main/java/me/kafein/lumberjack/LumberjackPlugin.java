package me.kafein.lumberjack;

import lombok.Getter;
import me.kafein.lumberjack.command.LumberjackCMD;
import me.kafein.lumberjack.config.ConfigStore;
import me.kafein.lumberjack.listener.BlockListener;
import me.kafein.lumberjack.listener.InventoryListener;
import me.kafein.lumberjack.listener.PlayerListener;
import me.kafein.lumberjack.lumberjack.LumberjackStore;
import me.kafein.lumberjack.npc.NPCManager;
import me.kafein.lumberjack.npc.listener.TabCompleteListener;
import me.kafein.lumberjack.runnable.SkillRunnable;
import me.kafein.lumberjack.storage.Storage;
import me.kafein.lumberjack.storage.StorageFactory;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class LumberjackPlugin extends JavaPlugin implements LumberjackPluginAPI {

    @Getter private static LumberjackPlugin instance;

    @Getter final private Plugin plugin = this;
    @Getter private ConfigStore configStore;
    @Getter private NPCManager npcManager;
    @Getter private StorageFactory storageFactory;
    @Getter private LumberjackStore lumberjackStore;

    @Override
    public void onEnable() {

        instance = this;

        configStore = ConfigStore.create(this);
        npcManager = new NPCManager(this);
        storageFactory = new StorageFactory();
        lumberjackStore = LumberjackStore.create();

        storageFactory.loadStorage();
        npcManager.loadAllNPC();

        new SkillRunnable().start(this);

        getCommand("lumberjack").setExecutor(new LumberjackCMD(this));

        registerListeners();

    }

    @Override
    public void onDisable() {

        npcManager.saveAllNPC();

        lumberjackStore.saveAll();

    }

    private void registerListeners() {

        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerListener(), this);
        pluginManager.registerEvents(new BlockListener(), this);
        pluginManager.registerEvents(new InventoryListener(), this);

        npcManager.registerListeners();

    }

}
