package net.withery.duels.events.arena;

import net.withery.duels.arena.Arena;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public class ArenaPreEditEvent extends ArenaEditEvent implements Cancellable {

    private boolean cancelled = false;

    public ArenaPreEditEvent(@NotNull Arena arena, @NotNull EditType editType) {
        super(arena, editType);
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