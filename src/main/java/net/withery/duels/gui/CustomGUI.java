package net.withery.duels.gui;

import net.withery.duels.utilities.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class CustomGUI implements GUI {

    public Inventory inventory;

    public final static ItemStack BACKGROUND = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").hideAttributes().build();
    public final static ItemStack UNUSED_SLOT = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").hideAttributes().build();
    public final static ItemStack BACK = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(ChatColor.RED + "Back").hideAttributes().build();
    public final static ItemStack NEXT = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName(ChatColor.AQUA + "Next Page").hideAttributes().build();
    public final static ItemStack NEXT_INACTIVE = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(ChatColor.GRAY + "Next Page").hideAttributes().build();
    public final static ItemStack PREVIOUS = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName(ChatColor.AQUA + "Previous Page").hideAttributes().build();
    public final static ItemStack PREVIOUS_INACTIVE = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(ChatColor.GRAY + "Previous Page").hideAttributes().build();

    public void fillBackground() {
        if (inventory == null) return;

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, BACKGROUND);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

}