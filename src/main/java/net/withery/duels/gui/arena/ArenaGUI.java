package net.withery.duels.gui.arena;

import net.withery.duels.Duels;
import net.withery.duels.arena.Arena;
import net.withery.duels.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class ArenaGUI implements GUI {

    private final Duels plugin;
    private final Player player;
    private final Arena arena;
    private final Inventory inventory;

    public ArenaGUI(Duels plugin, Player player, Arena arena) {
        this.plugin = plugin;
        this.player = player;
        this.arena = arena;

        inventory = Bukkit.createInventory(this, 9 * 3, arena.getName() + " Arena");
    }

    @Override
    public void open() {
        plugin.getGuiHandler().setGUI(player, this);
        player.openInventory(inventory);
    }

    @Override
    public void close() {
        plugin.getGuiHandler().removeGUI(player);
        player.closeInventory();
    }

    @Override
    public void update() {

    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }

}