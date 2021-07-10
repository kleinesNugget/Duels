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
        if (!(event.getView().getTopInventory().getHolder() instanceof GUI gui)) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;

        ItemStack item = event.getCurrentItem();
        int slot = event.getSlot();
        ClickType clickType = event.getClick();

        if (item != null && event.getClickedInventory() == event.getView().getTopInventory())
            event.setCancelled(gui.onClickGUIItem(player, item, slot, clickType));

        else if (item == null && event.getClickedInventory() == event.getView().getTopInventory())
            event.setCancelled(gui.onClickGUINothing(player, slot, clickType));

        else if (item != null && event.getClickedInventory() == event.getView().getBottomInventory())
            event.setCancelled(gui.onClickBottomItem(player, item, slot, clickType));

        else if (item == null && event.getClickedInventory() == event.getView().getBottomInventory())
            event.setCancelled(gui.onClickBottomNothing(player, slot, clickType));

        else if (event.getClickedInventory() == null)
            event.setCancelled(gui.onClickOutside(player, clickType));

        if (event.isCancelled())
            player.updateInventory();
    }

}