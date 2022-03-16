package me.kafein.lumberjack.npc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EnumItemSlot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface NPC {

    /**
     * @param plugin the plugin
     * @param location the npc's location
     * @param name the npc's name
     * @return the created npc
     */
    @NotNull
    static NPC create(final Plugin plugin, final Location location, final String name) {
        return new Impl(plugin, location, name);
    }

    /**
     * @param plugin the plugin
     * @param location the npc's location
     * @param name the npc's name
     * @param skin the npc's skin
     * @return the created npc
     */
    @NotNull
    static NPC create(final Plugin plugin, final Location location, final String name, final String skin) {
        return new Impl(plugin, location, name, skin);
    }

    /**
     * @return the npc's id
     */
    @NotNull
    UUID getUniqueID();

    /**
     * @return the npc's name
     */
    @NotNull
    String getName();

    /**
     * @return npc's skin name
     */
    @NotNull
    String getSkin();

    /**
     * @return the npc's location
     */
    @NotNull
    Location getLocation();

    /**
     * @return the entity player from npc
     */
    @NotNull
    EntityPlayer getEntityPlayer();

    /**
     * set the npc's location
     * @param location the location to be adjusted
     */
    void setLocation(final Location location);

    /**
     * set the npc's skin
     * @param skinName the skin to be adjusted
     */
    void setSkin(final String skinName);

    /**
     * set the npc's equipments
     * @param enumItemSlot the slot to be adjusted
     * @param itemStack the item to be adjusted
     */
    void setEquipment(final EnumItemSlot enumItemSlot, final ItemStack itemStack);

    /**
     * send npc's packets to player
     * @param player the player
     */
    void sendPacket(final Player player);

    /**
     * receive npc's packets from player
     * @param player the player
     */
    void removePacket(final Player player);

    /**
     * load skin the adjusted
     */
    void loadSkin();

    /**
     * reload npc for player
     * @param player the player id
     */
    void reloadNPC(final Player player);

    class Impl implements NPC {

        final private List<Pair<EnumItemSlot, net.minecraft.world.item.ItemStack>> equipmentList = new ArrayList<>();

        @Getter private EntityPlayer entityPlayer;

        final private Plugin plugin;
        @Getter @NonNull final private UUID uniqueID;
        @Getter @NonNull final private String name;
        @Getter @NonNull private Location location;
        @Getter @NonNull private String skin;

        public Impl(final Plugin plugin, final Location location, final String name) {
            this.plugin = plugin;
            this.uniqueID = UUID.randomUUID();
            this.location = location;
            this.name = name;
            this.skin = name;
            init();
        }

        public Impl(final Plugin plugin, final Location location, final String name, final String skin) {
            this.plugin = plugin;
            this.uniqueID = UUID.randomUUID();
            this.location = location;
            this.name = name;
            this.skin = skin;
            init();
        }

        private void init() {

            final MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
            final WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
            final GameProfile gameProfile = new GameProfile(uniqueID, ChatColor.translateAlternateColorCodes('&', name));

            final EntityPlayer entityPlayer = new EntityPlayer(minecraftServer, worldServer, gameProfile);
            this.entityPlayer = entityPlayer;
            entityPlayer.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            loadSkin();
            setEquipment(EnumItemSlot.a, new ItemStack(Material.DIAMOND_AXE));

            Bukkit.getOnlinePlayers().forEach(this::sendPacket);

        }

        @Override
        public void loadSkin() {
            entityPlayer.getBukkitEntity().getProfile().getProperties().clear();
            final String[] textures = getTexture();
            entityPlayer.getBukkitEntity().getProfile().getProperties().put("textures", new Property("textures", textures[0], textures[1]));
        }

        @SneakyThrows
        private String[] getTexture() {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidParser(skin) + "?unsigned=false");
                    InputStreamReader reader = new InputStreamReader(url.openStream());
                    JsonObject textureProperty = new JsonParser().parse(reader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
                    String texture = textureProperty.get("value").getAsString();
                    String signature = textureProperty.get("signature").getAsString();
                    return new String[]{texture, signature};
                }catch (Exception e) {
                    e.printStackTrace();
                }
                return new String[]{};
            }).get();
        }

        @Override
        public void setLocation(Location location) {
            this.location = location;
            entityPlayer.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }

        @Override
        public void setSkin(String skinName) {
            this.skin = skinName;
            loadSkin();
        }

        @Override
        public void setEquipment(EnumItemSlot enumItemSlot, ItemStack itemStack) {
            equipmentList.add(new Pair<>(enumItemSlot, CraftItemStack.asNMSCopy(itemStack)));
        }

        private String uuidParser(String playerName) throws Exception {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            return new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
        }

        @Override
        public void sendPacket(Player player) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, entityPlayer));
            connection.a(new PacketPlayOutNamedEntitySpawn(entityPlayer));
            connection.a(new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) (entityPlayer.y * 256 / 360)));
            if (!equipmentList.isEmpty()) connection.a(new PacketPlayOutEntityEquipment(entityPlayer.ae(), equipmentList));
            Bukkit.getScheduler().runTaskLater(plugin, () ->{
                connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, entityPlayer));
            }, 10L);
        }

        @Override
        public void removePacket(Player player) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, entityPlayer));
            connection.a(new PacketPlayOutEntityDestroy(entityPlayer.ae()));
        }

        @Override
        public void reloadNPC(Player player) {
            removePacket(player);
            Bukkit.getScheduler().runTaskLater(plugin, () -> sendPacket(player), 20L);
        }

    }

}
