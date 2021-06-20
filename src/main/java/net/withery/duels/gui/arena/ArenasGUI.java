package net.withery.duels.gui.arena;

import net.withery.duels.Duels;
import net.withery.duels.arena.Arena;
import net.withery.duels.gui.GUI;
import net.withery.duels.utilities.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ArenasGUI implements GUI {

    private final static Map<Integer, Arena> arenaSlots = new HashMap<>();
    private final static Set<Integer> INVALID_SLOTS = new HashSet<>(Arrays.asList(17, 18, 26, 27));

    private final static int MAX_PER_PAGE = 21;

    private final static int ADD_SLOT = 49;
    private final static int PREVIOUS_SLOT = 46;
    private final static int NEXT_SLOT = 52;

    private final static ItemStack BACKGROUND = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName(" ").hideAttributes().build();
    private final static ItemStack NO_ARENAS = new ItemBuilder(Material.BARRIER).setName(ChatColor.RED + "No Arenas").hideAttributes().build();
    private final static ItemStack UNUSED_SLOT = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(" ").hideAttributes().build();
    private final static ItemStack ADD = new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(ChatColor.AQUA + "Add Arena").hideAttributes().build();
    private final static ItemStack NEXT = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName(ChatColor.AQUA + "Next Page").hideAttributes().build();
    private final static ItemStack NEXT_INACTIVE = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(ChatColor.GRAY + "Next Page").hideAttributes().build();
    private final static ItemStack PREVIOUS = new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName(ChatColor.AQUA + "Previous Page").hideAttributes().build();
    private final static ItemStack PREVIOUS_INACTIVE = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName(ChatColor.GRAY + "Previous Page").hideAttributes().build();

    private final Duels plugin;
    private final Player player;
    private final Inventory inventory;

    private int page;
    private int pages;
    private boolean previousPage;
    private boolean nextPage;

    public ArenasGUI(Duels plugin, Player player) {
        this.plugin = plugin;
        this.player = player;

        inventory = Bukkit.createInventory(this, 9 * 6, "Duel Arenas");
    }

    private void load() {
        page = 1;
        pages = (int) Math.ceil((double) plugin.getArenaHandler().getArenas().size() / MAX_PER_PAGE);

        previousPage = false;
        nextPage = pages > 1;

        inventory.clear();

        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, BACKGROUND);

        inventory.setItem(9 * 6 - 5, ADD);
        setArenas();
        setControls();
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void open() {
        load();
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
        pages = (int) Math.ceil((double) plugin.getArenaHandler().getArenas().size() / MAX_PER_PAGE);

        previousPage = page > 1;
        nextPage = pages > page;

        setArenas();
        setControls();
    }

    @Override
    public boolean onClickGUIItem(Player player, ItemStack item, int slot, ClickType clickType) {
        switch (slot) {
            case ADD_SLOT -> {
                close();
                player.sendMessage("Started adding process...");
                return true;
            }
            case PREVIOUS_SLOT -> {
                if (previousPage) {
                    page--;
                    update();
                }
                return true;
            }
            case NEXT_SLOT -> {
                if (nextPage) {
                    page++;
                    update();
                }
                return true;
            }
        }

        Arena arena = arenaSlots.get(slot);
        if (arena == null) return true;

        if (clickType.isShiftClick() && clickType.isRightClick()) {
            arena.setDisabled(!arena.isDisabled());
            update();
        }
        return true;
    }

    private void setArenas() {
        arenaSlots.clear();
        for (int i = 10; i < 35; i++) {
            if (INVALID_SLOTS.contains(i)) continue;

            inventory.setItem(i, UNUSED_SLOT);
        }

        List<Arena> arenas = plugin.getArenaHandler().getArenas().stream().sorted(Comparator.comparing(Arena::getName)).collect(Collectors.toList());

        if (arenas.isEmpty()) {
            inventory.setItem(9 * 3 - 5, NO_ARENAS);
            return;
        }

        int i = 10;
        int skip = (page - 1) * MAX_PER_PAGE;
        for (Arena arena : arenas) {
            if (skip-- > 0) {
                player.sendMessage("Skipping " + arena.getName());
                continue;
            }

            while (INVALID_SLOTS.contains(i)) {
                i++;
                if (i >= 35) break;
            }

            Arena.ArenaStatus arenaStatus = arena.getArenaStatus();
            Material material = arenaStatus.getMaterial();

            ItemBuilder itemBuilder = new ItemBuilder(material)
                    .setName(ChatColor.AQUA + arena.getName() + " Arena")
                    .addLoreLine("")
                    .addLoreLine(ChatColor.GRAY + "Status: " + ChatColor.AQUA + arenaStatus.getName())
                    .addLoreLine(ChatColor.GRAY + "Disabled: " + ChatColor.AQUA + arena.isDisabled())
                    .hideAttributes();

            arenaSlots.put(i, arena);
            inventory.setItem(i++, itemBuilder.build());
            if (i == 35) break;
        }

    }

    public void setControls() {
        inventory.setItem(9 * 6 - 8, (previousPage ? PREVIOUS : PREVIOUS_INACTIVE));
        inventory.setItem(9 * 6 - 2, (nextPage ? NEXT : NEXT_INACTIVE));
    }

}