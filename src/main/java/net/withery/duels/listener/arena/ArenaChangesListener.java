package net.withery.duels.listener.arena;

import net.withery.duels.Duels;
import net.withery.duels.events.arena.ArenaCreateEvent;
import net.withery.duels.events.arena.ArenaEditEvent;
import net.withery.duels.events.arena.ArenaRemoveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.logging.Level;

public record ArenaChangesListener(Duels plugin) implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onArenaCreateEvent(ArenaCreateEvent event) {
        plugin.getDebugger().log(Level.FINEST, "CREATED", true);
        plugin.getArenaHandler().setArenaChanges(true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onArenaEditEvent(ArenaEditEvent event) {
        plugin.getDebugger().log(Level.FINEST, "EDITED", true);
        plugin.getArenaHandler().setArenaChanges(true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onArenaRemoveEvent(ArenaRemoveEvent event) {
        plugin.getDebugger().log(Level.FINEST, "REMOVE", true);
        plugin.getArenaHandler().setArenaChanges(true);
    }

}