package me.kafein.lumberjack.util.timber;

import me.kafein.lumberjack.LumberjackPluginAPI;
import me.kafein.lumberjack.config.Config;
import me.kafein.lumberjack.config.ConfigType;
import me.kafein.lumberjack.lumberjack.Lumberjack;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface TimberSerializer {

    /**
     * @return the created instance
     */
    @NotNull
    static TimberSerializer getInstance() {
        return new Impl();
    }

    /**
     * knockdown the tree
     * @param start the first block
     * @param lumberjack the lumberjack
     * @return the if knockdowned tree
     */
    boolean knockDown(final Location start, final Lumberjack lumberjack);

    class Impl implements TimberSerializer {

        final private Config config = LumberjackPluginAPI.get().getConfigStore().getConfig(ConfigType.settings);

        @Override
        public boolean knockDown(Location start, Lumberjack lumberjack) {

            final Player player = lumberjack.getPlayer();
            if (player == null) return false;

            int radius = 2;

            final World world = start.getWorld();
            int x = start.getBlockX();
            int y = start.getBlockY();
            int z = start.getBlockZ();

            int max = config.getInteger("settings.max-knockdown-block");

            for (int fy = y; fy < (y + 30); fy++) {
                for (int fx = x - radius; fx <= x + radius; fx++) {
                    for (int fz = z - radius; fz <= z + radius; fz++) {
                        if (max <= 0) return true;
                        max--;
                        Location location = new Location(world, fx, fy, fz);
                        Block block = location.getBlock();
                        final String blockType = block.getType().name();
                        if (blockType.endsWith("LOG") || blockType.endsWith("LEAVES")) {
                            player.breakBlock(block);
                        }
                    }
                }
            }

            return false;

        }

    }

}
