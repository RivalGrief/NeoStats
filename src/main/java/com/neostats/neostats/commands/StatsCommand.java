package com.neostats.neostats.commands;

import com.neostats.neostats.NeoStats;
import com.neostats.neostats.managers.StatsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {
    private final NeoStats plugin;

    public StatsCommand(NeoStats plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эта команда только для игроков!");
            return true;
        }

        Player player = (Player) sender;
        Player targetPlayer = player;

        if (args.length > 0 && player.hasPermission("neostats.others")) {
            targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                player.sendMessage(ChatColor.RED + "Игрок не найден!");
                return true;
            }
        }

        StatsManager.PlayerStats stats = plugin.getStatsManager().getPlayerStats(targetPlayer);

        player.sendMessage(ChatColor.GOLD + "=== Статистика " + targetPlayer.getName() + " ===");
        player.sendMessage(ChatColor.GREEN + "Убийств: " + ChatColor.WHITE + stats.getKills());
        player.sendMessage(ChatColor.GREEN + "Смертей: " + ChatColor.WHITE + stats.getDeaths());
        player.sendMessage(ChatColor.GREEN + "K/D: " + ChatColor.WHITE + String.format("%.2f", stats.getKD()));
        player.sendMessage(ChatColor.GREEN + "Мобов убито: " + ChatColor.WHITE + stats.getMobKills());
        player.sendMessage(ChatColor.GREEN + "Блоков сломано: " + ChatColor.WHITE + stats.getBlocksBroken());
        player.sendMessage(ChatColor.GREEN + "Блоков поставлено: " + ChatColor.WHITE + stats.getBlocksPlaced());
        player.sendMessage(ChatColor.GREEN + "Время игры: " + ChatColor.WHITE + stats.getPlayTime() + " мин");
        player.sendMessage(ChatColor.GREEN + "Входов на сервер: " + ChatColor.WHITE + stats.getJoinCount());

        return true;
    }
}