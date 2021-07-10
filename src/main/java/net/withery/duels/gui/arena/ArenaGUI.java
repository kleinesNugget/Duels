package net.withery.duels.gui.arena;

import net.withery.duels.Duels;
import net.withery.duels.arena.Arena;
import net.withery.duels.arena.setup.ArenaSetupHandler;
import net.withery.duels.config.locale.LocaleReference;
import net.withery.duels.gui.CustomGUI;
import net.withery.duels.utilities.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
                .setName(ChatColor.GRAY + "Arena " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + arena.getName())
                .addLoreLine("")
                .addLoreLine(ChatColor.GRAY + "Status " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + arena.getArenaStatus().getName())
                .addLoreLine("")
                .addLoreLine(ChatColor.GRAY + "Left Click " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + "Rename")
                .addLoreLine(ChatColor.GRAY + "Right Click " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + "Disable/Enable")
                .addLoreLine(ChatColor.GRAY + "Shift + Right Click " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + "Delete")
                .build());

        ItemBuilder corner1Item = new ItemBuilder((arena.getRegion().getPosition1() != null ? Material.LIME_CONCRETE_POWDER : Material.RED_CONCRETE_POWDER))
                .hideAttributes()
                .setName(ChatColor.GRAY + "Corner " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + "1")
                .addLoreLine("")
                .addLoreLine(ChatColor.GRAY + "Position " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + (arena.getRegion().getPosition1() != null ? "" : "Not set"));

        if (arena.getRegion().getPosition1() != null) {
            Location position1 = arena.getRegion().getPosition1();
            corner1Item.addLoreLine(ChatColor.GRAY + " x " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + position1.getBlockX());
            corner1Item.addLoreLine(ChatColor.GRAY + " y " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + position1.getBlockY());
            corner1Item.addLoreLine(ChatColor.GRAY + " z " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + position1.getBlockZ());
        }

        corner1Item.addLoreLine("");
        corner1Item.addLoreLine(ChatColor.GRAY + "Left Click " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + "Set");
        corner1Item.addLoreLine(ChatColor.GRAY + "Shift + Right Click " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + "Unset");

        inventory.setItem(POSITION_1_SLOT, corner1Item.build());

        ItemBuilder corner2Item = new ItemBuilder((arena.getRegion().getPosition2() != null ? Material.LIME_CONCRETE_POWDER : Material.RED_CONCRETE_POWDER))
                .hideAttributes()
                .setName(ChatColor.GRAY + "Corner " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + "2")
                .addLoreLine("")
                .addLoreLine(ChatColor.GRAY + "Position " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + (arena.getRegion().getPosition2() != null ? "" : "Not set"));

        if (arena.getRegion().getPosition2() != null) {
            Location position2 = arena.getRegion().getPosition2();
            corner2Item.addLoreLine(ChatColor.GRAY + " x " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + position2.getBlockX());
            corner2Item.addLoreLine(ChatColor.GRAY + " y " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + position2.getBlockY());
            corner2Item.addLoreLine(ChatColor.GRAY + " z " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + position2.getBlockZ());
        }

        corner2Item.addLoreLine("");
        corner2Item.addLoreLine(ChatColor.GRAY + "Left Click " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + "Set");
        corner2Item.addLoreLine(ChatColor.GRAY + "Shift + Right Click " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + "Unset");

        inventory.setItem(POSITION_2_SLOT, corner2Item.build());

        ItemBuilder spawn1Item = new ItemBuilder((arena.getSpawn1() != null ? (arena.getRegion().isComplete() ? Material.LIME_CONCRETE_POWDER : Material.RED_CONCRETE_POWDER) : Material.RED_CONCRETE_POWDER))
                .hideAttributes()
                .setName(ChatColor.GRAY + "Spawn " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + "1")
                .addLoreLine("")
                .addLoreLine(ChatColor.GRAY + "Position " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + (arena.getRegion().getPosition1() != null ? "" : "Not set"));

        if (arena.getSpawn1() != null) {
            Location spawn1 = arena.getSpawn1();
            spawn1Item.addLoreLine(ChatColor.GRAY + " x " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + Math.round(spawn1.getX() * 1_000D) / 1_000D);
            spawn1Item.addLoreLine(ChatColor.GRAY + " y " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + Math.round(spawn1.getY() * 1_000D) / 1_000D);
            spawn1Item.addLoreLine(ChatColor.GRAY + " z " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + Math.round(spawn1.getZ() * 1_000D) / 1_000D);
            spawn1Item.addLoreLine(ChatColor.GRAY + " yaw " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + Math.round(spawn1.getYaw() * 10F) / 10F);
            spawn1Item.addLoreLine(ChatColor.GRAY + " pitch " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + Math.round(spawn1.getPitch() * 10F) / 10F);
        }

        spawn1Item.addLoreLine("");
        spawn1Item.addLoreLine(ChatColor.GRAY + "Left Click " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + "Set");
        spawn1Item.addLoreLine(ChatColor.GRAY + "Shift + Right Click " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + "Unset");

        inventory.setItem(SPAWN_1_SLOT, spawn1Item.build());

        ItemBuilder spawn2Item = new ItemBuilder((arena.getSpawn2() != null ? (arena.getRegion().isComplete() ? Material.LIME_CONCRETE_POWDER : Material.RED_CONCRETE_POWDER) : Material.RED_CONCRETE_POWDER))
                .hideAttributes()
                .setName(ChatColor.GRAY + "Spawn " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + "2")
                .addLoreLine("")
                .addLoreLine(ChatColor.GRAY + "Position " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + (arena.getRegion().getPosition1() != null ? "" : "Not set"));

        if (arena.getSpawn2() != null) {
            Location spawn2 = arena.getSpawn2();
            spawn2Item.addLoreLine(ChatColor.GRAY + " x " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + Math.round(spawn2.getX() * 1_000D) / 1_000D);
            spawn2Item.addLoreLine(ChatColor.GRAY + " y " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + Math.round(spawn2.getY() * 1_000D) / 1_000D);
            spawn2Item.addLoreLine(ChatColor.GRAY + " z " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + Math.round(spawn2.getZ() * 1_000D) / 1_000D);
            spawn2Item.addLoreLine(ChatColor.GRAY + " yaw " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + Math.round(spawn2.getYaw() * 10F) / 10F);
            spawn2Item.addLoreLine(ChatColor.GRAY + " pitch " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + Math.round(spawn2.getPitch() * 10F) / 10F);
        }

        spawn2Item.addLoreLine("");
        spawn2Item.addLoreLine(ChatColor.GRAY + "Left Click " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + "Set");
        spawn2Item.addLoreLine(ChatColor.GRAY + "Shift + Right Click " + ChatColor.DARK_GRAY + "» " + ChatColor.YELLOW + "Unset");

        inventory.setItem(SPAWN_2_SLOT, spawn2Item.build());
    }

    public Player getPlayer() {
        return player;
    }

}