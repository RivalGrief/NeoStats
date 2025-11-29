package com.neostats.neostats.listeners;

import com.neostats.neostats.NeoStats;
import com.neostats.neostats.managers.StatsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerStatsListener implements Listener {
    private final NeoStats plugin;

    public PlayerStatsListener(NeoStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        StatsManager.PlayerStats stats = plugin.getStatsManager().getPlayerStats(player);
        stats.addJoin();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getStatsManager().savePlayerStats(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        StatsManager.PlayerStats stats = plugin.getStatsManager().getPlayerStats(player);
        stats.addDeath();

        // Если убийца - игрок
        if (player.getKiller() != null) {
            Player killer = player.getKiller();
            StatsManager.PlayerStats killerStats = plugin.getStatsManager().getPlayerStats(killer);
            killerStats.addKill();
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            StatsManager.PlayerStats stats = plugin.getStatsManager().getPlayerStats(killer);
            stats.addMobKill();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        StatsManager.PlayerStats stats = plugin.getStatsManager().getPlayerStats(player);
        stats.addBlocksBroken(1);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        StatsManager.PlayerStats stats = plugin.getStatsManager().getPlayerStats(player);
        stats.addBlocksPlaced(1);
    }
}