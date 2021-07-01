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
import org.bukkit.inventory.ItemStack;

public class ArenaGUI extends CustomGUI {

    private final static int INVENTORY_SIZE = 9 * 3;

    private final static int ARENA_SLOT = 10;
    private final static int POSITION_1_SLOT = 12;
    private final static int POSITION_2_SLOT = 13;
    private final static int SPAWN_1_SLOT = 15;
    private final static int SPAWN_2_SLOT = 16;
    private final static int BACK_SLOT = INVENTORY_SIZE - 5;

    private final Duels plugin;
    private final Player player;
    private final Arena arena;

    public ArenaGUI(Duels plugin, Player player, Arena arena) {
        this.plugin = plugin;
        this.player = player;
        this.arena = arena;

        inventory = Bukkit.createInventory(this, INVENTORY_SIZE, arena.getName() + " Arena");
    }

    private void load() {
        fillBackground();

        setButtons();

        inventory.setItem(BACK_SLOT, BACK);
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
        setButtons();
    }

    @Override
    public boolean onClickGUIItem(Player player, ItemStack item, int slot, ClickType clickType) {
        if (clickType == ClickType.DOUBLE_CLICK) return true;

        switch (slot) {
            case ARENA_SLOT -> {
                if (clickType.isLeftClick()) {
                    close();
                    plugin.getArenaSetupHandler().startSetup(player, arena, ArenaSetupHandler.SetupType.NAME);
                    break;
                }
                if (clickType.isRightClick() && clickType.isShiftClick()) {
                    close(false);
                    new DeleteArenaGUI(plugin, player, arena).open();
                    break;
                }
                if (clickType.isRightClick()) {
                    arena.setDisabled(!arena.isDisabled());
                    plugin.getLocaleHandler().sendMessage(player, (arena.isDisabled() ? LocaleReference.ARENA_SETUP_ARENA_DISABLE : LocaleReference.ARENA_SETUP_ARENA_ENABLE), arena.getName());
                }
            }
            case POSITION_1_SLOT -> {
                if (clickType.isLeftClick()) {
                    close();
                    plugin.getArenaSetupHandler().startSetup(player, arena, ArenaSetupHandler.SetupType.POSITION_1);
                    break;
                }
                if (clickType.isRightClick() && clickType.isShiftClick()) {
                    arena.getRegion().setPosition1(null);
                    plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_POSITION_UNSET, "1");
                }
            }
            case POSITION_2_SLOT -> {
                if (clickType.isLeftClick()) {
                    close();
                    plugin.getArenaSetupHandler().startSetup(player, arena, ArenaSetupHandler.SetupType.POSITION_2);
                    break;
                }
                if (clickType.isRightClick() && clickType.isShiftClick()) {
                    arena.getRegion().setPosition2(null);
                    plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_POSITION_UNSET, "2");
                }
            }
            case SPAWN_1_SLOT -> {
                if (clickType.isRightClick() && clickType.isShiftClick()) {
                    arena.setSpawn1(null);
                    plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_SPAWN_UNSET, "1");
                    break;
                }
                if (!arena.getRegion().isComplete()) {
                    plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_SPAWN_POSITION_NEEDED);
                    break;
                }
                if (clickType.isLeftClick()) {
                    close();
                    plugin.getArenaSetupHandler().startSetup(player, arena, ArenaSetupHandler.SetupType.SPAWN_1);
                }
            }
            case SPAWN_2_SLOT -> {
                if (clickType.isRightClick() && clickType.isShiftClick()) {
                    arena.setSpawn2(null);
                    plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_SPAWN_UNSET, "2");
                    break;
                }
                if (!arena.getRegion().isComplete()) {
                    plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_SPAWN_POSITION_NEEDED);
                    break;
                }
                if (clickType.isLeftClick()) {
                    close();
                    plugin.getArenaSetupHandler().startSetup(player, arena, ArenaSetupHandler.SetupType.SPAWN_2);
                }
            }
            case BACK_SLOT -> {
                close(false);
                new ArenasGUI(plugin, player).open();
            }
        }

        return true;
    }

    private void setButtons() {
        inventory.setItem(ARENA_SLOT, new ItemBuilder(arena.getArenaStatus().getMaterial())
                .hideAttributes()
                .setName(ChatColor.AQUA + arena.getName())
                .addLoreLine("")
                .addLoreLine(ChatColor.GRAY + "Status: " + ChatColor.AQUA + arena.getArenaStatus().getName())
                .addLoreLine(ChatColor.GRAY + "Disabled: " + ChatColor.AQUA + arena.isDisabled())
                .addLoreLine("")
                .addLoreLine(ChatColor.AQUA + "Left Click: " + ChatColor.GRAY + "Rename")
                .addLoreLine(ChatColor.AQUA + "Right Click: " + ChatColor.GRAY + "Disable/Enable")
                .addLoreLine(ChatColor.AQUA + "Shift + Right Click: " + ChatColor.GRAY + "Delete")
                .build());

        inventory.setItem(POSITION_1_SLOT, new ItemBuilder((arena.getRegion().getPosition1() != null ? Material.LIME_CONCRETE_POWDER : Material.RED_CONCRETE_POWDER))
                .hideAttributes()
                .setName(ChatColor.AQUA + "Position 1")
                .addLoreLine("")
                .addLoreLine(ChatColor.GRAY + "Position: " + ChatColor.AQUA + (arena.getRegion().getPosition1() != null ? "x y z" : "Not set"))
                .build());
        inventory.setItem(POSITION_2_SLOT, new ItemBuilder((arena.getRegion().getPosition2() != null ? Material.LIME_CONCRETE_POWDER : Material.RED_CONCRETE_POWDER))
                .hideAttributes()
                .setName(ChatColor.AQUA + "Position 2")
                .addLoreLine("")
                .addLoreLine(ChatColor.GRAY + "Position: " + ChatColor.AQUA + (arena.getRegion().getPosition2() != null ? "x y z" : "Not set"))
                .build());

        inventory.setItem(SPAWN_1_SLOT, new ItemBuilder((!arena.isComplete() ? Material.GRAY_CONCRETE_POWDER : (arena.getSpawn1() != null ? Material.LIME_CONCRETE_POWDER : Material.RED_CONCRETE_POWDER)))
                .hideAttributes()
                .setName(ChatColor.AQUA + "Spawn 1")
                .addLoreLine("")
                .addLoreLine(ChatColor.GRAY + "Position: " + ChatColor.AQUA + (arena.getSpawn1() != null ? "x y z" : "Not set"))
                .build());
        inventory.setItem(SPAWN_2_SLOT, new ItemBuilder((!arena.isComplete() ? Material.GRAY_CONCRETE_POWDER : (arena.getSpawn2() != null ? Material.LIME_CONCRETE_POWDER : Material.RED_CONCRETE_POWDER)))
                .hideAttributes()
                .setName(ChatColor.AQUA + "Spawn 2")
                .addLoreLine("")
                .addLoreLine(ChatColor.GRAY + "Position: " + ChatColor.AQUA + (arena.getSpawn2() != null ? "x y z" : "Not set"))
                .build());
    }

    public Player getPlayer() {
        return player;
    }

}