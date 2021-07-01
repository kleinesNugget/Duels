package net.withery.duels.command.admin.duels;

import net.withery.duels.Duels;
import net.withery.duels.command.ParentCommand;
import net.withery.duels.command.SubCommand;
import net.withery.duels.config.locale.LocaleReference;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DuelsCommand extends ParentCommand {

    public DuelsCommand(Duels plugin) {
        super(plugin, "duels.command.admin.duels");

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

    protected void sendHelp(CommandSender sender, int page) {
        final int COMMANDS_PER_PAGE = 9;
        final int COMMANDS = getSubCommands().size();
        final int PAGES = (int) Math.ceil((double) COMMANDS / COMMANDS_PER_PAGE);

        if (page < 0)
            page = 1;

        if (page > PAGES)
            page = PAGES;

        List<SubCommand> subCommands = getSubCommands().stream().sorted(Comparator.comparing(SubCommand::getName)).collect(Collectors.toList());

        sender.sendMessage("HELP " + page + "/" + PAGES);

        int i = 0;
        for (SubCommand subCommand : subCommands) {
            if (i < ((page - 1) * COMMANDS_PER_PAGE)) {
                i++;
                continue;
            }

            if (i++ >= (COMMANDS_PER_PAGE * page)) break;
            String usage = subCommand.getUsage();
            sender.sendMessage("/parties " + subCommand.getName() + " " + (usage == null ? "" : usage) + " - " + subCommand.getDescription());
        }
    }

}