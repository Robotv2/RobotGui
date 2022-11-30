package fr.robotv2.robotgui.ui;

import fr.robotv2.robotgui.ui.Gui;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class GuiHandler implements Listener {

    private final HashMap<Class<? extends Gui>, Gui> menus = new HashMap<>();

    public GuiHandler(JavaPlugin plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onClick(InventoryClickEvent e) {

        final Player player = (Player) e.getWhoClicked();
        final ItemStack item = e.getCurrentItem();
        final InventoryHolder holder = e.getInventory().getHolder();

        if(item == null) return;
        if(!(holder instanceof Gui)) return;

        e.setCancelled(true);
        ((Gui) holder).onClick(player, e.getInventory(), item, e.getRawSlot(), e.getClick());
    }

    @EventHandler
    private void onClose(InventoryCloseEvent e) {

        final Player player = (Player) e.getPlayer();
        final InventoryHolder holder = e.getInventory().getHolder();

        if(holder instanceof Gui) {
            ((Gui) holder).onClose(player, e);
        }
    }

    public void register(Gui gui) {
        menus.put(gui.getClass(), gui);
    }

    public void open(Player player, Class<? extends Gui> gClass, Object... objects) {

        if(!menus.containsKey(gClass)) {
            throw new IllegalArgumentException("gui not registered");
        }

        final Gui menu = menus.get(gClass);
        final Inventory inv = Bukkit.createInventory(menu, menu.getSize(), ChatColor.translateAlternateColorCodes('&', menu.getName(player, objects)));

        menu.startContents(player, inv, objects);
        player.openInventory(inv);
    }
}
