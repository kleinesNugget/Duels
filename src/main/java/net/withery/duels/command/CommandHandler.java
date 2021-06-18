package net.withery.duels.command;

import net.withery.duels.Duels;
import net.withery.duels.command.admin.duels.DuelsCommand;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public record CommandHandler(Duels plugin) {

    public void load() {
        registerCommands();
    }

    private void registerCommands() {
        registerCommand("duels", new DuelsCommand(plugin));
    }

    private void registerCommand(@NotNull String name, @NotNull CommandExecutor commandExecutor) {
        PluginCommand command = plugin.getCommand(name);

        // Checking whether the command is registered in the plugin.yml
        if (command == null) {
            plugin.getDebugger().log(Level.WARNING, "Command \"" + name + "\" is not registered in the plugin.yml! Skipping command...");
            return;
        }

        command.setExecutor(commandExecutor);

        // Using command executor as tab completer if it is one
        if (commandExecutor instanceof TabCompleter)
            command.setTabCompleter((TabCompleter) commandExecutor);

        plugin.getDebugger().log(Level.INFO, "Registered command " + command.getName());
    }

}