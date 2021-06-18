package net.withery.duels.command.admin.duels;

import net.withery.duels.command.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class DuelsArenasSubCommand extends SubCommand {

    protected DuelsArenasSubCommand() {
        super("arenas", "duels.command.admin.duels.arenas", "Open a GUI to manage arenas", null);
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Collections.emptyList();
    }

}