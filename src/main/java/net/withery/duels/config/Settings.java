package net.withery.duels.config;

import net.withery.duels.Duels;
import org.bukkit.configuration.file.FileConfiguration;

public class Settings {

    private final FileConfiguration config;

    public Settings(Duels plugin) {
        plugin.getLogger().info("Loading Settings...");
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
    }

    public boolean isDebug() {
        return config.getBoolean("debug", false);
    }

    public long getArenasAutoSavingInterval() {
        return config.getLong("arenas.auto-save-interval", 300 );
    }

}