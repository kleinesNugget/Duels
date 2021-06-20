package net.withery.duels.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUIHandler {

    private final static Map<UUID, GUI> guis = new HashMap<>();

    public void setGUI(@NotNull Player player, @NotNull GUI gui) {
        guis.put(player.getUniqueId(), gui);
    }

    public void removeGUI(@NotNull Player player) {
        guis.remove(player.getUniqueId());
    }

    public @Nullable GUI getGUI(@NotNull Player player) {
        return guis.get(player.getUniqueId());
    }

    public static @NotNull Map<UUID, GUI> getGUIs() {
        return guis;
    }

}