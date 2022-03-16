package me.kafein.lumberjack.listener;

import me.kafein.lumberjack.LumberjackPluginAPI;
import me.kafein.lumberjack.config.Config;
import me.kafein.lumberjack.config.ConfigStore;
import me.kafein.lumberjack.config.ConfigType;
import me.kafein.lumberjack.lumberjack.Lumberjack;
import me.kafein.lumberjack.lumberjack.LumberjackStore;
import me.kafein.lumberjack.util.level.LevelSerializer;
import me.kafein.lumberjack.util.timber.TimberSerializer;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class BlockListener implements Listener {

    final private Config settingsConfig = LumberjackPluginAPI.get().getConfigStore().getConfig(ConfigType.settings);
    final private LumberjackStore lumberjackStore = LumberjackPluginAPI.get().getLumberjackStore();
    final private TimberSerializer timberSerializer = TimberSerializer.getInstance();
    final private LevelSerializer levelSerializer = LevelSerializer.getInstance();

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBreak(final BlockBreakEvent e) {

        final Block block = e.getBlock();

        if (!block.getType().name().endsWith("LOG")) return;

        final Player player = e.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE) return;

        final ItemStack itemHand = player.getItemInHand();
        if (!itemHand.getType().name().endsWith("_AXE")) return;

        final Optional<Lumberjack> optionalLumberjack = lumberjackStore.get(player.getUniqueId());
        if (!optionalLumberjack.isPresent()) return;

        final Lumberjack lumberjack = optionalLumberjack.get();

        if (lumberjack.getSkillDuration() > 0) return;

        final int maxLevel = settingsConfig.getInteger("settings.level.max-level");
        final int multiplier = settingsConfig.getInteger("settings.skill-duration-multiplier");
        final int applied = ((maxLevel * multiplier) - ((lumberjack.getLevel() - 1) * multiplier));
        lumberjack.setSkillDuration(applied);

        timberSerializer.knockDown(block.getLocation(), lumberjack);

        player.getWorld().playSound(player.getLocation(), Sound.valueOf(settingsConfig.getString("settings.knockDownSound")), 1, 1);

        levelSerializer.applyExp(lumberjack);

    }

}
