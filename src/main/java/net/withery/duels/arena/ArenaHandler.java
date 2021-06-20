package net.withery.duels.arena;

import net.withery.duels.Duels;
import net.withery.duels.events.arena.ArenaCreateEvent;
import net.withery.duels.events.arena.ArenaRemoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

public class ArenaHandler {

    private final Duels plugin;

    private final Set<Arena> arenas = new HashSet<>();
    private final File file;

    private BukkitTask autoSaveTask;
    private boolean arenaChanges = false;

    public ArenaHandler(Duels plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "arenas.json");
    }

    public void create(@NotNull String name) {
        Arena arena = new Arena(name);
        ArenaCreateEvent event = new ArenaCreateEvent(arena);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        arenas.add(arena);
    }

    public void remove(@NotNull Arena arena) {
        ArenaRemoveEvent event = new ArenaRemoveEvent(arena);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;

        arenas.remove(arena);
    }

    public @Nullable Arena get(@NotNull String name) {
        for (Arena arena : arenas) {
            if (arena.getName().equals(name))
                return arena;
        }

        return null;
    }

    public @Nullable Arena getRandom() {
        List<Arena> available = new ArrayList<>(arenas);
        Arena selected = null;

        while (selected == null || !selected.isComplete() || selected.isDisabled() || selected.getDuel() != null && !available.isEmpty()) {
            int random = ThreadLocalRandom.current().nextInt(available.size());
            selected = available.remove(random);
        }

        return selected;
    }

    public @NotNull Set<Arena> getArenas() {
        return arenas;
    }

    public void setArenaChanges(boolean arenaChanges) {
        this.arenaChanges = arenaChanges;
    }

    public boolean hasArenaChanges() {
        return arenaChanges;
    }

    public void load() {
        if (!file.exists()) {
            if (!createFile())
                return;
        } else {
            try (Reader reader = new InputStreamReader(new FileInputStream(file))) {
                Arena[] arenas = plugin.getGson().fromJson(reader, Arena[].class);
                if (arenas != null)
                    this.arenas.addAll(Arrays.asList(arenas));

                plugin.getDebugger().log(Level.INFO, "Loaded " + this.arenas.size() + " arenas");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        startSavingTask();
    }

    public void unload() {
        if (autoSaveTask != null)
            autoSaveTask.cancel();

        saveArenas(true);
    }

    private void startSavingTask() {
        final long AUTO_SAVE_INTERVAL = 20L * plugin.getSettings().getArenasAutoSavingInterval();
        autoSaveTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> saveArenas(false), AUTO_SAVE_INTERVAL, AUTO_SAVE_INTERVAL);
    }

    private boolean createFile() {
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                plugin.getDebugger().log(Level.SEVERE, "An error occurred while creating a directory!", true);
                return false;
            }
        }

        try {
            if (!file.createNewFile()) {
                plugin.getDebugger().log(Level.SEVERE, "An error occurred while creating a file!", true);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void saveArenas(boolean force) {
        if (!arenaChanges && !force) {
            plugin.getDebugger().log(Level.INFO, "No arena changes, skipping save...");
            return;
        }

        plugin.getDebugger().log(Level.INFO, "Saving arena changes to file...");
        if (!file.exists())
            if (!createFile())
                return;

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file))) {
            plugin.getGson().toJson(arenas.toArray(Arena[]::new), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        arenaChanges = false;
    }

}