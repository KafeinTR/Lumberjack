package me.kafein.lumberjack.runnable;

import me.kafein.lumberjack.LumberjackPluginAPI;
import me.kafein.lumberjack.config.Config;
import me.kafein.lumberjack.config.ConfigType;
import me.kafein.lumberjack.lumberjack.Lumberjack;
import me.kafein.lumberjack.lumberjack.LumberjackStore;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

public class SkillRunnable extends CustomRunnable {

    final private Config config = LumberjackPluginAPI.get().getConfigStore().getConfig(ConfigType.settings);
    final private LumberjackStore lumberjackStore = LumberjackPluginAPI.get().getLumberjackStore();

    @Override
    public void start(final Plugin plugin) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, 20L, 20L);
    }

    @Override
    public void run() {

        Collection<Lumberjack> lumberjacks = lumberjackStore.getLumberjacks();
        if (lumberjacks.isEmpty()) return;

        final String text = ChatColor.translateAlternateColorCodes('&', config.getString("settings.skill-actionbar-text"));

        for (Lumberjack lumberjack : lumberjacks) {

            final int skillDuration = lumberjack.getSkillDuration();

            if (skillDuration <= 0) continue;

            lumberjack.setSkillDuration(skillDuration - 1);

            final Player player = Bukkit.getPlayer(lumberjack.getPlayerUUID());
            if (player != null) player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(String.valueOf(text).replace("%duration%", Integer.toString(skillDuration - 1))));

        }

    }

}
