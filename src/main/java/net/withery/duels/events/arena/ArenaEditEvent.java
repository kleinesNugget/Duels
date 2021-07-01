package net.withery.duels.events.arena;

import net.withery.duels.arena.Arena;
import org.jetbrains.annotations.NotNull;

public abstract class ArenaEditEvent extends ArenaEvent {

    private final ArenaEditEvent.EditType editType;

    public ArenaEditEvent(@NotNull Arena arena, @NotNull ArenaEditEvent.EditType editType) {
        super(arena);

        this.editType = editType;
    }

    public @NotNull ArenaEditEvent.EditType getEditType() {
        return editType;
    }

    public enum EditType {

        NAME_CHANGE,
        POSITION_CHANGE,
        SPAWN_CHANGE,
        DISABLE

    }

}