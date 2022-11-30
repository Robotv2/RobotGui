package fr.robotv2.robotgui;

import org.bukkit.ChatColor;

public class BukkitUtil {
    public static String withColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
