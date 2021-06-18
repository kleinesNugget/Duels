package net.withery.duels.kit;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Equipment {

    private final ItemStack[] armor = new ItemStack[4];
    private final ItemStack[] inventory = new ItemStack[36];
    private ItemStack offhand = new ItemStack(Material.AIR);

    public Equipment setHelmet(ItemStack item) {
        armor[3] = item;
        return this;
    }

    public Equipment setChestplate(ItemStack item) {
        armor[2] = item;
        return this;
    }

    public Equipment setLeggings(ItemStack item) {
        armor[1] = item;
        return this;
    }

    public Equipment setBoots(ItemStack item) {
        armor[0] = item;
        return this;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public Equipment setInventoryItem(int slot, ItemStack item) {
        if (slot < 0 || slot > 35) return this;

        inventory[slot] = item;
        return this;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public Equipment setOffhand(ItemStack item) {
        offhand = item;
        return this;
    }

    public ItemStack getOffhand() {
        return offhand;
    }

}