package me.kafein.lumberjack.npc;

import lombok.Getter;
import me.kafein.lumberjack.npc.event.NPCClickEvent;
import me.kafein.lumberjack.npc.listener.NPCClickListener;
import me.kafein.lumberjack.npc.listener.TabCompleteListener;
import me.kafein.lumberjack.packet.PacketInjector;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.Optional;
import java.util.UUID;

public class NPCManager {

    @Getter
    final private NPCStore store = NPCStore.create();
    @Getter
    final private NPCLoader loader;

    final private Plugin plugin;

    public NPCManager(final Plugin plugin) {
        this.plugin = plugin;
        loader = NPCLoader.getInstance(plugin);
    }

    public NPC createNPC(final Location location, final String name, final String skinName) {
        final NPC npc = NPC.create(plugin, location, name, skinName);
        store.put(npc);
        return npc;
    }

    public NPC createNPC(final Location location, final String name) {
        final NPC npc = NPC.create(plugin, location, name);
        store.put(npc);
        return npc;
    }

    public void deleteNPC(final UUID uuid) {
        final Optional<NPC> npc = store.getNPC(uuid);
        npc.ifPresent(value -> Bukkit.getOnlinePlayers().forEach(value::removePacket));
        store.remove(uuid);
        loader.delete(uuid);
    }

    public void loadAllNPC() {
        loader.load().forEach(store::put);
    }

    public void saveAllNPC() {
        loader.save(store.getNPCCollection());
    }

    public void sendPackets(final Player player) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {

            store.getNPCCollection().forEach(npc -> npc.sendPacket(player));

            new PacketInjector(player, PacketPlayInUseEntity.class) {

                int count = 0;

                @Override
                public void onRead(Object object) {

                    if (count == 0) {
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            int id = (int) getValue(object, "a");
                            for (NPC npc : store.getNPCCollection()) {
                                if (npc.getEntityPlayer().ae() == id) Bukkit.getPluginManager().callEvent(new NPCClickEvent(player, npc));
                            }
                        });
                    }

                    if (count == 3) count = 0;
                    else count++;

                }

            }.register();

        }, 20L);
    }

    public void removePackets(final Player player) {
        store.getNPCCollection().forEach(npc -> npc.removePacket(player));
    }

    public void registerListeners() {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new TabCompleteListener(), plugin);
        pluginManager.registerEvents(new NPCClickListener(plugin), plugin);
    }

}
