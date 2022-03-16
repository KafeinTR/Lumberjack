package me.kafein.lumberjack.command;

import me.kafein.lumberjack.LumberjackPluginAPI;
import me.kafein.lumberjack.config.Config;
import me.kafein.lumberjack.config.ConfigStore;
import me.kafein.lumberjack.config.ConfigType;
import me.kafein.lumberjack.lumberjack.LumberjackStore;
import me.kafein.lumberjack.npc.NPC;
import me.kafein.lumberjack.npc.NPCManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class LumberjackCMD implements TabExecutor {

    final private ConfigStore configStore = LumberjackPluginAPI.get().getConfigStore();
    final private LumberjackStore lumberjackStore = LumberjackPluginAPI.get().getLumberjackStore();
    final private NPCManager npcManager = LumberjackPluginAPI.get().getNpcManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return true;

        final Player player = (Player) sender;

        final Config languageConfig = configStore.getConfig(ConfigType.language);

        if (!player.isOp()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getString("language.noPerm")));
            return true;
        }

        if (args.length == 0) {
            sendHelpMessage(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("npc")) {

            if (args.length < 2) {
                sendHelpMessage(player);
                return true;
            }

            if (args[1].equalsIgnoreCase("create")) {

                if (args.length < 3) {
                    sendHelpMessage(player);
                    return true;
                }

                if (args.length > 3) {
                    npcManager.createNPC(player.getLocation(), args[2], args[3]);
                } else {
                    npcManager.createNPC(player.getLocation(), args[2]);
                }

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getString("language.npc.created")));

            }else if (args[1].equalsIgnoreCase("delete")) {

                if (args.length < 3) {
                    sendHelpMessage(player);
                    return true;
                }

                final String npcName = args[2];

                for (NPC npc : npcManager.getStore().getNPCCollection()) {
                    if (!npc.getName().equalsIgnoreCase(npcName)) continue;
                    npcManager.deleteNPC(npc.getUniqueID());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getString("language.npc.deleted")));
                    return true;
                }

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getString("language.npc.notFound")));

            }else if (args[1].equalsIgnoreCase("set")) {

                if (args.length < 3) {
                    sendHelpMessage(player);
                    return true;
                }

                final String npcName = args[2];

                for (NPC npc : npcManager.getStore().getNPCCollection()) {
                    if (!npc.getName().equalsIgnoreCase(npcName)) continue;
                    npc.setLocation(player.getLocation());
                    Bukkit.getOnlinePlayers().forEach(npc::reloadNPC);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getString("language.npc.setted")));
                    return true;
                }

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', languageConfig.getString("language.npc.notFound")));

            }else {
                sendHelpMessage(player);
            }

        }

        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) return null;
        if (args.length == 1) return Arrays.asList("npc");
        if (args.length == 2 && args[0].equalsIgnoreCase("npc")) return Arrays.asList("create", "delete", "set");
        if (args.length == 3 && args[0].equalsIgnoreCase("npc")) return Arrays.asList("<name>");
        if (args.length == 4 && args[0].equalsIgnoreCase("npc") && args[1].equalsIgnoreCase("create")) return Arrays.asList("<skin>");
        return null;
    }

    private void sendHelpMessage(final Player player) {
        configStore.getConfig(ConfigType.language).getStringList("language.help").forEach(message -> player.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
    }

}
