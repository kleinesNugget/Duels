package net.withery.duels.listener.arena.setup;

import net.withery.duels.Duels;
import net.withery.duels.arena.Arena;
import net.withery.duels.arena.setup.ArenaSetupHandler;
import net.withery.duels.config.locale.LocaleReference;
import net.withery.duels.gui.arena.ArenaGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.atomic.AtomicReference;

public record ArenaSetupListener(Duels plugin) implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ArenaSetupHandler.ArenaSetup arenaSetup = plugin.getArenaSetupHandler().getArenaSetup(player);
        if (arenaSetup == null) return;

        AtomicReference<Arena> arena = new AtomicReference<>(arenaSetup.getArena());
        String message = event.getMessage();
        if (message.equalsIgnoreCase("cancel")) {
            event.setCancelled(true);
            plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_CANCEL);
            arenaSetup.stop();
            return;
        }

        if (arenaSetup.getSetupType() == ArenaSetupHandler.SetupType.ARENA || arenaSetup.getSetupType() == ArenaSetupHandler.SetupType.NAME) {
            event.setCancelled(true);
            Bukkit.getScheduler().runTask(plugin, () -> {
                String name = ChatColor.stripColor(message);
                switch (arenaSetup.getSetupType()) {
                    case ARENA -> {
                        if (plugin.getArenaHandler().get(name) != null) {
                            plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_NAME_EXISTS);
                            new ArenaGUI(plugin, player, arena.get()).open();
                            arenaSetup.stop();
                            return;
                        }

                        arena.set(plugin.getArenaHandler().create(name));
                        if (arena.get() == null) {
                            plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_ARENA_CANCELLED);
                            new ArenaGUI(plugin, player, arena.get()).open();
                            arenaSetup.stop();
                            return;
                        }

                        plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_ARENA_DONE, name);
                    }
                    case NAME -> {
                        if (plugin.getArenaHandler().get(name) != null) {
                            plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_NAME_EXISTS);
                            new ArenaGUI(plugin, player, arena.get()).open();
                            arenaSetup.stop();
                            return;
                        }

                        arena.get().setName(name);
                        plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_NAME_DONE, name);
                    }
                }
                new ArenaGUI(plugin, player, arena.get()).open();
                arenaSetup.stop();
            });
            return;
        }

        if (arenaSetup.getSetupType() == ArenaSetupHandler.SetupType.SPAWN_1 || arenaSetup.getSetupType() == ArenaSetupHandler.SetupType.SPAWN_2) {
            if (!message.equalsIgnoreCase("confirm")) return;
            event.setCancelled(true);

            Bukkit.getScheduler().runTask(plugin, () -> {
                Location location = player.getLocation();
                if (!arena.get().getRegion().isComplete()) {
                    plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_SPAWN_POSITION_NEEDED);
                    new ArenaGUI(plugin, player, arena.get()).open();
                    arenaSetup.stop();
                    return;
                }

                if (!arena.get().getRegion().contains(location)) {
                    plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_SPAWN_INSIDE_ARENA);
                    new ArenaGUI(plugin, player, arena.get()).open();
                    arenaSetup.stop();
                    return;
                }

                switch (arenaSetup.getSetupType()) {
                    case SPAWN_1 -> {
                        arena.get().setSpawn1(location);
                        plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_SPAWN_DONE, "1");
                    }
                    case SPAWN_2 -> {
                        arena.get().setSpawn2(location);
                        plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_SPAWN_DONE, "2");
                    }
                }

                new ArenaGUI(plugin, player, arena.get()).open();
                arenaSetup.stop();
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ArenaSetupHandler.ArenaSetup arenaSetup = plugin.getArenaSetupHandler().getArenaSetup(player);
        if (arenaSetup == null) return;
        if (arenaSetup.getSetupType() != ArenaSetupHandler.SetupType.POSITION_1 && arenaSetup.getSetupType() != ArenaSetupHandler.SetupType.POSITION_2)
            return;
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;

        event.setCancelled(true);
        Location location = event.getClickedBlock().getLocation();

        Arena arena = arenaSetup.getArena();

        switch (arenaSetup.getSetupType()) {
            case POSITION_1 -> {
                arena.getRegion().setPosition1(location);
                plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_POSITION_DONE, "1");
            }
            case POSITION_2 -> {
                arena.getRegion().setPosition2(location);
                plugin.getLocaleHandler().sendMessage(player, LocaleReference.ARENA_SETUP_POSITION_DONE, "2");
            }
        }

        if (arena.isComplete()) {
            Location spawn1 = arena.getSpawn1();
            Location spawn2 = arena.getSpawn2();

            if (spawn1 != null && !arena.getRegion().contains(spawn1))
                arena.setSpawn1(null);

            if (spawn2 != null && !arena.getRegion().contains(spawn2))
                arena.setSpawn2(null);
        }

        new ArenaGUI(plugin, player, arenaSetup.getArena()).open();
        arenaSetup.stop();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ArenaSetupHandler.ArenaSetup arenaSetup = plugin.getArenaSetupHandler().getArenaSetup(player);
        if (arenaSetup == null) return;

        arenaSetup.stop();
    }

}