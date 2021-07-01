package net.withery.duels.events.arena;

import net.withery.duels.arena.Arena;
import org.jetbrains.annotations.NotNull;

public class ArenaPostCreateEvent extends ArenaEvent {

    public ArenaPostCreateEvent(@NotNull Arena arena) {
        super(arena);
    }

}