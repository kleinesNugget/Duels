package net.withery.duels.gui.arena;

import net.withery.duels.Duels;
import net.withery.duels.arena.Arena;
import net.withery.duels.arena.setup.ArenaSetupHandler;
import net.withery.duels.config.locale.LocaleReference;
import net.withery.duels.gui.CustomGUI;
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

public class ArenasGUI extends CustomGUI {

    private final static Map<Integer, Arena> arenaSlots = new HashMap<>();
    private final static Set<Integer> INVALID_SLOTS = new HashSet<>(Arrays.asList(17, 18, 26, 27));

    private final static int INVENTORY_SIZE = 9 * 6;

    private final static int MAX_PER_PAGE = 21;

    private final static int ADD_SLOT = INVENTORY_SIZE - 5;
    private final static int PREVIOUS_SLOT = INVENTORY_SIZE - 8;
    private final static int NEXT_SLOT = INVENTORY_SIZE - 2;

    private final static ItemStack NO_ARENAS = new ItemBuilder(Material.BARRIER).setName(ChatColor.RED + "No Arenas").hideAttributes().build();
    private final static ItemStack ADD = new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(ChatColor.AQUA + "Add Arena").hideAttributes().build();

    private final Duels plugin;
    private final Player player;

    private int page;
    private int pages;
    private boolean previousPage;
    private boolean nextPage;

    public ArenasGUI(Duels plugin, Player player) {
        this.plugin = plugin;
        this.player = player;

        inventory = Bukkit.createInventory(this, INVENTORY_SIZE, "Duel Arenas");
    }

    private void load() {
        page = 1;
        pages = (int) Math.ceil((double) plugin.getArenaHandler().getArenas().size() / MAX_PER_PAGE);

        previousPage = false;
        nextPage = pages > 1;

        fillBackground();

        inventory.setItem(INVENTORY_SIZE - 5, ADD);
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
    public void close(boolean inventory) {
        plugin.getGuiHandler().removeGUI(player);
        if (inventory)
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
        if (clickType == ClickType.DOUBLE_CLICK) return true;

        switch (slot) {
            case ADD_SLOT -> {
                close();
                plugin.getArenaSetupHandler().startSetup(player, null, ArenaSetupHandler.SetupType.ARENA);
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

        if (clickType.isLeftClick() && !clickType.isShiftClick()) {
            close(false);
            new ArenaGUI(plugin, player, arena).open();
            return true;
        }

        if (clickType.isShiftClick() && clickType.isRightClick()) {
            arena.setDisabled(!arena.isDisabled());
            plugin.getLocaleHandler().sendMessage(player, (arena.isDisabled() ? LocaleReference.ARENA_SETUP_ARENA_DISABLE : LocaleReference.ARENA_SETUP_ARENA_ENABLE), arena.getName());
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
            if (skip-- > 0) continue;
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
        inventory.setItem(INVENTORY_SIZE - 8, (previousPage ? PREVIOUS : PREVIOUS_INACTIVE));
        inventory.setItem(INVENTORY_SIZE - 2, (nextPage ? NEXT : NEXT_INACTIVE));
    }

    public Player getPlayer() {
        return player;
    }

}