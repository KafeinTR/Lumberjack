package me.kafein.lumberjack.config;

import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public interface Config {

    /**
     * @param plugin the plugin
     * @param filePath the file path
     * @return the config
     */
    @NotNull
    static Config create(final Plugin plugin, final String filePath) {
        return new Impl(plugin, filePath);
    }

    /**
     * @param plugin the plugin
     * @param filePath the file path
     * @return the config
     */
    @NotNull
    static Config create(final Plugin plugin, final String filePath, final boolean saveResource) {
        return new Impl(plugin, filePath, saveResource);
    }

    /**
     * @param file the file
     * @return the config
     */
    @NotNull
    static Config create(final File file) {
        return new Impl(file);
    }

    /**
     * @param property the property
     * @return the get by property object
     */
    @NotNull
    Object get(final String property);

    /**
     * @param property the property
     * @return the get by property text list
     */
    @NotNull
    List<String> getStringList(final String property);

    /**
     * @param property the property
     * @return the get by property int list
     */
    @NotNull
    List<Integer> getIntegerList(final String property);

    /**
     * @param property the property
     * @return the get by property section
     */
    @NotNull
    ConfigurationSection getConfigurationSection(final String property);

    /**
     * @param property the property
     * @return the get by property text
     */
    @NotNull
    String getString(final String property);

    /**
     * @param property the property
     * @return the get by property int
     */
    int getInteger(final String property);

    /**
     * @param property the property
     * @return the get by property long
     */
    long getLong(final String property);

    /**
     * @param property the property
     * @return the get by property double
     */
    double getDouble(final String property);

    /**
     * @param property the property
     * @return the get by property boolean
     */
    boolean getBoolean(final String property);

    /**
     * @param property the property
     * @return the get by property is int
     */
    boolean isInt(final String property);

    /**
     * @param property the property
     * @return contains property
     */
    boolean containsProperty(final String property);

    class Impl implements Config {

        final private FileConfiguration fileConfiguration;

        public Impl(final Plugin plugin, final String filePath) {
            final File file = new File(plugin.getDataFolder(), filePath);
            if (!file.exists()) throw new NullPointerException("File is not exists!");
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
        }

        public Impl(final Plugin plugin, final String filePath, final boolean saveResource) {
            final File file = new File(plugin.getDataFolder(), filePath);
            if (!file.exists()) plugin.saveResource(filePath, true);
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
        }

        public Impl(final File file) {
            if (!file.exists()) throw new NullPointerException("File is not exists!");
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
        }

        @Override
        public @NonNull List<String> getStringList(String property) {
            return fileConfiguration.getStringList(property);
        }

        @Override
        public @NonNull List<Integer> getIntegerList(String property) {
            return fileConfiguration.getIntegerList(property);
        }

        @Override
        public @NonNull ConfigurationSection getConfigurationSection(String property) {
            return fileConfiguration.getConfigurationSection(property);
        }

        @Override
        public @NonNull Object get(String property) {
            return fileConfiguration.get(property);
        }

        @Override
        public @NonNull String getString(String property) {
            final String text = fileConfiguration.getString(property);
            return text == null ? "?" : text;
        }

        @Override
        public int getInteger(String property) {
            return fileConfiguration.getInt(property);
        }

        @Override
        public long getLong(String property) {
            return fileConfiguration.getLong(property);
        }

        @Override
        public double getDouble(String property) {
            return fileConfiguration.getDouble(property);
        }

        @Override
        public boolean getBoolean(String property) {
            return fileConfiguration.getBoolean(property);
        }

        @Override
        public boolean isInt(String property) {
            return fileConfiguration.isInt(property);
        }

        @Override
        public boolean containsProperty(String property) {
            return fileConfiguration.contains(property);
        }

    }

}
