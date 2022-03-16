package me.kafein.lumberjack.npc.listener;

import me.kafein.lumberjack.LumberjackPluginAPI;
import me.kafein.lumberjack.npc.NPCManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

public class TabCompleteListener implements Listener {

    final private NPCManager npcManager = LumberjackPluginAPI.get().getNpcManager();

    @EventHandler
    public void onComplete(final TabCompleteEvent e) {

        npcManager.getStore().getNPCCollection().forEach(npc -> {
            e.getCompletions().remove(npc.getName());
        });

    }

}
