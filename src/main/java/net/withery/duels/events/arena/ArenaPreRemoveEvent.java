package net.withery.duels.events.arena;

import net.withery.duels.arena.Arena;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public class ArenaPreRemoveEvent extends ArenaEvent implements Cancellable {

    private boolean cancelled = false;

    public ArenaPreRemoveEvent(@NotNull Arena arena) {
        super(arena);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}