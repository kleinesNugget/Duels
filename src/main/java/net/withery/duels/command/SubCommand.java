package net.withery.duels.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class SubCommand {

    private final String name;
    private final String[] aliases;
    private final String permission;
    private final String description;
    private final String usage;

    protected SubCommand(@NotNull String name, @NotNull String permission, @NotNull String description, @Nullable String usage, String... aliases) {
        this.name = name;
        this.permission = permission;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String[] getAliases() {
        return aliases;
    }

    public abstract void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);

    public abstract List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args);

    public List<String> getVisiblePlayers(@NotNull CommandSender sender) {
        List<String> results = new ArrayList<>();

        if (sender instanceof Player) {
            for (Player player : Bukkit.getOnlinePlayers())
                if (((Player) sender).canSee(player))
                    results.add(player.getName());
        } else {
            for (Player player : Bukkit.getOnlinePlayers())
                results.add(player.getName());
        }

        return results;
    }

}