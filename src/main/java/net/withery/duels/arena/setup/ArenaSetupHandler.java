package net.withery.duels.arena.setup;

import net.withery.duels.Duels;
import net.withery.duels.arena.Arena;
import net.withery.duels.config.locale.LocaleReference;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record ArenaSetupHandler(Duels plugin) {

    private final static Map<UUID, ArenaSetup> arenaSetups = new HashMap<>();

    public void startSetup(Player player, Arena arena, SetupType setupType) {
        switch (setupType) {
            case ARENA -> plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_ARENA_START);
            case NAME -> plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_NAME_START);
            case POSITION_1 -> plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_POSITION_START, "1");
            case POSITION_2 -> plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_POSITION_START, "2");
            case SPAWN_1 -> plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_SPAWN_START, "1");
            case SPAWN_2 -> plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_SPAWN_START, "2");
        }

        arenaSetups.put(player.getUniqueId(), new ArenaSetup(player, arena, setupType));
    }

    public ArenaSetup getArenaSetup(Player player) {
        return arenaSetups.get(player.getUniqueId());
    }

    public class ArenaSetup {

        private final Player player;
        private Arena arena;
        private final SetupType setupType;

        private final BukkitTask bukkitTask;

        public ArenaSetup(Player player, Arena arena, SetupType setupType) {
            this.player = player;
            this.arena = arena;
            this.setupType = setupType;

            bukkitTask = Bukkit.getScheduler().runTaskLater(plugin, () -> {
                plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_EXPIRED);
                stop();
            }, 20L * 60L);
        }

        public void stop() {
            arenaSetups.remove(player.getUniqueId());
            bukkitTask.cancel();
        }

        public Player getPlayer() {
            return player;
        }

        public SetupType getSetupType() {
            return setupType;
        }

        public void setArena(Arena arena) {
            this.arena = arena;
        }

        public Arena getArena() {
            return arena;
        }

    }

    public enum SetupType {

        ARENA,
        NAME,
        POSITION_1,
        POSITION_2,
        SPAWN_1,
        SPAWN_2

    }

}