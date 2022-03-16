package me.kafein.lumberjack.util.level;

import me.kafein.lumberjack.LumberjackPluginAPI;
import me.kafein.lumberjack.config.Config;
import me.kafein.lumberjack.config.ConfigType;
import me.kafein.lumberjack.lumberjack.Lumberjack;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public interface LevelSerializer {

    /**
     * @return the created instance
     */
    @NotNull
    static LevelSerializer getInstance() {
        return new Impl();
    }

    /**
     * exp serialize to level
     * @param exp the exp
     * @return serialized level
     */
    int serialize(final long exp);

    /**
     * level serialize to exp
     * @param level the level
     * @return serialized exp
     */
    long deserialize(final int level);

    /**
     * apply exp when lumberjack knockdown the wood
     * @param lumberjack the lumberjack
     */
    void applyExp(final Lumberjack lumberjack);

    class Impl implements LevelSerializer {

        final private Config settingsConfig = LumberjackPluginAPI.get().getConfigStore().getConfig(ConfigType.settings);
        final private Config languageConfig = LumberjackPluginAPI.get().getConfigStore().getConfig(ConfigType.language);
        final private Random random = new Random();
        private long multiplier = settingsConfig.getLong("settings.level.level-multiplier");

        @Override
        public int serialize(long exp) {
            return (int) ((exp / multiplier) % multiplier) + 1;
        }

        @Override
        public long deserialize(int level) {
            return ((long) level * multiplier);
        }

        @Override
        public void applyExp(Lumberjack lumberjack) {

            final Player player = lumberjack.getPlayer();

            final int lumberjackLevel = lumberjack.getLevel();
            if (lumberjackLevel >= settingsConfig.getInteger("settings.level.max-level")) return;

            final String appliedExp = settingsConfig.getString("settings.level.applied-exp");
            long exp = appliedExp.contains("-")
                    ? random.nextInt(Integer.parseInt(appliedExp.split("-")[1]) - Integer.parseInt(appliedExp.split("-")[0])) + Integer.parseInt(appliedExp.split("-")[0])
                    : Integer.parseInt(appliedExp);

            lumberjack.addExp(exp);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getString("language.lumberjack.givedExp").replace("%exp%", Long.toString(exp))));
            if (lumberjackLevel < lumberjack.getLevel())
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getString("language.lumberjack.levelUp").replace("%level%", Integer.toString(lumberjack.getLevel()))));

        }

    }

}
