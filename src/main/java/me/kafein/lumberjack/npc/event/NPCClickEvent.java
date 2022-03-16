package me.kafein.lumberjack.npc.event;

import me.kafein.lumberjack.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCClickEvent extends Event {

    final private static HandlerList handlerList = new HandlerList();

    final private Player player;
    final private NPC npc;

    public NPCClickEvent(final Player player, final NPC npc) {
        this.player = player;
        this.npc = npc;
    }

    public Player getPlayer() {
        return player;
    }

    public NPC getNPC() {
        return npc;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}
