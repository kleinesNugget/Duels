package net.withery.duels.events.arena;

import net.withery.duels.arena.Arena;
import org.jetbrains.annotations.NotNull;

public class ArenaPostEditEvent extends ArenaEditEvent  {

    public ArenaPostEditEvent(@NotNull Arena arena, @NotNull ArenaEditEvent.EditType editType) {
        super(arena, editType);
    }

}