package fr.robotv2.robotgui.item;

import fr.robotv2.robotgui.BukkitUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ItemBuilder {

    private Material type;
    private int amount;
    private int durability = 0;

    private ItemMeta meta = new ItemStack(Material.GRASS).getItemMeta();

    public ItemBuilder setType(Material type) {
        this.type = type;
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder setName(String name) {
        if(name == null) return this;
        this.meta.setDisplayName(BukkitUtil.withColor(name));
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        this.meta.setLore(Arrays.stream(lore).map(BukkitUtil::withColor).collect(Collectors.toList()));
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.meta.setLore(lore.stream().map(BukkitUtil::withColor).collect(Collectors.toList()));
        return this;
    }

    public ItemBuilder setKey(String keyStr, String value) {
        NamespacedKey key = new NamespacedKey(JavaPlugin.getProvidingPlugin(ItemBuilder.class), keyStr);
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
        return this;
    }

    public ItemBuilder setKey(String keyStr, double value) {
        NamespacedKey key = new NamespacedKey(JavaPlugin.getProvidingPlugin(ItemBuilder.class), keyStr);
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
        return this;
    }

    public ItemBuilder setKey(String keyStr, int value) {
        NamespacedKey key = new NamespacedKey(JavaPlugin.getProvidingPlugin(ItemBuilder.class), keyStr);
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
        return this;
    }

    public ItemBuilder setKey(String keyStr, float value) {
        NamespacedKey key = new NamespacedKey(JavaPlugin.getProvidingPlugin(ItemBuilder.class), keyStr);
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.FLOAT, value);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchant, int level, boolean ignoreLevelRestriction) {
        this.meta.addEnchant(enchant, level, ignoreLevelRestriction);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder addFlags(ItemFlag... flags) {
        this.meta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder setCustomModelData(int model) {
        this.meta.setCustomModelData(model);
        return this;
    }

    public ItemBuilder setDurability(int durability) {
        this.durability = durability;
        return this;
    }

    public ItemBuilder setMeta(ItemMeta meta) {
        this.meta = meta;
        return this;
    }

    public ItemStack build() {

        if(this.type == null) {
            type = Material.AIR;
        }

        if(this.amount <= 0) {
            amount = 1;
        }

        final ItemStack item = new ItemStack(type, amount);
        item.setItemMeta(meta);
        item.setDurability((short) durability);

        return item;
    }
}
