package me.kafein.lumberjack.menu.event;

import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuClickEvent extends Event implements Cancellable {

    final private static HandlerList handlerList = new HandlerList();

    final private Player player;
    final private Inventory inventory;
    final private String title;
    final private ItemStack item;
    final private int slot;
    private boolean cancelled;

    public MenuClickEvent(final Player player, final Inventory inventory, final String title, final ItemStack item, final int slot, final boolean cancelled) {
        this.player = player;
        this.inventory = inventory;
        this.title = title;
        this.item = item;
        this.slot = slot;
        this.cancelled = cancelled;
    }

    public Player getPlayer() {
        return player;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getTitle() {
        return title;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getSlot() {
        return slot;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NonNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}
