package me.kafein.lumberjack.listener;

import me.kafein.lumberjack.LumberjackPluginAPI;
import me.kafein.lumberjack.lumberjack.Lumberjack;
import me.kafein.lumberjack.lumberjack.LumberjackStore;
import me.kafein.lumberjack.npc.NPCManager;
import me.kafein.lumberjack.storage.StorageFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerListener implements Listener {

    final private StorageFactory storageFactory = LumberjackPluginAPI.get().getStorageFactory();
    final private LumberjackStore lumberjackStore = LumberjackPluginAPI.get().getLumberjackStore();
    final private NPCManager npcManager = LumberjackPluginAPI.get().getNpcManager();

    @EventHandler (priority = EventPriority.LOWEST)
    public void onAsyncPreLogin(final AsyncPlayerPreLoginEvent e) {
        if (storageFactory.getStorage().contains(e.getUniqueId())) lumberjackStore.put(storageFactory.getStorage().load(e.getUniqueId()));
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onAsyncPreLoginMonitor(final AsyncPlayerPreLoginEvent e) {
        if (lumberjackStore.contains(e.getUniqueId()) || !storageFactory.getStorage().contains(e.getUniqueId())) return;
        lumberjackStore.put(storageFactory.getStorage().load(e.getUniqueId()));
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onLogin(final PlayerLoginEvent e) {
        final Player player = e.getPlayer();
        npcManager.sendPackets(player);
        final UUID playerUUID = player.getUniqueId();
        if (lumberjackStore.contains(playerUUID) || !storageFactory.getStorage().contains(playerUUID)) return;
        lumberjackStore.put(storageFactory.getStorage().load(playerUUID));
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onLoginMonitor(final PlayerLoginEvent e) {
        final Player player = e.getPlayer();
        final UUID playerUUID = player.getUniqueId();
        if (lumberjackStore.contains(playerUUID) || !storageFactory.getStorage().contains(playerUUID)) return;
        lumberjackStore.put(storageFactory.getStorage().load(playerUUID));
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        npcManager.removePackets(player);
        final UUID playerUUID = player.getUniqueId();
        final Optional<Lumberjack> optionalLumberjack = lumberjackStore.get(playerUUID);
        if (!optionalLumberjack.isPresent()) return;
        CompletableFuture.runAsync(() -> storageFactory.getStorage().save(optionalLumberjack.get()));
    }

}
