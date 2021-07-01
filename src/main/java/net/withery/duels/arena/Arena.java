package net.withery.duels.arena;

import net.withery.duels.duel.Duel;
import net.withery.duels.events.arena.ArenaEditEvent;
import net.withery.duels.events.arena.ArenaPostEditEvent;
import net.withery.duels.events.arena.ArenaPreEditEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Arena {

    private final Region region;

    private String name;
    private Location spawn1;
    private Location spawn2;
    private boolean disabled;
    private Duel duel;

    public Arena(@NotNull String name) {
        this(name, null, null, null, null, false);
    }

    public Arena(@NotNull String name, @Nullable Location position1, @Nullable Location position2, @Nullable Location spawn1, @Nullable Location spawn2, boolean disabled) {
        this.region = new Region(this, position1, position2);

        this.name = name;
        this.spawn1 = spawn1;
        this.spawn2 = spawn2;
        this.disabled = disabled;
    }

    public void setName(@NotNull String name) {
        ArenaPreEditEvent event = new ArenaPreEditEvent(this, ArenaEditEvent.EditType.NAME_CHANGE);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        this.name = name;

        Bukkit.getPluginManager().callEvent(new ArenaPostEditEvent(this, ArenaEditEvent.EditType.NAME_CHANGE));
    }

    public @NotNull String getName() {
        return name;
    }

    public Region getRegion() {
        return region;
    }

    public void setSpawn1(@Nullable Location spawn1) {
        ArenaPreEditEvent event = new ArenaPreEditEvent(this, ArenaEditEvent.EditType.SPAWN_CHANGE);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        this.spawn1 = spawn1;

        Bukkit.getPluginManager().callEvent(new ArenaPostEditEvent(this, ArenaEditEvent.EditType.SPAWN_CHANGE));
    }

    public @Nullable Location getSpawn1() {
        return spawn1;
    }

    public void setSpawn2(@Nullable Location spawn2) {
        ArenaPreEditEvent event = new ArenaPreEditEvent(this, ArenaEditEvent.EditType.SPAWN_CHANGE);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        this.spawn2 = spawn2;

        Bukkit.getPluginManager().callEvent(new ArenaPostEditEvent(this, ArenaEditEvent.EditType.SPAWN_CHANGE));
    }

    public @Nullable Location getSpawn2() {
        return spawn2;
    }

    public void setDuel(@Nullable Duel duel) {
        this.duel = duel;
    }

    public @Nullable Duel getDuel() {
        return duel;
    }

    public void setDisabled(boolean disabled) {
        ArenaPreEditEvent event = new ArenaPreEditEvent(this, ArenaEditEvent.EditType.DISABLE);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        this.disabled = disabled;

        Bukkit.getPluginManager().callEvent(new ArenaPostEditEvent(this, ArenaEditEvent.EditType.DISABLE));
    }

    public boolean isDisabled() {
        return disabled;
    }

    public boolean isComplete() {
        return region.getPosition1() != null && region.getPosition2() != null && spawn1 != null && spawn2 != null;
    }

    public @NotNull ArenaStatus getArenaStatus() {
        if (getDuel() != null)
            return ArenaStatus.IN_USE;

        if (!isComplete())
            return ArenaStatus.INCOMPLETE;

        if (isDisabled())
            return ArenaStatus.DISABLED;

        return ArenaStatus.COMPLETE;
    }

    public enum ArenaStatus {

        DISABLED("Disabled", ChatColor.DARK_RED, Material.GRAY_CONCRETE_POWDER),
        INCOMPLETE("Incomplete", ChatColor.RED, Material.RED_CONCRETE_POWDER),
        COMPLETE("Complete", ChatColor.GREEN, Material.LIME_CONCRETE_POWDER),
        IN_USE("In use", ChatColor.AQUA, Material.LIGHT_BLUE_CONCRETE_POWDER);

        private final String name;
        private final ChatColor chatColor;
        private final Material material;

        ArenaStatus(String name, ChatColor chatColor, Material material) {
            this.name = name;
            this.chatColor = chatColor;
            this.material = material;
        }

        public String getName() {
            return name;
        }

        public ChatColor getChatColor() {
            return chatColor;
        }

        public Material getMaterial() {
            return material;
        }

    }

}