package me.kafein.lumberjack.lumberjack;

import lombok.NonNull;
import me.kafein.lumberjack.util.level.LevelSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public interface Lumberjack {

    /**
     * Starts from 0 exp.
     *
     * @param playerUUID the player id
     * @return lumberjack to be set
     */
    @NotNull
    static Lumberjack create(final UUID playerUUID) {
        return new Impl(playerUUID);
    }

    /**
     * Starts from selected exp.
     *
     * @param playerUUID the player id
     * @param exp the exp for lumberjack
     * @return lumberjack to be set
     */
    @NotNull
    static Lumberjack create(final UUID playerUUID, final long exp) {
        return new Impl(playerUUID, exp);
    }

    /**
     * Starts from selected level's exp.
     *
     * @param playerUUID the player id
     * @param level the level for lumberjack
     * @return lumberjack to be set
     */
    @NotNull
    static Lumberjack create(final UUID playerUUID, final int level) {
        return new Impl(playerUUID, level);
    }

    /**
     * @return the player id
     */
    @NotNull
    UUID getPlayerUUID();

    /**
     * @return the player
     */
    @Nullable
    Player getPlayer();

    /**
     * @return the player's lumberjack level
     */
    int getLevel();

    /**
     * @return the player's lumberjack exp
     */
    long getExp();

    /**
     * @return Is the player's knockdown skill ready
     */
    boolean isSkillReady();

    /**
     * if player used the knockdown skill
     *
     * @return the player's skill duration
     */
    int getSkillDuration();

    /**
     * set the player's lumberjack level
     * exp will be adjusted according to the level you set
     * @param value the level to be adjusted
     */
    void setLevel(final int value);

    /**
     * add level to the player's lumberjack level
     * @param value level to be added
     */
    void addLevel(final int value);

    /**
     * remove level from the player's lumberjack level
     * @param value level to be removed
     */
    void removeLevel(final int value);

    /**
     * set the player's lumberjack exp
     * level will be adjusted according to the exp you set
     * @param value the exp to be adjusted
     */
    void setExp(final long value);

    /**
     * add exp to the player's lumberjack exp
     * @param value exp to be added
     */
    void addExp(final long value);

    /**
     * remove exp from the player's lumberjack exp
     * @param value exp to be removed
     */
    void removeExp(final long value);

    /**
     * set the player's knockdown skill duration
     * @param value the duration to be adjusted
     */
    void setSkillDuration(final int value);

    class Impl implements Lumberjack {

        private int skillDuration;
        private long exp;

        final private UUID playerUUID;

        public Impl(final UUID playerUUID) {
            this.playerUUID = playerUUID;
        }

        public Impl(final UUID playerUUID, final long exp) {
            this.playerUUID = playerUUID;
            this.exp = exp;
        }

        public Impl(final UUID playerUUID, final int level) {
            this.playerUUID = playerUUID;
            this.exp = LevelSerializer.getInstance().deserialize(level);
        }

        @Override
        public @NonNull UUID getPlayerUUID() {
            return playerUUID;
        }

        @Override
        public Player getPlayer() {
            return Bukkit.getPlayer(playerUUID);
        }

        @Override
        public int getLevel() {
            return LevelSerializer.getInstance().serialize(exp);
        }

        @Override
        public long getExp() {
            return exp;
        }

        @Override
        public boolean isSkillReady() {
            return skillDuration <= 0;
        }

        @Override
        public int getSkillDuration() {
            return skillDuration;
        }

        @Override
        public void setLevel(int value) {
            this.exp = LevelSerializer.getInstance().deserialize(value);
        }

        @Override
        public void addLevel(int value) {
            this.exp += LevelSerializer.getInstance().deserialize(value);
        }

        @Override
        public void removeLevel(int value) {
            final long serializedExp = LevelSerializer.getInstance().deserialize(value);
            if (serializedExp > exp) return;
            this.exp -= serializedExp;
        }

        @Override
        public void setExp(long value) {
            this.exp = value;
        }

        @Override
        public void addExp(long value) {
            this.exp += value;
        }

        @Override
        public void removeExp(long value) {
            this.exp -= value;
        }

        @Override
        public void setSkillDuration(int value) {
            this.skillDuration = value;
        }

    }

}
