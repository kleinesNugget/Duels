package net.withery.duels.gui;

import net.withery.duels.Duels;
import net.withery.duels.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ArenasGUI implements GUI {

    private final static Map<UUID, ArenasGUI> arenaGUIs = new HashMap<>();

    private final Duels plugin;
    private final Player player;
    private final Inventory inventory;

    public ArenasGUI(Duels plugin, Player player) {
        this.plugin = plugin;
        this.player = player;

        inventory = Bukkit.createInventory(this, 9 * 6, "Duel Arenas");
    }

    public void load() {
        int i = 0;
        for (Arena arena : plugin.getArenaHandler().getArenas()) {
            Arena.ArenaStatus arenaStatus = arena.getArenaStatus();
            Material material = arenaStatus.getMaterial();

            ItemStack itemStack = new ItemStack(material);
            ItemMeta itemMeta = itemStack.getItemMeta();

            if (itemMeta != null) {
                List<String> lore = new ArrayList<>();
                lore.add("");
                lore.add(ChatColor.GRAY + "Status: " + ChatColor.AQUA + arenaStatus.getName());

                itemMeta.setDisplayName(ChatColor.AQUA + arena.getName());
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
            }

            inventory.setItem(i++, itemStack);
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void open() {
        arenaGUIs.put(player.getUniqueId(), this);
        player.openInventory(inventory);
    }

    @Override
    public void close() {
        arenaGUIs.remove(player.getUniqueId());
        player.closeInventory();
    }

    @Override
    public void update() {

    }

    public static @Nullable ArenasGUI getArenasGUI(Player player) {
        return arenaGUIs.get(player.getUniqueId());
    }

    public static Map<UUID, ArenasGUI> getArenaGUIs() {
        return arenaGUIs;
    }

}