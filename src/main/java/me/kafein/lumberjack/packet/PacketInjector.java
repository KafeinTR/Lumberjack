package me.kafein.lumberjack.packet;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import me.kafein.lumberjack.util.reflection.ReflectionUtils;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public abstract class PacketInjector {

    final private Player player;
    final private Class<?> packetClass;

    public PacketInjector(final Player player, final Class<?> packetClass) {
        this.player = player;
        this.packetClass = packetClass;
    }

    public abstract void onRead(final Object object);

    public PacketInjector register() {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object object) throws Exception {
                if (object.getClass().equals(packetClass)) {
                    onRead(object);
                    return;
                }
                super.channelRead(ctx, object);
            }
        };
        inject(channelDuplexHandler);
        return this;
    }

    private void inject(final ChannelDuplexHandler channelDuplexHandler) {
        ChannelPipeline channelPipeline = ((CraftPlayer) player).getHandle().b.a().k.pipeline();
        channelPipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
    }

    protected Object getValue(final Object object, final String field) {
        try {
            return ReflectionUtils.getValue(object, true, field);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

}
