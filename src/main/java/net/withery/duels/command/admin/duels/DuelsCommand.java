package net.withery.duels.command.admin.duels;

import net.withery.duels.Duels;
import net.withery.duels.command.ParentCommand;
import net.withery.duels.command.SubCommand;
import net.withery.duels.config.locale.LocaleReference;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class DuelsCommand extends ParentCommand {

    public DuelsCommand(Duels plugin) {
        super(plugin, "duels", "duels.command.admin.duels");

        register(new DuelsArenasSubCommand(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(permission)) {
            plugin.getLocaleHandler().sendMessage(sender, LocaleReference.NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            if (!sender.hasPermission("duels.command.admin.duels.help")) {
                plugin.getLocaleHandler().sendMessage(sender, LocaleReference.NO_PERMISSION);
                return true;
            }

            sendHelp(sender, 1);
            return true;
        }

        subLabel = args[0];
        subArgs = Arrays.copyOfRange(args, 1, args.length);

        if (!exists(subLabel)) {
            plugin.getLocaleHandler().sendMessage(sender, LocaleReference.INVALID_COMMAND);
            return true;
        }

        SubCommand subCommand = getExecutor(subLabel);

        if (!sender.hasPermission(subCommand.getPermission())) {
            plugin.getLocaleHandler().sendMessage(sender, LocaleReference.NO_PERMISSION);
            return true;
        }

        subCommand.onCommand(sender, command, subLabel, subArgs);
        return true;
    }

}