package me.kafein.lumberjack.storage;

import me.kafein.lumberjack.lumberjack.Lumberjack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface Storage {

    /**
     * @param playerUUID the player id
     * @return the loaded lumberjack for player
     */
    @NotNull
    Lumberjack load(final UUID playerUUID);

    /**
     * save lumberjack to storage
     * @param lumberjack the lumberjack
     */
    void save(final Lumberjack lumberjack);

    /**
     * @param playerUUID the player id
     * @return contains player's lumberjack
     */
    boolean contains(final UUID playerUUID);

}
