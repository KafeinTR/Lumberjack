package me.kafein.lumberjack.lumberjack;

import lombok.NonNull;
import me.kafein.lumberjack.LumberjackPluginAPI;
import me.kafein.lumberjack.storage.Storage;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public interface LumberjackStore {

    /**
     * @return the created store.
     */
    @NotNull
    static LumberjackStore create() {
        return new Impl();
    }

    /**
     * save all players lumberjack
     */
    void saveAll();

    /**
     * create a lumberjack
     * puts the created lumberjack
     *
     * @param playerUUID the player id
     */
    void put(final UUID playerUUID);

    /**
     * puts the lumberjack
     *
     * @param lumberjack the to be adjusted
     */
    void put(final Lumberjack lumberjack);

    /**
     * remove the selected player's lumberjack
     *
     * @param playerUUID the player id
     */
    void remove(final UUID playerUUID);

    /**
     * @param playerUUID the player id
     * @return contains player's lumberjack
     */
    boolean contains(final UUID playerUUID);

    /**
     * @param playerUUID the player id
     * @return the player's lumberjack
     */
    @NotNull
    Optional<Lumberjack> get(final UUID playerUUID);

    /**
     * @return the lumberjacks
     */
    @NotNull
    Collection<Lumberjack> getLumberjacks();

    class Impl implements LumberjackStore {

        final private Map<UUID, Lumberjack> lumberjackMap = new ConcurrentHashMap<>();

        @Override
        public void saveAll() {
            final Storage storage = LumberjackPluginAPI.get().getStorageFactory().getStorage();
            lumberjackMap.values().forEach(storage::save);
        }

        @Override
        public void put(UUID playerUUID) {
            lumberjackMap.put(playerUUID, Lumberjack.create(playerUUID));
        }

        @Override
        public void put(Lumberjack lumberjack) {
            lumberjackMap.put(lumberjack.getPlayerUUID(), lumberjack);
        }

        @Override
        public void remove(UUID playerUUID) {
            lumberjackMap.remove(playerUUID);
        }

        @Override
        public boolean contains(UUID playerUUID) {
            return lumberjackMap.containsKey(playerUUID);
        }

        @Override
        public @NonNull Optional<Lumberjack> get(UUID playerUUID) {
            return Optional.ofNullable(lumberjackMap.get(playerUUID));
        }

        @Override
        public @NonNull Collection<Lumberjack> getLumberjacks() {
            return lumberjackMap.values();
        }
    }

}
