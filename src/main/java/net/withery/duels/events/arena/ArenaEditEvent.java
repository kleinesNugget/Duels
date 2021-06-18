package net.withery.duels.events.arena;

import net.withery.duels.arena.Arena;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

public class ArenaEditEvent extends ArenaEvent implements Cancellable {

    private final EditType editType;

    private boolean cancelled = false;

    public ArenaEditEvent(@NotNull Arena arena, @NotNull EditType editType) {
        super(arena);

        this.editType = editType;
    }

    public @NotNull EditType getEditType() {
        return editType;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public enum EditType {

        NAME_CHANGE,
        POSITION_CHANGE,
        SPAWN_CHANGE,
        DISABLE

    }

}