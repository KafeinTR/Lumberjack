package me.kafein.lumberjack.npc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.NonNull;
import lombok.SneakyThrows;
import me.kafein.lumberjack.util.location.LocationSerializer;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface NPCLoader {

    /**
     * @param plugin the plugin
     * @return the craeted loader
     */
    @NotNull
    static NPCLoader getInstance(final Plugin plugin) {
        return new Impl(plugin);
    }

    /**
     * load npc list from storage
     * @return the loaded npc list
     */
    @NotNull
    List<NPC> load();

    /**
     * save the all npc
     * @param npcCollection the npc collection
     */
    void save(final Collection<NPC> npcCollection);

    /**
     * delete the npc
     * @param uuid the npc id
     */
    void delete(final UUID uuid);

    class Impl implements NPCLoader {

        final private Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final private LocationSerializer locationSerializer = LocationSerializer.getInstance();

        final private Plugin plugin;

        public Impl(final Plugin plugin) {
            this.plugin = plugin;
        }

        @SneakyThrows
        @Override
        public @NonNull List<NPC> load() {

            final List<NPC> list = new ArrayList<>();

            final File parentFile = new File(plugin.getDataFolder(), "npc");
            if (!parentFile.exists()) return list;

            final File[] files = parentFile.listFiles();
            if (ArrayUtils.isEmpty(files)) return list;

            for (File file : files) {

                final FileReader fileReader = new FileReader(file);
                final JsonObject jsonObject = gson.fromJson(fileReader, JsonObject.class);
                fileReader.close();

                final NPC npc = NPC.create(
                        plugin,
                        locationSerializer.serialize(jsonObject.get("location").getAsString()),
                        jsonObject.get("name").getAsString(),
                        jsonObject.get("skin").getAsString());

                list.add(npc);

            }

            return list;

        }

        @SneakyThrows
        @Override
        public void save(final Collection<NPC> npcCollection) {

            for (NPC npc : npcCollection) {

                final File file = new File(plugin.getDataFolder(), "npc/" + npc.getUniqueID().toString() + ".json");
                if (!file.exists()) {
                    if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("location", locationSerializer.deserialize(npc.getLocation()));
                jsonObject.put("name", npc.getName());
                jsonObject.put("skin", npc.getSkin());

                final FileWriter fileWriter = new FileWriter(file);
                gson.toJson(jsonObject, fileWriter);
                fileWriter.flush();
                fileWriter.close();

            }

        }

        @Override
        public void delete(UUID uuid) {
            final File file = new File(plugin.getDataFolder(), "npc/" + uuid.toString() + ".json");
            if (file.exists()) file.delete();
        }

    }

}
