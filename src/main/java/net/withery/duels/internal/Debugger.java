package net.withery.duels.internal;

import net.withery.duels.Duels;

import java.util.logging.Level;

public record Debugger(Duels plugin) {

    /**
     * Logs a message with the given {@link Level} to the console.
     * This will only log the message if debug in the config is set to true.
     *
     * @param level   the log level
     * @param message the message to log
     */
    public void log(Level level, String message) {
        if (plugin.getSettings().isDebug())
            plugin.getLogger().log(level, message);
    }

    /**
     * Logs a message with the given {@link Level} to the console.
     *
     * @param level   the log level
     * @param message the message to log
     * @param always  whether to send the message even though debug is set to false
     */
    public void log(Level level, String message, boolean always) {
        if (!always) {
            log(level, message);
            return;
        }

        plugin.getLogger().log(level, message);
    }

}