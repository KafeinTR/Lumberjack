package me.kafein.lumberjack.npc.listener;

import me.kafein.lumberjack.menu.impl.LumberjackMenu;
import me.kafein.lumberjack.npc.event.NPCClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class NPCClickListener implements Listener {

    final private LumberjackMenu lumberjackMenu;

    public NPCClickListener(final Plugin plugin) {
        this.lumberjackMenu = new LumberjackMenu(plugin);
        lumberjackMenu.register(plugin);
    }

    @EventHandler
    public void onClick(final NPCClickEvent e) {

        lumberjackMenu.open(e.getPlayer());

    }

}
