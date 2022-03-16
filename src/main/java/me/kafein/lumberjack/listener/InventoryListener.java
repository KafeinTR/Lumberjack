package me.kafein.lumberjack.listener;

import me.kafein.lumberjack.menu.event.MenuClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(final InventoryClickEvent e) {

        final Inventory inventory = e.getClickedInventory();

        if (inventory == null) return;

        final Player player = (Player) e.getWhoClicked();

        if (inventory.equals(player.getInventory())) return;

        final ItemStack itemStack = e.getCurrentItem();

        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        final MenuClickEvent menuClickEvent = new MenuClickEvent(player, inventory, e.getView().getTitle(), itemStack, e.getSlot(), false);
        Bukkit.getPluginManager().callEvent(menuClickEvent);
        if (menuClickEvent.isCancelled()) e.setCancelled(true);

    }

}
