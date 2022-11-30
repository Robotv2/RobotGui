package fr.robotv2.robotgui;

import fr.robotv2.robotgui.ui.GuiHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class RobotGui {
    public static GuiHandler create(JavaPlugin plugin) {
        return new GuiHandler(plugin);
    }
}
