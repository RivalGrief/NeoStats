package com.neostats.neostats.managers;

import com.neostats.neostats.NeoStats;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class StatsManager {
    private final NeoStats plugin;
    private final Map<UUID, PlayerStats> playerStats;
    private final File dataFolder;

    public StatsManager(NeoStats plugin) {
        this.plugin = plugin;
        this.playerStats = new HashMap<>();
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

    public static class PlayerStats {
        private UUID playerId;
        private String playerName;
        private int kills;
        private int deaths;
        private int blocksBroken;
        private int blocksPlaced;
        private int mobKills;
        private int playTime; // в минутах
        private int joinCount;

        public PlayerStats(UUID playerId, String playerName) {
            this.playerId = playerId;
            this.playerName = playerName;
            this.kills = 0;
            this.deaths = 0;
            this.blocksBroken = 0;
            this.blocksPlaced = 0;
            this.mobKills = 0;
            this.playTime = 0;
            this.joinCount = 0;
        }

        // Геттеры и сеттеры
        public UUID getPlayerId() { return playerId; }
        public String getPlayerName() { return playerName; }
        public int getKills() { return kills; }
        public void addKill() { this.kills++; }
        public int getDeaths() { return deaths; }
        public void addDeath() { this.deaths++; }
        public int getBlocksBroken() { return blocksBroken; }
        public void addBlocksBroken(int amount) { this.blocksBroken += amount; }
        public int getBlocksPlaced() { return blocksPlaced; }
        public void addBlocksPlaced(int amount) { this.blocksPlaced += amount; }
        public int getMobKills() { return mobKills; }
        public void addMobKill() { this.mobKills++; }
        public int getPlayTime() { return playTime; }
        public void addPlayTime(int minutes) { this.playTime += minutes; }
        public int getJoinCount() { return joinCount; }
        public void addJoin() { this.joinCount++; }

        public double getKD() {
            return deaths == 0 ? kills : (double) kills / deaths;
        }
    }

    public PlayerStats getPlayerStats(Player player) {
        return playerStats.computeIfAbsent(player.getUniqueId(),
                uuid -> loadPlayerStats(player));
    }

    private PlayerStats loadPlayerStats(Player player) {
        File playerFile = getPlayerFile(player.getUniqueId());
        if (playerFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
            PlayerStats stats = new PlayerStats(player.getUniqueId(), player.getName());
            stats.kills = config.getInt("kills", 0);
            stats.deaths = config.getInt("deaths", 0);
            stats.blocksBroken = config.getInt("blocksBroken", 0);
            stats.blocksPlaced = config.getInt("blocksPlaced", 0);
            stats.mobKills = config.getInt("mobKills", 0);
            stats.playTime = config.getInt("playTime", 0);
            stats.joinCount = config.getInt("joinCount", 0);
            return stats;
        }
        return new PlayerStats(player.getUniqueId(), player.getName());
    }

    public void savePlayerStats(Player player) {
        PlayerStats stats = playerStats.get(player.getUniqueId());
        if (stats != null) {
            File playerFile = getPlayerFile(player.getUniqueId());
            FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

            config.set("playerName", stats.getPlayerName());
            config.set("kills", stats.getKills());
            config.set("deaths", stats.getDeaths());
            config.set("blocksBroken", stats.getBlocksBroken());
            config.set("blocksPlaced", stats.getBlocksPlaced());
            config.set("mobKills", stats.getMobKills());
            config.set("playTime", stats.getPlayTime());
            config.set("joinCount", stats.getJoinCount());

            try {
                config.save(playerFile);
            } catch (IOException e) {
                plugin.getLogger().warning("Не удалось сохранить статистику для " + player.getName());
            }
        }
    }

    public void saveAllStats() {
        for (UUID playerId : playerStats.keySet()) {
            Player player = plugin.getServer().getPlayer(playerId);
            if (player != null) {
                savePlayerStats(player);
            }
        }
    }

    private File getPlayerFile(UUID playerId) {
        return new File(dataFolder, playerId.toString() + ".yml");
    }

    public List<PlayerStats> getTopStats(String type, int limit) {
        List<PlayerStats> allStats = new ArrayList<>(playerStats.values());

        switch (type.toLowerCase()) {
            case "kills":
                allStats.sort((a, b) -> Integer.compare(b.getKills(), a.getKills()));
                break;
            case "blocks":
                allStats.sort((a, b) -> Integer.compare(b.getBlocksBroken(), a.getBlocksBroken()));
                break;
            case "time":
                allStats.sort((a, b) -> Integer.compare(b.getPlayTime(), a.getPlayTime()));
                break;
            case "kd":
                allStats.sort((a, b) -> Double.compare(b.getKD(), a.getKD()));
                break;
            default:
                allStats.sort((a, b) -> Integer.compare(b.getKills(), a.getKills()));
        }

        return allStats.subList(0, Math.min(limit, allStats.size()));
    }
}