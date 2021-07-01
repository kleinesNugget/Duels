package net.withery.duels.arena;

import net.withery.duels.events.arena.ArenaEditEvent;
import net.withery.duels.events.arena.ArenaPostEditEvent;
import net.withery.duels.events.arena.ArenaPreEditEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Region {

    private final Arena arena;
    private Location position1;
    private Location position2;

    public Region(@NotNull Arena arena) {
        this(arena, null, null);
    }

    public Region(@NotNull Arena arena, @Nullable Location position1, @Nullable Location position2) {
        this.arena = arena;
        this.position1 = position1;
        this.position2 = position2;
    }

    /**
     * Checks if the given location is inside the region
     *
     * @param location location to check if its inside the region
     * @return if the location is inside the region
     */
    public boolean contains(@NotNull Location location) {
        if (position1 == null || position2 == null) return false;
        if (position1.getWorld() == null || position2.getWorld() == null || location.getWorld() == null) return false;
        if (!position1.getWorld().getName().equals(location.getWorld().getName())) return false;

        boolean betweenX = isBetween(position1.getBlockX(), position2.getBlockX(), location.getBlockX());
        boolean betweenY = isBetween(position1.getBlockY(), position2.getBlockY(), location.getBlockY());
        boolean betweenZ = isBetween(position1.getBlockZ(), position2.getBlockZ(), location.getBlockZ());

        return betweenX && betweenY && betweenZ;
    }

    /**
     * Check if c is between a and b
     *
     * @param a point 1
     * @param b point 2
     * @param c point to check if in between a and b
     * @return if c is between a and b
     */
    private boolean isBetween(double a, double b, double c) {
        double min = Math.min(a, b);
        double max = Math.max(a, b);

        return (c >= min) && (c <= max);
    }

    /**
     * Sets the first corner of the region
     *
     * @param position1 {@link Location} of the first corner
     */
    public void setPosition1(@Nullable Location position1) {
        ArenaPreEditEvent event = new ArenaPreEditEvent(arena, ArenaEditEvent.EditType.POSITION_CHANGE);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        this.position1 = position1;

        Bukkit.getPluginManager().callEvent(new ArenaPostEditEvent(arena, ArenaEditEvent.EditType.POSITION_CHANGE));
    }

    /**
     * Gets the first corner of the region
     *
     * @return the first corner of the region
     */
    public @Nullable Location getPosition1() {
        return position1;
    }

    /**
     * Sets the second corner of the region
     *
     * @param position2 {@link Location} of the second corner
     */
    public void setPosition2(@Nullable Location position2) {
        ArenaPreEditEvent event = new ArenaPreEditEvent(arena, ArenaEditEvent.EditType.POSITION_CHANGE);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        this.position2 = position2;

        Bukkit.getPluginManager().callEvent(new ArenaPostEditEvent(arena, ArenaEditEvent.EditType.POSITION_CHANGE));
    }

    /**
     * Gets the second corner of the region
     *
     * @return the second corner of the region
     */
    public @Nullable Location getPosition2() {
        return position2;
    }

    /**
     * Gets whether both positions of the region are set
     *
     * @return true if both positions are set
     */
    public boolean isComplete() {
        return position1 != null && position2 != null;
    }

}