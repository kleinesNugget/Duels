package net.withery.duels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.withery.duels.arena.Arena;
import net.withery.duels.arena.ArenaHandler;
import net.withery.duels.arena.ArenaTypeAdapter;
import net.withery.duels.command.CommandHandler;
import net.withery.duels.config.Settings;
import net.withery.duels.internal.Debugger;
import net.withery.duels.kit.KitHandler;
import net.withery.duels.listener.arena.ArenaChangesListener;
import net.withery.duels.listener.gui.GUIListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Duels extends JavaPlugin {

    private Gson gson;
    private Settings settings;
    private Debugger debugger;
    private ArenaHandler arenaHandler;
    private KitHandler kitHandler;

    @Override
    public void onEnable() {
        initialize();

        getLogger().info("Enabled " + getDescription().getName() + " v" + getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        terminate();

        getLogger().info("Disabled " + getDescription().getName() + " v" + getDescription().getVersion());
    }

    private void initialize() {
        loadGson();
        settings = new Settings(this);
        debugger = new Debugger(this);

        getDebugger().log(Level.INFO, "Loading Arena Handler...");
        arenaHandler = new ArenaHandler(this);
        arenaHandler.load();

        getDebugger().log(Level.INFO, "Loading Kit Handler...");
        kitHandler = new KitHandler(this);
        kitHandler.load();

        getDebugger().log(Level.INFO, "Loading Command Handler...");
        CommandHandler commandHandler = new CommandHandler(this);
        commandHandler.load();

        getDebugger().log(Level.INFO, "Registering listeners...");
        registerListeners();
    }

    private void terminate() {
        arenaHandler.unload();
    }

    private void loadGson() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Arena[].class, new ArenaTypeAdapter())
                .create();
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new ArenaChangesListener(this), this);
        Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
    }

    public Gson getGson() {
        return gson;
    }

    public Settings getSettings() {
        return settings;
    }

    public Debugger getDebugger() {
        return debugger;
    }

    public ArenaHandler getArenaHandler() {
        return arenaHandler;
    }

    public KitHandler getKitHandler() {
        return kitHandler;
    }

}