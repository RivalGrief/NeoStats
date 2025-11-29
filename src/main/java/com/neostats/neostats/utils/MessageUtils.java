package com.neostats.neostats.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MessageUtils {

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendConfigMessage(Player player, FileConfiguration config, String path) {
        if (config.contains(path)) {
            if (config.isList(path)) {
                for (String line : config.getStringList(path)) {
                    player.sendMessage(color(line));
                }
            } else {
                player.sendMessage(color(config.getString(path, "")));
            }
        }
    }

    public static String formatTime(int minutes) {
        int hours = minutes / 60;
        int days = hours / 24;

        if (days > 0) {
            return days + "д " + (hours % 24) + "ч";
        } else if (hours > 0) {
            return hours + "ч " + (minutes % 60) + "м";
        } else {
            return minutes + "м";
        }
    }
}