package net.withery.duels.command;

import net.withery.duels.Duels;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class ParentCommand implements CommandExecutor, TabCompleter {

    protected final Duels plugin;
    protected final String permission;

    private final Set<SubCommand> subCommands = new HashSet<>();

    protected String subLabel;
    protected String[] subArgs;

    public ParentCommand(Duels plugin, String permission) {
        this.plugin = plugin;
        this.permission = permission;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission(permission)) return null;

        List<String> results = null;
        if (args.length == 1) {
            results = new ArrayList<>();
            for (SubCommand subCommand : getSubCommands()) {
                if (sender.hasPermission(subCommand.getPermission())) {
                    results.add(subCommand.getName());
                    results.addAll(Arrays.asList(subCommand.getAliases()));
                }
            }
        }

        if (args.length >= 2) {
            results = new ArrayList<>();
            subLabel = args[0];
            subArgs = Arrays.copyOfRange(args, 1, args.length);

            SubCommand subCommand = getExecutor(subLabel);

            if (subCommand != null && sender.hasPermission(subCommand.getPermission()))
                results.addAll(subCommand.onTabComplete(sender, command, subLabel, subArgs));
        }

        if (results != null && !results.isEmpty()) {
            results = StringUtil.copyPartialMatches(args[args.length - 1], results, new ArrayList<>(results.size()));
            results.sort(String.CASE_INSENSITIVE_ORDER);
        }

        return results;
    }

    public void register(SubCommand subCommand) {
        subCommands.add(subCommand);
    }

    public boolean exists(String label) {
        for (SubCommand subCommand : subCommands)
            if (subCommand.getName().equalsIgnoreCase(label))
                return true;

            else
                for (String alias : subCommand.getAliases())
                    if (alias.equalsIgnoreCase(label))
                        return true;

        return false;
    }

    public SubCommand getExecutor(String label) {
        for (SubCommand subCommand : subCommands)
            if (subCommand.getName().equalsIgnoreCase(label))
                return subCommand;

            else
                for (String alias : subCommand.getAliases())
                    if (alias.equalsIgnoreCase(label))
                        return subCommand;

        return null;
    }

    public Set<SubCommand> getSubCommands() {
        return subCommands;
    }

}