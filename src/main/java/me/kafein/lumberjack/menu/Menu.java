package me.kafein.lumberjack.menu;

import me.kafein.lumberjack.menu.event.MenuClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class Menu implements Listener {

    /**
     * open menu to player
     * @param player the player
     */
    public abstract void open(final Player player);

    /**
     * if clicks menu
     * @param e the event
     */
    public abstract void onClick(final MenuClickEvent e);

    /**
     * register the menu's listeners
     * @param plugin the plugin
     */
    public void register(final Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

}
