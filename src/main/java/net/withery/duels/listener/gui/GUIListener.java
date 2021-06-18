package net.withery.duels.listener.gui;

import net.withery.duels.gui.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTopInventory() instanceof GUI gui)) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack item = event.getCurrentItem();
        int slot = event.getSlot();
        ClickType clickType = event.getClick();

        if (item == null && event.getClickedInventory() == event.getView().getTopInventory())
            event.setCancelled(gui.onClickNothing(player, slot, clickType));

        else if (item != null && event.getClickedInventory() == event.getView().getTopInventory())
            event.setCancelled(gui.onClickItem(player, item, slot, clickType));

        else
            event.setCancelled(gui.onClickOutside(player, clickType));
    }

}