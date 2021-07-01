package net.withery.duels.listener.gui;

import net.withery.duels.Duels;
import net.withery.duels.events.arena.ArenaPostCreateEvent;
import net.withery.duels.events.arena.ArenaPostEditEvent;
import net.withery.duels.events.arena.ArenaPostRemoveEvent;
import net.withery.duels.gui.GUI;
import net.withery.duels.gui.arena.ArenaGUI;
import net.withery.duels.gui.arena.ArenasGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public record ArenaGUIListener(Duels plugin) implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onArenaPostCreateEvent(ArenaPostCreateEvent event) {
        for (GUI gui : plugin.getGuiHandler().getGUIs().values()) {
            if (gui instanceof ArenasGUI || gui instanceof ArenaGUI)
                gui.update();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onArenaPostEditEvent(ArenaPostEditEvent event) {
        for (GUI gui : plugin.getGuiHandler().getGUIs().values()) {
            if (gui instanceof ArenasGUI || gui instanceof ArenaGUI)
                gui.update();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onArenaPostRemoveEvent(ArenaPostRemoveEvent event) {
        for (GUI gui : plugin.getGuiHandler().getGUIs().values()) {
            if (gui instanceof ArenasGUI || gui instanceof ArenaGUI)
                gui.update();
        }
    }

}