package me.kafein.lumberjack.menu.impl;

import me.kafein.lumberjack.LumberjackPluginAPI;
import me.kafein.lumberjack.config.Config;
import me.kafein.lumberjack.config.ConfigType;
import me.kafein.lumberjack.lumberjack.Lumberjack;
import me.kafein.lumberjack.lumberjack.LumberjackStore;
import me.kafein.lumberjack.menu.Menu;
import me.kafein.lumberjack.menu.event.MenuClickEvent;
import me.kafein.lumberjack.util.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;

public class LumberjackMenu extends Menu {

    final private LumberjackStore lumberjackStore = LumberjackPluginAPI.get().getLumberjackStore();
    final private Config settingsConfig = LumberjackPluginAPI.get().getConfigStore().getConfig(ConfigType.settings);

    final private Plugin plugin;
    final private Config menuConfig;

    public LumberjackMenu(final Plugin plugin) {
        this.plugin = plugin;
        menuConfig = Config.create(plugin, "lumberjack_menu.yml", true);
    }

    @Override
    public void open(Player player) {

        final Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', menuConfig.getString("menu.title")));

        final Optional<Lumberjack> optionalLumberjack = lumberjackStore.get(player.getUniqueId());
        if (optionalLumberjack.isPresent()) {

            final Lumberjack lumberjack = optionalLumberjack.get();

            final List<String> loreList = menuConfig.getStringList("menu.items.info.lore");
            loreList.replaceAll(lore -> lore
                    .replace("%level%", Integer.toString(lumberjack.getLevel()))
                    .replace("%exp%", Long.toString(lumberjack.getExp())));

            final ItemBuilder itemBuilder = new ItemBuilder(Material.getMaterial(menuConfig.getString("menu.items.info.material")))
                    .setName(menuConfig.getString("menu.items.info.name")
                            .replace("%level%", Integer.toString(lumberjack.getLevel()))
                            .replace("%exp%", Long.toString(lumberjack.getExp())))
                    .setLore(loreList);

            inventory.setItem(4, itemBuilder.build());

        } else {

            final ItemBuilder itemBuilder = new ItemBuilder(Material.getMaterial(menuConfig.getString("menu.items.become-lumberjack.material")))
                    .setName(menuConfig.getString("menu.items.become-lumberjack.name"))
                    .setLore(menuConfig.getStringList("menu.items.become-lumberjack.lore"));

            inventory.setItem(4, itemBuilder.build());

        }

        if (menuConfig.getBoolean("menu.fill")) {
            final ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            for (int i = 0; i < 9; i++) {
                if (i == 4) continue;
                inventory.setItem(i, itemStack);
            }
        }

        Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(inventory));

    }

    @EventHandler
    public void onClick(MenuClickEvent e) {

        if (!e.getTitle().equals(ChatColor.translateAlternateColorCodes('&', menuConfig.getString("menu.title"))))
            return;

        e.setCancelled(true);

        if (e.getSlot() != 4) return;

        final Player player = e.getPlayer();
        final Optional<Lumberjack> optionalLumberjack = lumberjackStore.get(player.getUniqueId());

        if (!optionalLumberjack.isPresent()) {
            player.playSound(player.getLocation(), Sound.valueOf(settingsConfig.getString("settings.becomeLumberjackSound")), 1, 1);
            lumberjackStore.put(player.getUniqueId());
            open(player);
        }

    }

}
