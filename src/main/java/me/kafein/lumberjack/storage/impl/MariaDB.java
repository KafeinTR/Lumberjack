package me.kafein.lumberjack.storage.impl;

import lombok.NonNull;
import lombok.SneakyThrows;
import me.kafein.lumberjack.LumberjackPluginAPI;
import me.kafein.lumberjack.config.Config;
import me.kafein.lumberjack.config.ConfigStore;
import me.kafein.lumberjack.config.ConfigType;
import me.kafein.lumberjack.lumberjack.Lumberjack;
import me.kafein.lumberjack.storage.Storage;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MariaDB implements Storage {

    final private ConfigStore configStore = LumberjackPluginAPI.get().getConfigStore();

    private Connection connection;

    public MariaDB() {

        final Config settingsConfig =configStore.getConfig(ConfigType.settings);

        try {

            Class.forName("org.mariadb.jdbc.Driver");

            connection = DriverManager.getConnection(
                    "jdbc:mariadb://" + settingsConfig.getString("settings.storage.maria.ip") + "/" +
                    settingsConfig.getString("settings.storage.maria.database"),
                    settingsConfig.getString("settings.storage.maria.user"),
                    settingsConfig.getString("settings.storage.maria.password"));

            connection.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS lumberjacks(" +
                    "uuid VARCHAR(255) NOT NULL," +
                    "exp VARCHAR(255) NOT NULL," +
                    "PRIMARY KEY (uuid)" +
                    ");");

        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @SneakyThrows
    @Override
    public @NonNull Lumberjack load(UUID playerUUID) {

        return CompletableFuture.supplyAsync(() -> {

            final Lumberjack lumberjack = Lumberjack.create(playerUUID);

            PreparedStatement ps;
            ResultSet rs;

            try {

                ps = connection.prepareStatement("SELECT * FROM lumberjacks WHERE uuid = ?");
                ps.setString(1, playerUUID.toString());

                rs = ps.executeQuery();

                if (rs.next()) {

                    lumberjack.setExp(rs.getLong("exp"));

                }else {

                    ps = connection.prepareStatement("INSERT INTO lumberjacks(uuid,exp) VALUES(?,?)");
                    ps.setString(1, playerUUID.toString());
                    ps.setLong(2, 0L);

                    ps.executeUpdate();

                }

                rs.close();
                ps.close();

            }catch (SQLException e) {
                e.printStackTrace();
            }

            return lumberjack;

        }).get();

    }

    @Override
    public void save(Lumberjack lumberjack) {

        CompletableFuture.runAsync(() -> {

            PreparedStatement ps;

            try {

                ps = connection.prepareStatement("UPDATE lumberjacks SET exp = ? WHERE uuid = ?");
                ps.setLong(1, lumberjack.getExp());
                ps.setString(2, lumberjack.getPlayerUUID().toString());

                ps.executeUpdate();
                ps.close();

            }catch (SQLException e) {
                e.printStackTrace();
            }

        });

    }

    @SneakyThrows
    @Override
    public boolean contains(UUID playerUUID) {
        return CompletableFuture.supplyAsync(() -> {

            PreparedStatement ps;
            ResultSet rs;

            try {

                ps = connection.prepareStatement("SELECT * FROM lumberjacks WHERE uuid = ?");
                ps.setString(1, playerUUID.toString());

                rs = ps.executeQuery();

                return rs.next();

            }catch (SQLException e) {
                e.printStackTrace();
            }

            return false;

        }).get();
    }

}
