package net.withery.duels.events.arena;

import net.withery.duels.arena.Arena;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class ArenaEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Arena arena;

    public ArenaEvent(@NotNull Arena arena) {
        this.arena = arena;
    }

    public @NotNull Arena getArena() {
        return arena;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }

}