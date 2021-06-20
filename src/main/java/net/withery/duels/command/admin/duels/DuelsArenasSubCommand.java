package net.withery.duels.command.admin.duels;

import net.withery.duels.Duels;
import net.withery.duels.command.SubCommand;
import net.withery.duels.gui.arena.ArenasGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class DuelsArenasSubCommand extends SubCommand {

    private final Duels plugin;

    protected DuelsArenasSubCommand(Duels plugin) {
        super("arenas", "duels.command.admin.duels.arenas", "Open a GUI to manage arenas", null);

        this.plugin = plugin;
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            ArenasGUI arenasGUI = new ArenasGUI(plugin, player);
            arenasGUI.open();
            return;
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Collections.emptyList();
    }

}