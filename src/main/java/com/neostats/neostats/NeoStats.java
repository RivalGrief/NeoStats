package com.neostats.neostats;

import com.neostats.neostats.listeners.PlayerStatsListener;
import com.neostats.neostats.managers.StatsManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NeoStats extends JavaPlugin {
    private StatsManager statsManager;

    @Override
    public void onEnable() {
        // Создаем менеджер статистики
        this.statsManager = new StatsManager(this);

        // Регистрируем команды
        getCommand("stats").setExecutor(new com.neostats.neostats.commands.StatsCommand(this));
        getCommand("topstats").setExecutor(new com.neostats.neostats.commands.TopStatsCommand(this));

        // Регистрируем слушатели событий
        getServer().getPluginManager().registerEvents(new PlayerStatsListener(this), this);

        // Сохраняем конфиг по умолчанию
        saveDefaultConfig();

        getLogger().info("NeoStats успешно запущен!");
    }

    @Override
    public void onDisable() {
        statsManager.saveAllStats();
        getLogger().info("NeoStats успешно выключен!");
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }
}