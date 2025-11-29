package com.neostats.neostats.commands;

import com.neostats.neostats.NeoStats;
import com.neostats.neostats.managers.StatsManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TopStatsCommand implements CommandExecutor {
    private final NeoStats plugin;

    public TopStatsCommand(NeoStats plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эта команда только для игроков!");
            return true;
        }

        Player player = (Player) sender;
        String type = "kills";

        if (args.length > 0) {
            type = args[0].toLowerCase();
        }

        List<StatsManager.PlayerStats> topStats = plugin.getStatsManager().getTopStats(type, 10);

        String title = getTitleByType(type);
        player.sendMessage(ChatColor.GOLD + "=== Топ-10 " + title + " ===");

        for (int i = 0; i < topStats.size(); i++) {
            StatsManager.PlayerStats stats = topStats.get(i);
            String value = getValueByType(stats, type);
            player.sendMessage(ChatColor.YELLOW + "" + (i + 1) + ". " +
                    ChatColor.WHITE + stats.getPlayerName() +
                    ChatColor.GRAY + " - " +
                    ChatColor.GREEN + value);
        }

        return true;
    }

    private String getTitleByType(String type) {
        switch (type) {
            case "kills": return "по убийствам";
            case "blocks": return "по блокам";
            case "time": return "по времени игры";
            case "kd": return "по K/D";
            default: return "по убийствам";
        }
    }

    private String getValueByType(StatsManager.PlayerStats stats, String type) {
        switch (type) {
            case "kills": return String.valueOf(stats.getKills());
            case "blocks": return String.valueOf(stats.getBlocksBroken());
            case "time": return stats.getPlayTime() + " мин";
            case "kd": return String.format("%.2f", stats.getKD());
            default: return String.valueOf(stats.getKills());
        }
    }
}