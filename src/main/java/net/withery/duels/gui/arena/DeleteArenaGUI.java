package net.withery.duels.gui.arena;

import net.withery.duels.Duels;
import net.withery.duels.arena.Arena;
import net.withery.duels.config.locale.LocaleReference;
import net.withery.duels.gui.CustomGUI;
import net.withery.duels.utilities.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DeleteArenaGUI extends CustomGUI {

    private final static int INVENTORY_SIZE = 9 * 3;

    private final static Set<Integer> CONFIRM_SLOTS = new HashSet<>(Arrays.asList(10, 11, 12));
    private final static Set<Integer> CANCEL_SLOTS = new HashSet<>(Arrays.asList(14, 15, 16));
    private final static int ARENA_SLOT = 13;

    private final Duels plugin;
    private final Player player;
    private final Arena arena;

    public DeleteArenaGUI(Duels plugin, Player player, Arena arena) {
        this.plugin = plugin;
        this.player = player;
        this.arena = arena;

        inventory = Bukkit.createInventory(this, INVENTORY_SIZE, "Delete " + arena.getName() + " Arena");
    }

    @Override
    public void open() {
        fillBackground();

        inventory.setItem(ARENA_SLOT, new ItemBuilder(arena.getArenaStatus().getMaterial())
                .setName(ChatColor.RED + "Delete " + arena.getName()).hideAttributes().build());

        for (int slot : CONFIRM_SLOTS)
            inventory.setItem(slot, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(ChatColor.GREEN + "Confirm").hideAttributes().build());

        for (int slot : CANCEL_SLOTS)
            inventory.setItem(slot, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(ChatColor.RED + "Cancel").hideAttributes().build());
        plugin.getGuiHandler().setGUI(player, this);
        player.openInventory(inventory);
    }

    @Override
    public void close(boolean inventory) {
        plugin.getGuiHandler().removeGUI(player);
        if (inventory)
            player.closeInventory();
    }

    @Override
    public void update() {
        inventory.setItem(ARENA_SLOT, new ItemBuilder(arena.getArenaStatus().getMaterial())
                .setName(ChatColor.RED + "Delete " + arena.getName()).hideAttributes().build());
    }

    @Override
    public boolean onClickGUIItem(Player player, ItemStack item, int slot, ClickType clickType) {
        if (CONFIRM_SLOTS.contains(slot)) {
            plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_ARENA_DELETE, arena.getName());
            plugin.getArenaHandler().remove(arena);
            close(false);
            new ArenasGUI(plugin, player).open();
            return true;
        }

        if (CANCEL_SLOTS.contains(slot)) {
            close(false);
            new ArenaGUI(plugin, player, arena).open();
            return true;
        }

        return true;
    }

}