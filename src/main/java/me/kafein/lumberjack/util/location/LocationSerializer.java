package me.kafein.lumberjack.util.location;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LocationSerializer {

    /**
     * @return created instance
     */
    @NotNull
    static LocationSerializer getInstance() {
        return new Impl();
    }

    /**
     * String value serialize to location
     * @param value the to be serialized
     * @return the serialized location
     */
    @Nullable
    Location serialize(final String value);

    /**
     * location serialize to text
     * @param location the to be serialized
     * @return the serialized text
     */
    @NotNull
    String deserialize(final Location location);

    class Impl implements LocationSerializer {

        @Override
        public Location serialize(String value) {
            final String[] worldSplitter = value.split(": ");
            final String[] locSplitter = worldSplitter[1].split(", ");
            return new Location(
                    Bukkit.getWorld(worldSplitter[0]),
                    Integer.parseInt(locSplitter[0]),
                    Integer.parseInt(locSplitter[1]),
                    Integer.parseInt(locSplitter[2]));
        }

        @Override
        public @NonNull String deserialize(Location location) {
            return location.getWorld().getName() + ": " +
                    location.getBlockX() + ", " +
                    location.getBlockY() + ", " +
                    location.getBlockZ() + ", ";
        }

    }

}
