package net.withery.duels.listener.arena;

import net.withery.duels.Duels;
import net.withery.duels.events.arena.ArenaPostCreateEvent;
import net.withery.duels.events.arena.ArenaPostEditEvent;
import net.withery.duels.events.arena.ArenaPostRemoveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public record ArenaChangesListener(Duels plugin) implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onArenaPostCreateEvent(ArenaPostCreateEvent event) {
        plugin.getArenaHandler().setArenaChanges(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onArenaPostEditEvent(ArenaPostEditEvent event) {
        plugin.getArenaHandler().setArenaChanges(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onArenaPostRemoveEvent(ArenaPostRemoveEvent event) {
        plugin.getArenaHandler().setArenaChanges(true);
    }

}