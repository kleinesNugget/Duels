package net.withery.duels.events.arena;

import net.withery.duels.arena.Arena;
import org.jetbrains.annotations.NotNull;

public class ArenaPostRemoveEvent extends ArenaEvent {

    public ArenaPostRemoveEvent(@NotNull Arena arena) {
        super(arena);
    }

}