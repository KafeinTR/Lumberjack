package me.kafein.lumberjack.storage;

import com.google.common.base.Enums;
import me.kafein.lumberjack.LumberjackPluginAPI;
import me.kafein.lumberjack.config.Config;
import me.kafein.lumberjack.config.ConfigType;
import me.kafein.lumberjack.storage.impl.MariaDB;

import java.util.Locale;

public class StorageFactory {

    private Storage storage;

    public void loadStorage() {
        final Config config = LumberjackPluginAPI.get().getConfigStore().getConfig(ConfigType.settings);
        final String storageTypeName = config.getString("settings.storage.type").toUpperCase(Locale.ENGLISH);
        final StorageType storageType = Enums.getIfPresent(StorageType.class, storageTypeName).isPresent()
                ? StorageType.valueOf(storageTypeName)
                : StorageType.CUSTOM;
        if (storageType == StorageType.MARIADB) storage = new MariaDB();
    }

    public void setStorage(final Storage storage) {
        this.storage = storage;
    }

    public Storage getStorage() {
        return storage;
    }

}
