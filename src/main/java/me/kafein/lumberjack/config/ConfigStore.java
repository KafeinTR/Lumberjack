package me.kafein.lumberjack.config;

import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public interface ConfigStore {

    /**
     * @param plugin the plugin
     * @return created config store
     */
    @NotNull
    static ConfigStore create(final Plugin plugin) {
        return new Impl(plugin);
    }

    /**
     * puts the config
     *
     * @param configType the config type
     * @param config the config
     */
    void put(final ConfigType configType, final Config config);

    /**
     * puts the config
     *
     * @param configType the config type
     * @param config the config
     */
    void put(final String configType, final Config config);

    /**
     * remove the config
     *
     * @param configType the config type
     */
    void remove(final String configType);

    /**
     * @param configType the config type
     * @return contains config
     */
    boolean contains(final String configType);

    /**
     * @param configType the config type
     * @return the config
     */
    @NotNull
    Optional<Config> getConfig(final String configType);

    /**
     * @param configType the config type
     * @return the config
     */
    @NotNull
    Config getConfig(final ConfigType configType);

    /**
     * resource file properties clone to file
     *
     * @param file the file
     * @param resource the resource
     */
    void copyResource(final File file, final String resource) throws IOException;

    class Impl implements ConfigStore {

        final private Map<String, Config> configMap = new ConcurrentHashMap<>();

        final private Plugin plugin;

        public Impl(final Plugin plugin) {
            this.plugin = plugin;

            final Config settings = Config.create(plugin, "settings.yml", true);
            put(ConfigType.settings, settings);

            Config language = Config.create(plugin, "language.yml", true);
            put(ConfigType.language, language);

        }

        @Override
        public void put(ConfigType configType, Config config) {
            configMap.put(configType.name(), config);
        }

        @Override
        public void put(String configType, Config config) {
            configMap.put(configType, config);
        }

        @Override
        public void remove(String configType) {
            configMap.remove(configType);
        }

        @Override
        public boolean contains(String configType) {
            return configMap.containsKey(configType);
        }

        @Override
        public @NonNull Optional<Config> getConfig(String configType) {
            return Optional.ofNullable(configMap.get(configType));
        }

        @Override
        public @NonNull Config getConfig(ConfigType configType) {
            return configMap.get(configType.name());
        }

        @Override
        public void copyResource(File file, String resource) throws IOException {
            final InputStream in = plugin.getResource(resource);

            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        }

    }


}
