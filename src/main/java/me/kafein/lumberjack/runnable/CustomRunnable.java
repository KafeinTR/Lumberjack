package me.kafein.lumberjack.runnable;

import org.bukkit.plugin.Plugin;

public abstract class CustomRunnable implements Runnable {

    public abstract void start(final Plugin plugin);

}
