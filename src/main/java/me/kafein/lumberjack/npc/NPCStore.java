package me.kafein.lumberjack.npc;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface NPCStore {

    /**
     * @return the created store
     */
    @NotNull
    static NPCStore create() {
        return new Impl();
    }

    /**
     * @param uuid the npc id
     * @return the npc
     */
    @NotNull
    Optional<NPC> getNPC(final UUID uuid);

    /**
     * puts the npc
     * @param npc the npc
     */
    void put(final NPC npc);

    /**
     * remove the npc
     * @param uuid the npc's id
     */
    void remove(final UUID uuid);

    /**
     * @param uuid the npc's id
     * @return the contains npc
     */
    boolean contains(final UUID uuid);

    /**
     * @return the npc collection
     */
    @NotNull
    Collection<NPC> getNPCCollection();

    class Impl implements NPCStore {

        final private Map<UUID, NPC> npcMap = new HashMap<>();

        @Override
        public @NonNull Optional<NPC> getNPC(final UUID uuid) {
            return Optional.ofNullable(npcMap.get(uuid));
        }

        @Override
        public void put(NPC npc) {
            npcMap.put(npc.getUniqueID(), npc);
        }

        @Override
        public void remove(UUID uuid) {
            npcMap.remove(uuid);
        }

        @Override
        public boolean contains(UUID uuid) {
            return npcMap.containsKey(uuid);
        }

        @Override
        public @NonNull Collection<NPC> getNPCCollection() {
            return npcMap.values();
        }

    }

}
