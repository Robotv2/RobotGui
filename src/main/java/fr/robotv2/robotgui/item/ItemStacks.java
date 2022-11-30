package fr.robotv2.robotgui.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.UnmodifiableView;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ItemStacks {

    private final static HashMap<String, ItemStack> heads = new HashMap<>();

    @UnmodifiableView
    public static Collection<ItemStack> getCachedHeads() {
        return Collections.unmodifiableCollection(heads.values());
    }

    public static ItemStack getHead(UUID playerUUID) {

        if(heads.containsKey(playerUUID.toString())) {
            return heads.get(playerUUID.toString());
        }

        final ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        final SkullMeta meta = (SkullMeta) head.getItemMeta();

        meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerUUID));
        head.setItemMeta(meta);

        heads.put(playerUUID.toString(), head);
        return head;
    }

    public static ItemStack getHead(Player player) {
        return ItemStacks.getHead(player.getUniqueId());
    }

    public static ItemStack createSkull(String url) {

        if(heads.containsKey(url)) {
            return heads.get(url);
        }

        final ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (url.isEmpty()) return head;

        final SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        final GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));

        try {
            final Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }

        head.setItemMeta(headMeta);
        heads.put(url, head);
        return head;
    }

    public static ItemStack fromConfigurationSection(ConfigurationSection section) {

        ItemBuilder builder = new ItemBuilder();
        final String mat = section.getString("material", "STONE");
        final String name = section.getString("name");
        final List<String> lore = section.getStringList("lore");

        if(mat.startsWith("head-")) {
            String url = mat.substring("head-".length());
            builder = ItemStacks.toBuilder(ItemStacks.createSkull(url));
        } else {
            builder.setType(Material.getMaterial(mat));
        }

        return builder.setName(name).setLore(lore).build();
    }

    public static ItemBuilder toBuilder(ItemStack item) {
        ItemBuilder builder = new ItemBuilder();
        builder.setMeta(item.getItemMeta());
        builder.setType(item.getType());
        builder.setAmount(item.getAmount());
        return builder;
    }

    public static boolean hasKey(ItemStack item, String keyStr, PersistentDataType<?, ?> type) {
        final NamespacedKey key = new NamespacedKey(JavaPlugin.getProvidingPlugin(ItemStacks.class), keyStr);
        return item.getItemMeta().getPersistentDataContainer().has(key, type);
    }

    public static <V> V getKeyValue(ItemStack item, String keyStr, PersistentDataType<?, V> type) {
        final NamespacedKey key = new NamespacedKey(JavaPlugin.getProvidingPlugin(ItemStacks.class), keyStr);
        return item.getItemMeta().getPersistentDataContainer().get(key, type);
    }
}
