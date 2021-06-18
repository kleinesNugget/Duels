package net.withery.duels.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface GUI extends InventoryHolder {

    void open();

    void close();

    void update();

    default boolean onClickItem(Player player, ItemStack item, int slot, ClickType clickType) {
        return true;
    }

    default boolean onClickNothing(Player player, int slot, ClickType clickType) {
        return true;
    }

    default boolean onClickOutside(Player player, ClickType clickType) {
        return true;
    }

}