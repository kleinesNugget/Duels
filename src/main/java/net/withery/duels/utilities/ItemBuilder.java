package net.withery.duels.utilities;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        itemStack = new ItemStack(material);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder setName(String name) {
        if (itemMeta != null)
            itemMeta.setDisplayName(name);

        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        if (itemMeta != null)
            itemMeta.setLore(lore);

        return this;
    }

    public ItemBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    public ItemBuilder addLoreLine(String line) {
        if (itemMeta != null) {
            List<String> lore = itemMeta.getLore();
            if (lore == null)
                lore = new ArrayList<>();

            lore.add(line);
            itemMeta.setLore(lore);
        }

        return this;
    }

    public ItemBuilder removeLore() {
        if (itemMeta != null)
            itemMeta.setLore(Collections.emptyList());

        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        if (itemMeta != null)
            itemMeta.setUnbreakable(true);

        return this;
    }

    public ItemBuilder hideEnchantments() {
        if (itemMeta != null)
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        return this;
    }

    public ItemBuilder hideAttributes() {
        if (itemMeta != null)
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        return this;
    }

    public ItemBuilder hideUnbreakable() {
        if (itemMeta != null)
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        return this;
    }

    public ItemStack build() {
        if (itemMeta != null)
            itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}